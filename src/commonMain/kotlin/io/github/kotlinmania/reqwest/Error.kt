// port-lint: source error.rs
package io.github.kotlinmania.reqwest

/**
 * A Result alias where the Err case is reqwest.Error.
 */
public typealias ReqwestResult<T> = kotlin.Result<T>

/**
 * The Errors that may occur when processing a Request.
 *
 * Note: Errors may include the full URL used to make the Request. If the URL
 * contains sensitive information (e.g. an API key as a query parameter), be
 * sure to remove it ([withoutUrl]).
 */
public class Error internal constructor(
    internal val inner: Inner,
) : Exception(inner.toDisplayMessage(), inner.source) {
    internal class Inner(
        val kind: Kind,
        val source: Throwable?,
        var url: Url?,
    ) {
        fun toDisplayMessage(): String {
            val sb = StringBuilder()
            when (kind) {
                Kind.Builder -> sb.append("builder error")
                Kind.Request -> sb.append("error sending request")
                Kind.Body -> sb.append("request or response body error")
                Kind.Decode -> sb.append("error decoding response body")
                Kind.Redirect -> sb.append("error following redirect")
                Kind.Upgrade -> sb.append("error upgrading connection")
                is Kind.Status -> {
                    val prefix =
                        if (kind.code.isClientError()) {
                            "HTTP status client error"
                        } else {
                            "HTTP status server error"
                        }
                    if (kind.reason != null) {
                        sb.append("$prefix (${kind.code.asStr()} ${kind.reason})")
                    } else {
                        sb.append("$prefix (${kind.code.asStr()})")
                    }
                }
            }
            if (url != null) {
                sb.append(" for url ($url)")
            }
            return sb.toString()
        }
    }

    public companion object {
        internal fun create(
            kind: Kind,
            source: Throwable?,
            url: Url? = null,
        ): Error = Error(Inner(kind, source, url))

        public fun builder(e: Throwable): Error = create(Kind.Builder, e)

        public fun builder(message: String): Error = create(Kind.Builder, IllegalArgumentException(message))

        public fun body(e: Throwable): Error = create(Kind.Body, e)

        public fun decode(e: Throwable): Error = create(Kind.Decode, e)

        public fun request(e: Throwable): Error = create(Kind.Request, e)

        public fun request(message: String): Error = create(Kind.Request, RuntimeException(message))

        public fun redirect(
            e: Throwable,
            url: Url,
        ): Error = create(Kind.Redirect, e, url)

        public fun statusCode(
            url: Url,
            status: StatusCode,
            reason: String? = null,
        ): Error = create(Kind.Status(status, reason), null, url)

        public fun urlBadScheme(url: Url): Error = create(Kind.Builder, BadScheme(), url)

        public fun urlInvalidUri(url: Url): Error = create(Kind.Builder, IllegalArgumentException("Parsed Url is not a valid Uri"), url)

        public fun upgrade(e: Throwable): Error = create(Kind.Upgrade, e)

        public fun decodeIo(e: Throwable): Error {
            if (e is Error) {
                return e
            }
            val cause = e.cause
            if (cause is Error) {
                return cause
            }
            return decode(e)
        }
    }

    /**
     * Returns a possible URL related to this error.
     */
    public fun url(): Url? = inner.url

    /**
     * Add a url related to this error (overwriting any existing)
     */
    public fun withUrl(url: Url): Error {
        inner.url = url
        return this
    }

    internal fun ifNoUrl(f: () -> Url): Error {
        if (inner.url == null) {
            inner.url = f()
        }
        return this
    }

    /**
     * Strip the related url from this error (if, for example, it contains sensitive information)
     */
    public fun withoutUrl(): Error {
        inner.url = null
        return this
    }

    /**
     * Returns true if the error is from a type Builder.
     */
    public fun isBuilder(): Boolean = inner.kind is Kind.Builder

    /**
     * Returns true if the error is from a RedirectPolicy.
     */
    public fun isRedirect(): Boolean = inner.kind is Kind.Redirect

    /**
     * Returns true if the error is from Response.errorForStatus.
     */
    public fun isStatus(): Boolean = inner.kind is Kind.Status

    /**
     * Returns true if the error is related to a timeout.
     */
    public fun isTimeout(): Boolean {
        var curr: Throwable? = this
        while (curr != null) {
            if (curr is TimedOut) return true
            val msg = curr.message?.lowercase() ?: ""
            if (msg.contains("timeout") || msg.contains("timed out") || msg.contains("deadline")) {
                return true
            }
            curr = curr.cause
        }
        return false
    }

    /**
     * Returns true if the error is related to the request.
     */
    public fun isRequest(): Boolean = inner.kind is Kind.Request

    /**
     * Returns true if the error is related to connect.
     */
    public fun isConnect(): Boolean {
        var curr: Throwable? = this
        while (curr != null) {
            val msg = curr.message?.lowercase() ?: ""
            if (msg.contains("connection refused") || msg.contains("connect error") || msg.contains("failed to connect")) {
                return true
            }
            curr = curr.cause
        }
        return false
    }

    /**
     * Returns true if the error is related to the request or response body.
     */
    public fun isBody(): Boolean = inner.kind is Kind.Body

    /**
     * Returns true if the error is related to decoding the response's body.
     */
    public fun isDecode(): Boolean = inner.kind is Kind.Decode

    /**
     * Returns the status code, if the error was generated from a response.
     */
    public fun status(): StatusCode? =
        when (val k = inner.kind) {
            is Kind.Status -> k.code
            else -> null
        }

    /**
     * Returns true if the error is related to a protocol upgrade request.
     */
    public fun isUpgrade(): Boolean = inner.kind is Kind.Upgrade

    public fun source(): Throwable? = inner.source
}

public sealed class Kind {
    public data object Builder : Kind()

    public data object Request : Kind()

    public data object Redirect : Kind()

    public data class Status(
        val code: StatusCode,
        val reason: String? = null,
    ) : Kind()

    public data object Body : Kind()

    public data object Decode : Kind()

    public data object Upgrade : Kind()
}

public class TimedOut : Exception("operation timed out")

public class BadScheme : Exception("URL scheme is not allowed")
