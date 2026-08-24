// port-lint: source config.rs
package io.github.kotlinmania.reqwest

import kotlin.time.Duration

/**
 * Request-scoped configuration store.
 */
public class RequestConfig<T>(public var value: T? = null) {
    public fun fetch(clientDefault: T?): T? = value ?: clientDefault

    public companion object {
        public val TOTAL_TIMEOUT: String = "TotalTimeout"
        public val CONNECT_TIMEOUT: String = "ConnectTimeout"
        public val READ_TIMEOUT: String = "ReadTimeout"
    }
}

/**
 * Configuration container for request and client options.
 */
public class Config {
    public var timeout: Duration? = null
    public var connectTimeout: Duration? = null
    public var readTimeout: Duration? = null
    public var userAgent: String? = null
    public var gzip: Boolean = true
    public var brotli: Boolean = true
    public var zstd: Boolean = true
    public var deflate: Boolean = true
    public var referer: Boolean = true
    public var defaultHeaders: HeaderMap = HeaderMap()

    public fun copy(): Config {
        val c = Config()
        c.timeout = timeout
        c.connectTimeout = connectTimeout
        c.readTimeout = readTimeout
        c.userAgent = userAgent
        c.gzip = gzip
        c.brotli = brotli
        c.zstd = zstd
        c.deflate = deflate
        c.referer = referer
        c.defaultHeaders = defaultHeaders.copy()
        return c
    }
}
