// port-lint: source into_url.rs
package io.github.kotlinmania.reqwest

/**
 * A parsed URL representation supporting RFC 3986 / WHATWG URL operations needed by Reqwest.
 */
public class Url internal constructor(
    private var schemeVal: String,
    private var usernameVal: String,
    private var passwordVal: String?,
    private var hostVal: String?,
    private var portVal: Int?,
    private var pathVal: String,
    private var queryVal: String?,
    private var fragmentVal: String?,
) : IntoUrl {
    public companion object {
        public fun percentEncode(
            input: String,
            keep: String = "",
        ): String {
            val sb = StringBuilder()
            for (ch in input) {
                if (ch in 'a'..'z' || ch in 'A'..'Z' || ch in '0'..'9' || ch == '-' || ch == '_' || ch == '.' || ch == '~' || ch in keep) {
                    sb.append(ch)
                } else {
                    val bytes = ch.toString().encodeToByteArray()
                    for (b in bytes) {
                        val u = b.toInt() and 0xFF
                        sb.append('%').append(u.toString(16).uppercase().padStart(2, '0'))
                    }
                }
            }
            return sb.toString()
        }

        public fun parse(input: String): Result<Url> {
            val trimmed = input.trim()
            val colonIndex = trimmed.indexOf(':')
            if (colonIndex == -1) {
                return Result.failure(IllegalArgumentException("Invalid URL: missing scheme in '$input'"))
            }

            val scheme = trimmed.substring(0, colonIndex).lowercase()
            if (scheme.isEmpty() || !scheme[0].isLetter() || !scheme.all { it.isLetterOrDigit() || it == '+' || it == '-' || it == '.' }) {
                return Result.failure(IllegalArgumentException("Invalid URL scheme: '$scheme' in '$input'"))
            }

            val afterColon = trimmed.substring(colonIndex + 1)
            if (!afterColon.startsWith("//")) {
                // Scheme-only without authority, e.g. blob:, mailto:, file: without authority, etc.
                return Result.success(
                    Url(
                        schemeVal = scheme,
                        usernameVal = "",
                        passwordVal = null,
                        hostVal = null,
                        portVal = null,
                        pathVal = afterColon,
                        queryVal = null,
                        fragmentVal = null,
                    ),
                )
            }

            var rest = afterColon.substring(2)

            var fragment: String? = null
            val fragIndex = rest.indexOf('#')
            if (fragIndex != -1) {
                fragment = rest.substring(fragIndex + 1)
                rest = rest.substring(0, fragIndex)
            }

            var query: String? = null
            val qIndex = rest.indexOf('?')
            if (qIndex != -1) {
                query = rest.substring(qIndex + 1)
                rest = rest.substring(0, qIndex)
            }

            val pathIndex = rest.indexOf('/')
            val hostPart = if (pathIndex != -1) rest.substring(0, pathIndex) else rest
            val path = if (pathIndex != -1) rest.substring(pathIndex) else "/"

            var username = ""
            var password: String? = null
            var hostAndPort = hostPart

            val atIndex = hostPart.indexOf('@')
            if (atIndex != -1) {
                val userinfo = hostPart.substring(0, atIndex)
                hostAndPort = hostPart.substring(atIndex + 1)
                val userColon = userinfo.indexOf(':')
                if (userColon != -1) {
                    username = userinfo.substring(0, userColon)
                    password = userinfo.substring(userColon + 1)
                } else {
                    username = userinfo
                }
            }

            var host: String? = null
            var port: Int? = null

            if (hostAndPort.isNotEmpty()) {
                if (hostAndPort.startsWith("[")) {
                    val closeBracket = hostAndPort.indexOf(']')
                    if (closeBracket != -1) {
                        host = hostAndPort.substring(0, closeBracket + 1)
                        val portPart = hostAndPort.substring(closeBracket + 1)
                        if (portPart.startsWith(":")) {
                            port = portPart.substring(1).toIntOrNull()
                        }
                    } else {
                        host = hostAndPort
                    }
                } else {
                    val portColon = hostAndPort.lastIndexOf(':')
                    if (portColon != -1) {
                        host = hostAndPort.substring(0, portColon)
                        port = hostAndPort.substring(portColon + 1).toIntOrNull()
                    } else {
                        host = hostAndPort
                    }
                }
            }

            return Result.success(
                Url(
                    schemeVal = scheme,
                    usernameVal = username,
                    passwordVal = password,
                    hostVal = host?.ifEmpty { null },
                    portVal = port,
                    pathVal = if (path.isEmpty()) "/" else path,
                    queryVal = query,
                    fragmentVal = fragment,
                ),
            )
        }
    }

    public fun scheme(): String = schemeVal

    public fun hasHost(): Boolean = hostVal != null && hostVal!!.isNotEmpty()

    public fun hostStr(): String? = hostVal

    public fun port(): Int? = portVal

    public fun portOrKnownDefault(): Int? {
        if (portVal != null) return portVal
        return when (schemeVal) {
            "http", "ws" -> 80
            "https", "wss" -> 443
            "ftp" -> 21
            "socks5", "socks5h", "socks4", "socks4a" -> 1080
            else -> null
        }
    }

    public fun path(): String = pathVal

    public fun query(): String? = queryVal

    public fun fragment(): String? = fragmentVal

    public fun username(): String = usernameVal

    public fun password(): String? = passwordVal

    public fun setUsername(username: String): Boolean {
        usernameVal = username
        return true
    }

    public fun setPassword(password: String?): Boolean {
        passwordVal = password
        return true
    }

    public fun setFragment(fragment: String?): Boolean {
        fragmentVal = fragment
        return true
    }

    public fun copy(): Url =
        Url(
            schemeVal = schemeVal,
            usernameVal = usernameVal,
            passwordVal = passwordVal,
            hostVal = hostVal,
            portVal = portVal,
            pathVal = pathVal,
            queryVal = queryVal,
            fragmentVal = fragmentVal,
        )

    public fun clone(): Url = copy()

    public fun setQuery(query: String?) {
        this.queryVal = query
    }

    public fun setPort(port: Int?): Boolean {
        portVal = port
        return true
    }

    override fun intoUrl(): Result<Url> {
        return if (hasHost()) {
            Result.success(this)
        } else {
            Result.failure(Error.urlBadScheme(this))
        }
    }

    override fun asStr(): String = toString()

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(schemeVal)
        if (hostVal != null || (schemeVal == "http" || schemeVal == "https" || schemeVal == "ftp" || schemeVal.startsWith("socks") || schemeVal.startsWith("ws"))) {
            sb.append("://")
            if (usernameVal.isNotEmpty() || passwordVal != null) {
                sb.append(usernameVal)
                if (passwordVal != null) {
                    sb.append(":").append(passwordVal)
                }
                sb.append("@")
            }
            if (hostVal != null) {
                sb.append(hostVal)
            }
            if (portVal != null) {
                sb.append(":").append(portVal)
            }
        } else {
            sb.append(":")
        }
        sb.append(pathVal)
        if (queryVal != null) {
            sb.append("?").append(queryVal)
        }
        if (fragmentVal != null) {
            sb.append("#").append(fragmentVal)
        }
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Url) return false
        return this.toString() == other.toString()
    }

    override fun hashCode(): Int = toString().hashCode()
}
