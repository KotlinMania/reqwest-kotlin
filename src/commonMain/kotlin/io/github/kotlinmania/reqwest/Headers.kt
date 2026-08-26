// port-lint: source headers.rs
package io.github.kotlinmania.reqwest

/**
 * An HTTP Header Name.
 */
public class HeaderName(
    public val name: String,
) {
    public val lower: String = name.lowercase()

    public fun asStr(): String = name

    public fun toStr(): String = name

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HeaderName) return false
        return lower == other.lower
    }

    override fun hashCode(): Int = lower.hashCode()

    public companion object {
        public val ACCEPT: HeaderName = HeaderName("accept")
        public val ACCEPT_ENCODING: HeaderName = HeaderName("accept-encoding")
        public val AUTHORIZATION: HeaderName = HeaderName("authorization")
        public val CONTENT_TYPE: HeaderName = HeaderName("content-type")
        public val CONTENT_LENGTH: HeaderName = HeaderName("content-length")
        public val COOKIE: HeaderName = HeaderName("cookie")
        public val COOKIE2: HeaderName = HeaderName("cookie2")
        public val HOST: HeaderName = HeaderName("host")
        public val LOCATION: HeaderName = HeaderName("location")
        public val PROXY_AUTHORIZATION: HeaderName = HeaderName("proxy-authorization")
        public val REFERER: HeaderName = HeaderName("referer")
        public val SET_COOKIE: HeaderName = HeaderName("set-cookie")
        public val USER_AGENT: HeaderName = HeaderName("user-agent")
        public val WWW_AUTHENTICATE: HeaderName = HeaderName("www-authenticate")

        public fun fromStatic(name: String): HeaderName = HeaderName(name)

        public fun fromStr(name: String): HeaderName = HeaderName(name)

        public fun fromString(name: String): HeaderName = HeaderName(name)
    }
}

/**
 * An HTTP Header Value.
 */
public class HeaderValue(
    private val value: String,
    private var sensitive: Boolean = false,
) {
    public fun asStr(): String = value

    public fun toStr(): String = value

    public fun asBytes(): ByteArray = value.encodeToByteArray()

    public fun isSensitive(): Boolean = sensitive

    public fun setSensitive(sensitive: Boolean) {
        this.sensitive = sensitive
    }

    override fun toString(): String = if (sensitive) "[sensitive]" else value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HeaderValue) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    public companion object {
        public fun fromStatic(value: String): HeaderValue = HeaderValue(value)

        public fun fromStr(value: String): HeaderValue = HeaderValue(value)

        public fun fromString(value: String): HeaderValue = HeaderValue(value)
    }
}

/**
 * An entry pair within a [HeaderMap].
 */
public class HeaderEntry(
    public val name: HeaderName,
    public val value: HeaderValue,
) {
    public operator fun component1(): HeaderName = name

    public operator fun component2(): HeaderValue = value
}

/**
 * A Map of HTTP headers.
 */
public class HeaderMap {
    private val map = mutableMapOf<String, MutableList<HeaderValue>>()
    private val originalNames = mutableMapOf<String, HeaderName>()

    public fun insert(
        name: HeaderName,
        value: HeaderValue,
    ) {
        val list = mutableListOf(value)
        map[name.lower] = list
        originalNames[name.lower] = name
    }

    public fun insert(
        name: String,
        value: String,
    ) {
        insert(HeaderName(name), HeaderValue(value))
    }

    public fun append(
        name: HeaderName,
        value: HeaderValue,
    ) {
        val list = map.getOrPut(name.lower) { mutableListOf() }
        list.add(value)
        originalNames[name.lower] = name
    }

    public fun get(name: HeaderName): HeaderValue? = map[name.lower]?.firstOrNull()

    public fun get(name: String): HeaderValue? = map[name.lowercase()]?.firstOrNull()

    public fun getAll(name: HeaderName): List<HeaderValue> = map[name.lower]?.toList() ?: emptyList()

    public fun getAll(name: String): List<HeaderValue> = map[name.lowercase()]?.toList() ?: emptyList()

    public fun remove(name: HeaderName): List<HeaderValue>? {
        originalNames.remove(name.lower)
        return map.remove(name.lower)
    }

    public fun remove(name: String): List<HeaderValue>? {
        originalNames.remove(name.lowercase())
        return map.remove(name.lowercase())
    }

    public fun containsKey(name: HeaderName): Boolean = map.containsKey(name.lower)

    public fun containsKey(name: String): Boolean = map.containsKey(name.lowercase())

    public fun entries(): List<HeaderEntry> {
        val result = mutableListOf<HeaderEntry>()
        for ((key, values) in map) {
            val original = originalNames[key] ?: HeaderName(key)
            for (v in values) {
                result.add(HeaderEntry(original, v))
            }
        }
        return result
    }

    public fun isEmpty(): Boolean = map.isEmpty()

    public fun size(): Int = map.values.sumOf { it.size }

    public fun clone(): HeaderMap = copy()

    public fun copy(): HeaderMap {
        val copy = HeaderMap()
        for ((key, values) in map) {
            val original = originalNames[key] ?: HeaderName(key)
            copy.map[key] = values.toMutableList()
            copy.originalNames[key] = original
        }
        return copy
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HeaderMap) return false
        return map == other.map
    }

    override fun hashCode(): Int = map.hashCode()

    override fun toString(): String = map.toString()
}
