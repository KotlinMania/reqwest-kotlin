// port-lint: source lib.rs
package io.github.kotlinmania.reqwest

public object Reqwest {
    public const val VERSION: String = "0.12.28"
}

/**
 * Shortcut method to quickly make a GET request.
 */
public fun get(url: IntoUrl): RequestBuilder = Client.new().get(url)

public fun get(url: String): RequestBuilder = Client.new().get(url.asIntoUrl())
