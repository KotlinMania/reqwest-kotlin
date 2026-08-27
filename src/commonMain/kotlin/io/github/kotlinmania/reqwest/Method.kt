// port-lint: source reqwest/src/lib.rs
package io.github.kotlinmania.reqwest

/**
 * An HTTP method.
 */
public class Method(
    public val name: String,
) {
    public fun asStr(): String = name

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Method) return false
        return name.equals(other.name, ignoreCase = true)
    }

    override fun hashCode(): Int = name.uppercase().hashCode()

    public companion object {
        public val GET: Method = Method("GET")
        public val POST: Method = Method("POST")
        public val PUT: Method = Method("PUT")
        public val DELETE: Method = Method("DELETE")
        public val HEAD: Method = Method("HEAD")
        public val OPTIONS: Method = Method("OPTIONS")
        public val CONNECT: Method = Method("CONNECT")
        public val PATCH: Method = Method("PATCH")
        public val TRACE: Method = Method("TRACE")

        public fun fromString(name: String): Method = Method(name.uppercase())
    }
}
