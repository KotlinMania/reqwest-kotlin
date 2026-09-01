// port-lint: source cookie.rs
package io.github.kotlinmania.reqwest

/**
 * An HTTP Cookie.
 */
public class Cookie(
    private val name: String,
    private val value: String,
    private var domain: String? = null,
    private var path: String? = null,
    private var secure: Boolean = false,
    private var httpOnly: Boolean = false,
    private var sameSiteLax: Boolean = false,
    private var sameSiteStrict: Boolean = false,
    private var maxAge: Long? = null,
) {
    public fun name(): String = name

    public fun value(): String = value

    public fun domain(): String? = domain

    public fun path(): String? = path

    public fun secure(): Boolean = secure

    public fun httpOnly(): Boolean = httpOnly

    public fun sameSiteLax(): Boolean = sameSiteLax

    public fun sameSiteStrict(): Boolean = sameSiteStrict

    public fun maxAge(): Long? = maxAge

    public fun setDomain(domain: String) {
        this.domain = domain
    }

    public fun setPath(path: String) {
        this.path = path
    }

    public fun setSecure(secure: Boolean) {
        this.secure = secure
    }

    public fun setHttpOnly(httpOnly: Boolean) {
        this.httpOnly = httpOnly
    }

    public companion object {
        public fun parse(raw: String): Cookie? = parse(HeaderValue.fromStr(raw)).getOrNull()

        public fun parse(header: HeaderValue): Result<Cookie> {
            val s = header.asStr()
            val parts = s.split(';')
            if (parts.isEmpty()) return Result.failure(IllegalArgumentException("Empty cookie header"))
            val first = parts[0].trim()
            val eqIdx = first.indexOf('=')
            if (eqIdx == -1) return Result.failure(IllegalArgumentException("Invalid cookie name/value pair: $first"))

            val name = first.substring(0, eqIdx).trim()
            val value = first.substring(eqIdx + 1).trim()
            val cookie = Cookie(name, value)

            for (i in 1 until parts.size) {
                val part = parts[i].trim()
                if (part.isEmpty()) continue
                val pEq = part.indexOf('=')
                val attrName = if (pEq != -1) part.substring(0, pEq).trim() else part
                val attrVal = if (pEq != -1) part.substring(pEq + 1).trim() else ""

                when (attrName.lowercase()) {
                    "domain" -> cookie.domain = attrVal
                    "path" -> cookie.path = attrVal
                    "secure" -> cookie.secure = true
                    "httponly" -> cookie.httpOnly = true
                    "samesite" -> {
                        if (attrVal.equals("lax", ignoreCase = true)) cookie.sameSiteLax = true
                        if (attrVal.equals("strict", ignoreCase = true)) cookie.sameSiteStrict = true
                    }
                    "max-age" -> cookie.maxAge = attrVal.toLongOrNull()
                }
            }
            return Result.success(cookie)
        }
    }
}

/**
 * A persistent or in-memory store for cookies.
 */
public interface CookieStore {
    public fun setCookies(
        cookies: List<HeaderValue>,
        url: Url,
    )

    public fun cookies(url: Url): HeaderValue?
}

/**
 * In-memory implementation of [CookieStore].
 */
public class Jar : CookieStore {
    private val store = mutableListOf<Cookie>()

    public fun addCookieStr(cookieStr: String, url: Url) {
        setCookies(listOf(HeaderValue.fromStr(cookieStr)), url)
    }

    override fun setCookies(
        cookies: List<HeaderValue>,
        url: Url,
    ) {
        for (h in cookies) {
            val parsed = Cookie.parse(h)
            if (parsed.isSuccess) {
                val c = parsed.getOrThrow()
                store.removeAll { it.name() == c.name() && it.domain() == (c.domain() ?: url.hostStr()) }
                store.add(c)
            }
        }
    }

    override fun cookies(url: Url): HeaderValue? {
        val matched = mutableListOf<String>()
        val host = url.hostStr() ?: ""
        for (c in store) {
            val cDomain = c.domain()
            val domainMatch = cDomain == null || host == cDomain || host.endsWith(".$cDomain")
            val secureMatch = !c.secure() || url.scheme() == "https"
            if (domainMatch && secureMatch) {
                matched.add("${c.name()}=${c.value()}")
            }
        }
        if (matched.isEmpty()) return null
        return HeaderValue.fromStr(matched.joinToString("; "))
    }
}
