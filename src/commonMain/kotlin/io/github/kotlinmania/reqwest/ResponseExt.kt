// port-lint: source response.rs
package io.github.kotlinmania.reqwest

import kotlin.reflect.KClass

/**
 * An extension container holding type-keyed extensions, matching http::Extensions.
 */
public class Extensions {
    private val map: MutableMap<KClass<*>, Any>

    public constructor() {
        this.map = mutableMapOf()
    }

    private constructor(map: MutableMap<KClass<*>, Any>) {
        this.map = map
    }

    public inline fun <reified T : Any> get(): T? = get(T::class)

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> get(kClass: KClass<T>): T? = map[kClass] as? T

    public inline fun <reified T : Any> insert(value: T): T? = insert(T::class, value)

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> insert(kClass: KClass<out T>, value: T): T? = map.put(kClass, value) as? T

    public fun insertAny(value: Any) {
        map[value::class] = value
    }

    public fun clone(): Extensions = Extensions(map.toMutableMap())
}

/**
 * Wrapper for storing the final [Url] of a response.
 */
public data class ResponseUrl(
    public val url: Url,
)

/**
 * Extension trait / interface for Response builders to attach a target [Url].
 *
 * Allows the user to add a `Url` to the Response builder.
 */
public interface ResponseBuilderExt {
    /**
     * A builder method for the Response builder type that allows the user to add a [Url]
     * to the Response.
     */
    public fun url(url: Url): ResponseBuilderExt
}
