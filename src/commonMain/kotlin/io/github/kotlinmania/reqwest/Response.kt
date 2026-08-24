// port-lint: source async_impl/response.rs
package io.github.kotlinmania.reqwest

/**
 * A response to a submitted [Request].
 */
public class Response(
    public val status: StatusCode,
    public val version: Version = Version.HTTP_11,
    public val headers: HeaderMap = HeaderMap(),
    public val url: Url,
    public val body: Body = Body.empty(),
) {
    public fun status(): StatusCode = status

    public fun version(): Version = version

    public fun headers(): HeaderMap = headers

    public fun url(): Url = url

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
        public fun builder(): Builder = Builder()

        public fun create(
            status: StatusCode,
            url: Url,
            headers: HeaderMap = HeaderMap(),
            body: Body = Body.empty(),
            version: Version = Version.HTTP_11,
        ): Response = Response(status, version, headers, url, body)
    }

    public class Builder {
        private var statusVal: StatusCode = StatusCode.OK
        private var versionVal: Version = Version.HTTP_11
        private var headersVal: HeaderMap = HeaderMap()
        private var urlVal: Url? = null
        private var bodyVal: Body = Body.empty()

        public fun status(status: StatusCode): Builder {
            this.statusVal = status
            return this
        }

        public fun version(version: Version): Builder {
            this.versionVal = version
            return this
        }

        public fun header(
            name: HeaderName,
            value: HeaderValue,
        ): Builder {
            headersVal.append(name, value)
            return this
        }

        public fun headers(headers: HeaderMap): Builder {
            for ((k, v) in headers.entries()) {
                headersVal.append(k, v)
            }
            return this
        }

        public fun url(url: Url): Builder {
            this.urlVal = url
            return this
        }

        public fun body(body: Body): Builder {
            this.bodyVal = body
            return this
        }

        public fun build(): Response {
            val u = urlVal ?: Url.parse("http://localhost/").getOrThrow()
            return Response(statusVal, versionVal, headersVal, u, bodyVal)
        }
    }
}
