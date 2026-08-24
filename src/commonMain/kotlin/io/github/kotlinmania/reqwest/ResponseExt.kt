// port-lint: source response.rs
package io.github.kotlinmania.reqwest

/**
 * Wrapper for storing the final [Url] of a response.
 */
public data class ResponseUrl(
    public val url: Url,
)

/**
 * Extension trait / interface for Response builders to attach a target [Url].
 */
public interface ResponseBuilderExt {
    public fun url(url: Url): ResponseBuilderExt
}
