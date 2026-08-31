// port-lint: source reqwest/src/proxy.rs
package io.github.kotlinmania.reqwest

/**
 * Configuration of a proxy that a Client should pass requests to.
 */
public class Proxy internal constructor(
    private var intercept: Intercept,
    private var extra: Extra = Extra(),
    private var noProxy: NoProxy? = null,
) {
    public companion object {
        /**
         * Proxy all HTTP traffic to the passed URL.
         */
        public fun http(proxyScheme: IntoProxy): Result<Proxy> =
            proxyScheme.intoProxy().map { url ->
                Proxy(Intercept.Http(url))
            }

        public fun http(url: String): Result<Proxy> = http(url.asIntoProxy())

        /**
         * Proxy all HTTPS traffic to the passed URL.
         */
        public fun https(proxyScheme: IntoProxy): Result<Proxy> =
            proxyScheme.intoProxy().map { url ->
                Proxy(Intercept.Https(url))
            }

        public fun https(url: String): Result<Proxy> = https(url.asIntoProxy())

        /**
         * Proxy all traffic to the passed URL.
         */
        public fun all(proxyScheme: IntoProxy): Result<Proxy> =
            proxyScheme.intoProxy().map { url ->
                Proxy(Intercept.All(url))
            }

        public fun all(url: String): Result<Proxy> = all(url.asIntoProxy())

        /**
         * Provide a custom function to determine what traffic to proxy to where.
         */
        public fun custom(functor: (Url) -> IntoProxy?): Proxy =
            Proxy(
                Intercept.Custom(CustomMatcher(functor)),
            )
    }

    /**
     * Set the Proxy-Authorization header using Basic auth.
     */
    public fun basicAuth(
        username: String,
        password: String? = null,
    ): Proxy {
        when (val ic = intercept) {
            is Intercept.All -> {
                ic.url.setUsername(username)
                ic.url.setPassword(password)
            }
            is Intercept.Http -> {
                ic.url.setUsername(username)
                ic.url.setPassword(password)
            }
            is Intercept.Https -> {
                ic.url.setUsername(username)
                ic.url.setPassword(password)
            }
            is Intercept.Custom -> {
                extra.auth = Util.basicAuth(username, password)
            }
        }
        return this
    }

    /**
     * Set a NoProxy filter for this proxy.
     */
    public fun noProxy(noProxy: NoProxy): Proxy {
        this.noProxy = noProxy
        return this
    }

    public fun destination(targetUrl: Url): Url? = matches(targetUrl)

    public fun matches(targetUrl: Url): Url? {
        if (noProxy?.matches(targetUrl) == true) {
            return null
        }
        return when (val ic = intercept) {
            is Intercept.All -> ic.url
            is Intercept.Http -> if (targetUrl.scheme() == "http" || targetUrl.scheme() == "ws") ic.url else null
            is Intercept.Https -> if (targetUrl.scheme() == "https" || targetUrl.scheme() == "wss") ic.url else null
            is Intercept.Custom ->
                ic.matcher
                    .match(targetUrl)
                    ?.intoProxy()
                    ?.getOrNull()
        }
    }
}

public class NoProxy(
    private val inner: String = "",
) {
    public fun matches(url: Url): Boolean {
        if (inner.isEmpty()) return false
        val host = url.hostStr() ?: return false
        val domains = inner.split(',').map { it.trim() }
        for (d in domains) {
            if (d == "*") return true
            if (d.startsWith(".")) {
                if (host.endsWith(d) || host == d.substring(1)) return true
            } else if (host == d || host.endsWith(".$d")) {
                return true
            }
        }
        return false
    }

    public companion object {
        public fun fromCustom(rules: String): NoProxy = NoProxy(rules)

        public fun fromString(rules: String): NoProxy = NoProxy(rules)
    }
}

internal class Extra(
    var auth: HeaderValue? = null,
    var misc: HeaderMap? = null,
)

internal sealed class Intercept {
    class Http(
        val url: Url,
    ) : Intercept()

    class Https(
        val url: Url,
    ) : Intercept()

    class All(
        val url: Url,
    ) : Intercept()

    class Custom(
        val matcher: CustomMatcher,
    ) : Intercept()
}

internal class CustomMatcher(
    private val func: (Url) -> IntoProxy?,
) {
    fun match(url: Url): IntoProxy? = func(url)
}

public interface IntoProxy {
    public fun intoProxy(): Result<Url>
}

public class UrlIntoProxy(
    private val url: Url,
) : IntoProxy {
    override fun intoProxy(): Result<Url> {
        val target = url
        if (target.port() == null && (target.scheme().startsWith("socks"))) {
            target.setPort(1080)
        }
        return Result.success(target)
    }
}

public class StringIntoProxy(
    private val str: String,
) : IntoProxy {
    override fun intoProxy(): Result<Url> {
        val parsed = Url.parse(str)
        if (parsed.isSuccess) {
            return UrlIntoProxy(parsed.getOrThrow()).intoProxy()
        }
        // Try prepending http://
        val withHttp = Url.parse("http://$str")
        if (withHttp.isSuccess) {
            return UrlIntoProxy(withHttp.getOrThrow()).intoProxy()
        }
        return Result.failure(Error.builder(parsed.exceptionOrNull() ?: IllegalArgumentException("Invalid proxy URL: $str")))
    }
}

public fun Url.asIntoProxy(): IntoProxy = UrlIntoProxy(this)

public fun String.asIntoProxy(): IntoProxy = StringIntoProxy(this)
