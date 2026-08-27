// port-lint: tests reqwest/src/lib.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RetryTest {
    @Test
    fun testRetryPolicyNever() {
        val policy = Retry.never().intoPolicy()
        val url = Url.parse("https://example.com").getOrThrow()
        val shouldRetry = policy.shouldRetry("GET", url, StatusCode.INTERNAL_SERVER_ERROR, null)
        assertFalse(shouldRetry)
    }

    @Test
    fun testRetryPolicyProtocolNacks() {
        val policy = Builder.default().intoPolicy()
        val url = Url.parse("https://example.com").getOrThrow()
        val error = RuntimeException("connection refused")
        val shouldRetry = policy.shouldRetry("GET", url, null, error)
        assertTrue(shouldRetry)
        assertEquals(1, policy.retryCount())
    }

    @Test
    fun testRetryScoped() {
        val policy = Retry.forHost("example.com").intoPolicy()
        val url1 = Url.parse("https://example.com/api").getOrThrow()
        val url2 = Url.parse("https://other.com/api").getOrThrow()

        val p =
            Retry
                .scoped { it.hostStr() == "example.com" }
                .classifyFn { RetryAction.Retryable }
                .maxRetriesPerRequest(2)
                .intoPolicy()

        assertTrue(p.shouldRetry("GET", url1, StatusCode.BAD_GATEWAY, null))
        assertFalse(p.shouldRetry("GET", url2, StatusCode.BAD_GATEWAY, null))
    }
}
