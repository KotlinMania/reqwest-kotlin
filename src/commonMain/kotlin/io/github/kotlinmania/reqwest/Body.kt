// port-lint: source reqwest/src/async_impl/body.rs
package io.github.kotlinmania.reqwest

/**
 * An asynchronous request or response body.
 */
public class Body internal constructor(
    private val rawBytes: ByteArray?,
    private val streamProvider: (() -> ByteArray)? = null,
) {
    public fun asBytes(): ByteArray? = rawBytes?.copyOf()

    public fun text(): String = rawBytes?.decodeToString() ?: ""

    public fun tryClone(): Body? {
        if (rawBytes != null) {
            return Body(rawBytes.copyOf(), null)
        }
        return null
    }

    public fun contentLength(): Long? = rawBytes?.size?.toLong()

    public companion object {
        public fun empty(): Body = Body(ByteArray(0), null)

        public fun reusable(bytes: ByteArray): Body = Body(bytes.copyOf(), null)

        public fun fromString(str: String): Body = Body(str.encodeToByteArray(), null)

        public fun fromBytes(bytes: ByteArray): Body = Body(bytes.copyOf(), null)

        public fun from(text: String): Body = fromString(text)

        public fun from(bytes: ByteArray): Body = fromBytes(bytes)
    }
}
