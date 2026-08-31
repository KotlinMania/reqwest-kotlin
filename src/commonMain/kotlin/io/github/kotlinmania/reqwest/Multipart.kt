// port-lint: source reqwest/src/async_impl/multipart.rs
package io.github.kotlinmania.reqwest

/**
 * A multipart/form-data request body.
 */
public class Form(
    private val boundary: String = "----KotlinManiaReqwestBoundary" + Util.fastRandom(),
) {
    private val parts = mutableListOf<Pair<String, Part>>()

    public fun boundary(): String = boundary

    public fun text(
        name: String,
        value: String,
    ): Form {
        parts.add(name to Part.text(value))
        return this
    }

    public fun part(
        name: String,
        part: Part,
    ): Form {
        parts.add(name to part)
        return this
    }

    public fun toBody(): Body {
        val sb = StringBuilder()
        for ((name, part) in parts) {
            sb.append("--").append(boundary).append("\r\n")
            sb.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
            if (part.fileName() != null) {
                sb.append("; filename=\"").append(part.fileName()).append("\"")
            }
            sb.append("\r\n")
            if (part.mime() != null) {
                sb.append("Content-Type: ").append(part.mime()).append("\r\n")
            }
            sb.append("\r\n")
            sb.append(part.text())
            sb.append("\r\n")
        }
        sb.append("--").append(boundary).append("--\r\n")
        return Body.from(sb.toString())
    }

    public companion object {
        public fun new(): Form = Form()
    }
}

/**
 * A field in a multipart form.
 */
public class Part internal constructor(
    private val body: Body,
    private var mimeVal: String? = null,
    private var fileNameVal: String? = null,
    private var headersVal: HeaderMap = HeaderMap(),
) {
    public fun mime(): String? = mimeVal

    public fun fileName(): String? = fileNameVal

    public fun text(): String = body.text()

    public fun bytes(): ByteArray = body.asBytes() ?: ByteArray(0)

    public fun mimeStr(mime: String): Part {
        this.mimeVal = mime
        return this
    }

    public fun fileName(name: String): Part {
        this.fileNameVal = name
        return this
    }

    public fun headers(headers: HeaderMap): Part {
        this.headersVal = headers
        return this
    }

    public companion object {
        public fun text(value: String): Part = Part(Body.from(value)).mimeStr("text/plain")

        public fun bytes(bytes: ByteArray): Part = Part(Body.from(bytes)).mimeStr("application/octet-stream")
    }
}
