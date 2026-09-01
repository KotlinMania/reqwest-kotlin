// port-lint: source lib.rs
package io.github.kotlinmania.reqwest

/**
 * An HTTP status code (3-digit integer from 100 to 999).
 */
public class StatusCode(
    private val code: Int,
    private val canonicalReason: String = "",
) {
    init {
        require(code in 100..999) { "Status code must be between 100 and 999, got $code" }
    }

    public fun asU16(): Int = code

    public fun asStr(): String = code.toString()

    public fun canonicalReason(): String? = canonicalReason.ifEmpty { null }

    public fun isInformational(): Boolean = code in 100..199

    public fun isSuccess(): Boolean = code in 200..299

    public fun isRedirection(): Boolean = code in 300..399

    public fun isClientError(): Boolean = code in 400..499

    public fun isServerError(): Boolean = code in 500..599

    override fun toString(): String = code.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatusCode) return false
        return code == other.code
    }

    override fun hashCode(): Int = code.hashCode()

    public companion object {
        public val CONTINUE: StatusCode = StatusCode(100, "Continue")
        public val SWITCHING_PROTOCOLS: StatusCode = StatusCode(101, "Switching Protocols")
        public val PROCESSING: StatusCode = StatusCode(102, "Processing")

        public val OK: StatusCode = StatusCode(200, "OK")
        public val CREATED: StatusCode = StatusCode(201, "Created")
        public val ACCEPTED: StatusCode = StatusCode(202, "Accepted")
        public val NON_AUTHORITATIVE_INFORMATION: StatusCode = StatusCode(203, "Non-Authoritative Information")
        public val NO_CONTENT: StatusCode = StatusCode(204, "No Content")
        public val RESET_CONTENT: StatusCode = StatusCode(205, "Reset Content")
        public val PARTIAL_CONTENT: StatusCode = StatusCode(206, "Partial Content")
        public val MULTI_STATUS: StatusCode = StatusCode(207, "Multi-Status")
        public val ALREADY_REPORTED: StatusCode = StatusCode(208, "Already Reported")
        public val IM_USED: StatusCode = StatusCode(226, "IM Used")

        public val MULTIPLE_CHOICES: StatusCode = StatusCode(300, "Multiple Choices")
        public val MOVED_PERMANENTLY: StatusCode = StatusCode(301, "Moved Permanently")
        public val FOUND: StatusCode = StatusCode(302, "Found")
        public val SEE_OTHER: StatusCode = StatusCode(303, "See Other")
        public val NOT_MODIFIED: StatusCode = StatusCode(304, "Not Modified")
        public val USE_PROXY: StatusCode = StatusCode(305, "Use Proxy")
        public val TEMPORARY_REDIRECT: StatusCode = StatusCode(307, "Temporary Redirect")
        public val PERMANENT_REDIRECT: StatusCode = StatusCode(308, "Permanent Redirect")

        public val BAD_REQUEST: StatusCode = StatusCode(400, "Bad Request")
        public val UNAUTHORIZED: StatusCode = StatusCode(401, "Unauthorized")
        public val PAYMENT_REQUIRED: StatusCode = StatusCode(402, "Payment Required")
        public val FORBIDDEN: StatusCode = StatusCode(403, "Forbidden")
        public val NOT_FOUND: StatusCode = StatusCode(404, "Not Found")
        public val METHOD_NOT_ALLOWED: StatusCode = StatusCode(405, "Method Not Allowed")
        public val NOT_ACCEPTABLE: StatusCode = StatusCode(406, "Not Acceptable")
        public val PROXY_AUTHENTICATION_REQUIRED: StatusCode = StatusCode(407, "Proxy Authentication Required")
        public val REQUEST_TIMEOUT: StatusCode = StatusCode(408, "Request Timeout")
        public val CONFLICT: StatusCode = StatusCode(409, "Conflict")
        public val GONE: StatusCode = StatusCode(410, "Gone")
        public val LENGTH_REQUIRED: StatusCode = StatusCode(411, "Length Required")
        public val PRECONDITION_FAILED: StatusCode = StatusCode(412, "Precondition Failed")
        public val PAYLOAD_TOO_LARGE: StatusCode = StatusCode(413, "Payload Too Large")
        public val URI_TOO_LONG: StatusCode = StatusCode(414, "URI Too Long")
        public val UNSUPPORTED_MEDIA_TYPE: StatusCode = StatusCode(415, "Unsupported Media Type")
        public val RANGE_NOT_SATISFIABLE: StatusCode = StatusCode(416, "Range Not Satisfiable")
        public val EXPECTATION_FAILED: StatusCode = StatusCode(417, "Expectation Failed")
        public val IM_A_TEAPOT: StatusCode = StatusCode(418, "I'm a teapot")
        public val MISDIRECTED_REQUEST: StatusCode = StatusCode(421, "Misdirected Request")
        public val UNPROCESSABLE_ENTITY: StatusCode = StatusCode(422, "Unprocessable Entity")
        public val LOCKED: StatusCode = StatusCode(423, "Locked")
        public val FAILED_DEPENDENCY: StatusCode = StatusCode(424, "Failed Dependency")
        public val TOO_EARLY: StatusCode = StatusCode(425, "Too Early")
        public val UPGRADE_REQUIRED: StatusCode = StatusCode(426, "Upgrade Required")
        public val PRECONDITION_REQUIRED: StatusCode = StatusCode(428, "Precondition Required")
        public val TOO_MANY_REQUESTS: StatusCode = StatusCode(429, "Too Many Requests")
        public val REQUEST_HEADER_FIELDS_TOO_LARGE: StatusCode = StatusCode(431, "Request Header Fields Too Large")
        public val UNAVAILABLE_FOR_LEGAL_REASONS: StatusCode = StatusCode(451, "Unavailable For Legal Reasons")

        public val INTERNAL_SERVER_ERROR: StatusCode = StatusCode(500, "Internal Server Error")
        public val NOT_IMPLEMENTED: StatusCode = StatusCode(501, "Not Implemented")
        public val BAD_GATEWAY: StatusCode = StatusCode(502, "Bad Gateway")
        public val SERVICE_UNAVAILABLE: StatusCode = StatusCode(503, "Service Unavailable")
        public val GATEWAY_TIMEOUT: StatusCode = StatusCode(504, "Gateway Timeout")
        public val HTTP_VERSION_NOT_SUPPORTED: StatusCode = StatusCode(505, "HTTP Version Not Supported")
        public val VARIANT_ALSO_NEGOTIATES: StatusCode = StatusCode(506, "Variant Also Negotiates")
        public val INSUFFICIENT_STORAGE: StatusCode = StatusCode(507, "Insufficient Storage")
        public val LOOP_DETECTED: StatusCode = StatusCode(508, "Loop Detected")
        public val NOT_EXTENDED: StatusCode = StatusCode(510, "Not Extended")
        public val NETWORK_AUTHENTICATION_REQUIRED: StatusCode = StatusCode(511, "Network Authentication Required")

        public fun fromU16(code: Int): Result<StatusCode> {
            if (code !in 100..999) {
                return Result.failure(IllegalArgumentException("Invalid status code: $code"))
            }
            val known =
                when (code) {
                    200 -> OK
                    201 -> CREATED
                    202 -> ACCEPTED
                    204 -> NO_CONTENT
                    301 -> MOVED_PERMANENTLY
                    302 -> FOUND
                    303 -> SEE_OTHER
                    304 -> NOT_MODIFIED
                    307 -> TEMPORARY_REDIRECT
                    308 -> PERMANENT_REDIRECT
                    400 -> BAD_REQUEST
                    401 -> UNAUTHORIZED
                    403 -> FORBIDDEN
                    404 -> NOT_FOUND
                    405 -> METHOD_NOT_ALLOWED
                    408 -> REQUEST_TIMEOUT
                    429 -> TOO_MANY_REQUESTS
                    500 -> INTERNAL_SERVER_ERROR
                    502 -> BAD_GATEWAY
                    503 -> SERVICE_UNAVAILABLE
                    504 -> GATEWAY_TIMEOUT
                    else -> StatusCode(code)
                }
            return Result.success(known)
        }
    }
}
