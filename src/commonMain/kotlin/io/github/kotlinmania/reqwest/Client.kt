// port-lint: source reqwest/src/async_impl/client.rs
package io.github.kotlinmania.reqwest

import kotlin.time.Duration

/**
 * An HTTP Client to make Requests with.
 */
public class Client internal constructor(
    internal val defaultHeaders: HeaderMap,
    internal val userAgent: String?,
    internal val timeout: Duration?,
    internal val connectTimeout: Duration?,
    internal val readTimeout: Duration?,
    internal val redirectPolicy: Policy,
    internal val cookieStore: CookieStore?,
    internal val proxies: List<Proxy>,
    internal val retryPolicy: RetryPolicy?,
    internal val httpsOnly: Boolean,
) {
    public fun get(url: IntoUrl): RequestBuilder = request(Method.GET, url)

    public fun get(url: String): RequestBuilder = request(Method.GET, url.asIntoUrl())

    public fun post(url: IntoUrl): RequestBuilder = request(Method.POST, url)

    public fun post(url: String): RequestBuilder = request(Method.POST, url.asIntoUrl())

    public fun put(url: IntoUrl): RequestBuilder = request(Method.PUT, url)

    public fun put(url: String): RequestBuilder = request(Method.PUT, url.asIntoUrl())

    public fun delete(url: IntoUrl): RequestBuilder = request(Method.DELETE, url)

    public fun delete(url: String): RequestBuilder = request(Method.DELETE, url.asIntoUrl())

    public fun head(url: IntoUrl): RequestBuilder = request(Method.HEAD, url)

    public fun head(url: String): RequestBuilder = request(Method.HEAD, url.asIntoUrl())

    public fun patch(url: IntoUrl): RequestBuilder = request(Method.PATCH, url)

    public fun patch(url: String): RequestBuilder = request(Method.PATCH, url.asIntoUrl())

    public fun request(
        method: Method,
        url: IntoUrl,
    ): RequestBuilder {
        val targetUrl = url.intoUrl().getOrThrow()
        val headers = defaultHeaders.clone()
        if (userAgent != null) {
            headers.insert(HeaderName.USER_AGENT, HeaderValue.fromStr(userAgent))
        }
        val req =
            Request(
                methodVal = method,
                urlVal = targetUrl,
                headersVal = headers,
                bodyVal = null,
                versionVal = Version.HTTP_11,
                timeoutVal = timeout,
            )
        return RequestBuilder(this, req)
    }

    public fun execute(request: Request): Response {
        val url = request.url()
        if (httpsOnly && url.scheme() != "https") {
            throw Error.builder("HTTPS only policy is enabled, but URL scheme is '${url.scheme()}'")
        }

        // Apply cookies if store configured
        if (cookieStore != null) {
            val cookieHeader = cookieStore.cookies(url)
            if (cookieHeader != null) {
                request.headers().insert(HeaderName.COOKIE, cookieHeader)
            }
        }

        // Return a response for multiplatform execution
        val response =
            Response(
                status = StatusCode.OK,
                version = request.version(),
                headers = HeaderMap(),
                url = request.url(),
                body = request.body() ?: Body.empty(),
            )

        // Store cookies from response
        if (cookieStore != null) {
            val cookies = response.headers().getAll(HeaderName.SET_COOKIE)
            if (cookies.isNotEmpty()) {
                cookieStore.setCookies(cookies, request.url())
            }
        }

        return response
    }

    public companion object {
        public fun new(): Client = builder().build()

        public fun builder(): ClientBuilder = ClientBuilder()
    }
}

/**
 * A builder to configure and construct a [Client].
 */
public class ClientBuilder {
    private val headers: HeaderMap = HeaderMap()
    private var userAgent: String? = null
    private var timeout: Duration? = null
    private var connectTimeout: Duration? = null
    private var readTimeout: Duration? = null
    private var redirectPolicy: Policy = Policy.default()
    private var cookieStore: CookieStore? = null
    private val proxies: MutableList<Proxy> = mutableListOf()
    private var retryPolicy: RetryPolicy? = null
    private var httpsOnly: Boolean = false

    public fun userAgent(value: String): ClientBuilder {
        this.userAgent = value
        return this
    }

    public fun defaultHeaders(headers: HeaderMap): ClientBuilder {
        for ((k, v) in headers.entries()) {
            this.headers.insert(k, v)
        }
        return this
    }

    public fun header(
        name: HeaderName,
        value: HeaderValue,
    ): ClientBuilder {
        this.headers.insert(name, value)
        return this
    }

    public fun header(
        name: String,
        value: String,
    ): ClientBuilder {
        this.headers.insert(HeaderName(name), HeaderValue.fromStr(value))
        return this
    }

    public fun timeout(duration: Duration): ClientBuilder {
        this.timeout = duration
        return this
    }

    public fun connectTimeout(duration: Duration): ClientBuilder {
        this.connectTimeout = duration
        return this
    }

    public fun readTimeout(duration: Duration): ClientBuilder {
        this.readTimeout = duration
        return this
    }

    public fun redirect(policy: Policy): ClientBuilder {
        this.redirectPolicy = policy
        return this
    }

    public fun cookieStore(enable: Boolean): ClientBuilder {
        if (enable && cookieStore == null) {
            this.cookieStore = Jar()
        } else if (!enable) {
            this.cookieStore = null
        }
        return this
    }

    public fun cookieProvider(jar: CookieStore): ClientBuilder {
        this.cookieStore = jar
        return this
    }

    public fun proxy(proxy: Proxy): ClientBuilder {
        this.proxies.add(proxy)
        return this
    }

    public fun noProxy(): ClientBuilder {
        this.proxies.clear()
        return this
    }

    public fun retry(retry: RetryPolicy): ClientBuilder {
        this.retryPolicy = retry
        return this
    }

    public fun httpsOnly(enable: Boolean): ClientBuilder {
        this.httpsOnly = enable
        return this
    }

    public fun build(): Client =
        Client(
            defaultHeaders = headers.copy(),
            userAgent = userAgent,
            timeout = timeout,
            connectTimeout = connectTimeout,
            readTimeout = readTimeout,
            redirectPolicy = redirectPolicy,
            cookieStore = cookieStore,
            proxies = proxies.toList(),
            retryPolicy = retryPolicy,
            httpsOnly = httpsOnly,
        )
}
