package io.github.kotlinmania.reqwest

/**
 * An HTTP version.
 */
public class Version(public val value: String) {
    public fun asStr(): String = value

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Version) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    public companion object {
        public val HTTP_09: Version = Version("HTTP/0.9")
        public val HTTP_10: Version = Version("HTTP/1.0")
        public val HTTP_11: Version = Version("HTTP/1.1")
        public val HTTP_2: Version = Version("HTTP/2.0")
        public val HTTP_3: Version = Version("HTTP/3.0")
    }
}
