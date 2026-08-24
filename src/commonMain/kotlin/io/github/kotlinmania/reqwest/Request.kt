// port-lint: source async_impl/request.rs
package io.github.kotlinmania.reqwest

import kotlin.time.Duration

/**
 * A request parameter for query or form encoding.
 */
public class QueryParam(
    public val name: String,
    public val value: String,
) {
    public operator fun component1(): String = name
    public operator fun component2(): String = value
}

/**
 * A request which can be executed with [Client.execute].
 */
public class Request(
    private var methodVal: Method,
    private var urlVal: Url,
    private var headersVal: HeaderMap = HeaderMap(),
    private var bodyVal: Body? = null,
    private var versionVal: Version = Version.HTTP_11,
    private var timeoutVal: Duration? = null,
) {
    public fun method(): Method = methodVal

    public fun url(): Url = urlVal

    public fun headers(): HeaderMap = headersVal

    public fun body(): Body? = bodyVal

    public fun version(): Version = versionVal

    public fun timeout(): Duration? = timeoutVal

    public fun setMethod(method: Method) {
        this.methodVal = method
    }

    public fun setUrl(url: Url) {
        this.urlVal = url
    }

    public fun setHeaders(headers: HeaderMap) {
        this.headersVal = headers
    }

    public fun setBody(body: Body?) {
        this.bodyVal = body
    }

    public fun setVersion(version: Version) {
        this.versionVal = version
    }

    public fun setTimeout(timeout: Duration?) {
        this.timeoutVal = timeout
    }

    public companion object {
        public fun new(
            method: Method,
            url: Url,
        ): Request =
            Request(
                methodVal = method,
                urlVal = url,
                headersVal = HeaderMap(),
                bodyVal = null,
                versionVal = Version.HTTP_11,
                timeoutVal = null,
            )
    }
}

/**
 * A builder to construct the properties of a [Request].
 */
public class RequestBuilder(
    private val client: Client,
    private val request: Request,
) {
    public fun header(
        key: HeaderName,
        value: HeaderValue,
    ): RequestBuilder {
        request.headers().append(key, value)
        return this
    }

    public fun header(
        key: String,
        value: String,
    ): RequestBuilder = header(HeaderName.fromString(key), HeaderValue.fromStr(value))

    public fun headers(headers: HeaderMap): RequestBuilder {
        for ((k, v) in headers.entries()) {
            request.headers().append(k, v)
        }
        return this
    }

    public fun basicAuth(
        username: String,
        password: String? = null,
    ): RequestBuilder {
        val authHeader = Util.basicAuth(username, password)
        return header(HeaderName.AUTHORIZATION, authHeader)
    }

    public fun bearerAuth(token: String): RequestBuilder {
        val header = HeaderValue.fromStr("Bearer $token")
        header.setSensitive(true)
        return header(HeaderName.AUTHORIZATION, header)
    }

    public fun body(body: Body): RequestBuilder {
        request.setBody(body)
        val len = body.contentLength()
        if (len != null) {
            request.headers().insert(HeaderName.CONTENT_LENGTH, HeaderValue.fromStr(len.toString()))
        }
        return this
    }

    public fun body(bytes: ByteArray): RequestBuilder = body(Body.from(bytes))

    public fun body(text: String): RequestBuilder = body(Body.from(text))

    public fun json(jsonString: String): RequestBuilder {
        header(HeaderName.CONTENT_TYPE, HeaderValue.fromStr("application/json"))
        return body(Body.from(jsonString))
    }

    public fun query(params: List<QueryParam>): RequestBuilder {
        val queryStr =
            params.joinToString("&") { (k, v) ->
                "${Url.percentEncode(k, "")}=${Url.percentEncode(v, "")}"
            }
        val existingQuery = request.url().query()
        val newQuery = if (existingQuery != null) "$existingQuery&$queryStr" else queryStr
        request.url().setQuery(newQuery)
        return this
    }

    public fun queryParam(name: String, value: String): RequestBuilder = query(listOf(QueryParam(name, value)))

    public fun form(params: List<QueryParam>): RequestBuilder {
        header(HeaderName.CONTENT_TYPE, HeaderValue.fromStr("application/x-www-form-urlencoded"))
        val formStr =
            params.joinToString("&") { (k, v) ->
                "${Url.percentEncode(k, "")}=${Url.percentEncode(v, "")}"
            }
        return body(Body.from(formStr))
    }

    public fun formParam(name: String, value: String): RequestBuilder = form(listOf(QueryParam(name, value)))

    public fun timeout(timeout: Duration): RequestBuilder {
        request.setTimeout(timeout)
        return this
    }

    public fun version(version: Version): RequestBuilder {
        request.setVersion(version)
        return this
    }

    public fun build(): Request = request

    public suspend fun send(): Response = client.execute(request)
}
