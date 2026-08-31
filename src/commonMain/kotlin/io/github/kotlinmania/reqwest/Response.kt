// port-lint: source reqwest/src/async_impl/response.rs
package io.github.kotlinmania.reqwest

/**
 * A response to a submitted [Request].
 */
public class Response(
    private val status: StatusCode,
    private val version: Version = Version.HTTP_11,
    private val headers: HeaderMap = HeaderMap(),
    private val url: Url,
    private val body: Body = Body.empty(),
) {
    public fun status(): StatusCode = status

    public fun version(): Version = version

    public fun headers(): HeaderMap = headers

    public fun url(): Url = url

    public fun body(): Body = body

    public fun contentLength(): Long? =
        body.contentLength()
            ?: headers.get(HeaderName.CONTENT_LENGTH)?.asStr()?.toLongOrNull()

    public fun cookies(): List<Cookie> {
        val setCookieHeaders = headers.getAll(HeaderName.SET_COOKIE)
        val result = mutableListOf<Cookie>()
        for (h in setCookieHeaders) {
            val c = Cookie.parse(h)
            if (c.isSuccess) {
                result.add(c.getOrThrow())
            }
        }
        return result
    }

    public fun bytes(): ByteArray = body.asBytes() ?: ByteArray(0)

    public fun text(): String = body.text()

    /**
     * Turn a response into an [Error] if the server returned an error status (4xx or 5xx).
     */
    public fun errorForStatus(): Response {
        val err = errorForStatusRef()
        if (err != null) {
            throw err
        }
        return this
    }

    /**
     * Returns an [Error] if the status code represents an error (4xx or 5xx), or null otherwise.
     */
    public fun errorForStatusRef(): Error? {
        if (status.isClientError() || status.isServerError()) {
            return Error.statusCode(url, status, status.canonicalReason())
        }
        return null
    }

    public companion object {
        public fun builder(): ResponseBuilder = ResponseBuilder()

        public fun create(status: StatusCode, url: Url): Response =
            Response(status = status, url = url)
    }
}

public class ResponseBuilder {
    private var status: StatusCode = StatusCode.OK
    private var version: Version = Version.HTTP_11
    private var headers: HeaderMap = HeaderMap()
    private var url: Url? = null
    private var body: Body = Body.empty()

    public fun status(status: StatusCode): ResponseBuilder {
        this.status = status
        return this
    }

    public fun version(version: Version): ResponseBuilder {
        this.version = version
        return this
    }

    public fun headers(headers: HeaderMap): ResponseBuilder {
        this.headers = headers.clone()
        return this
    }

    public fun header(name: HeaderName, value: HeaderValue): ResponseBuilder {
        this.headers.append(name, value)
        return this
    }

    public fun url(url: Url): ResponseBuilder {
        this.url = url
        return this
    }

    public fun body(body: Body): ResponseBuilder {
        this.body = body
        return this
    }

    public fun build(): Response {
        val u = url ?: Url.parse("http://localhost/").getOrThrow()
        return Response(
            status = status,
            version = version,
            headers = headers,
            url = u,
            body = body,
        )
    }
}
