// port-lint: tests reqwest/src/async_impl/client.rs
package io.github.kotlinmania.reqwest

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ClientTest {
    @Test
    fun testClientBuilderDefaults() {
        val client =
            Client
                .builder()
                .userAgent("reqwest-kotlin/0.1.0")
                .timeout(30.seconds)
                .cookieStore(true)
                .build()

        assertNotNull(client)
    }

    @Test
    fun testClientExecuteGet() =
        runTest {
            val client =
                Client
                    .builder()
                    .defaultHeaders(HeaderMap().apply { insert(HeaderName.ACCEPT, HeaderValue.fromStr("application/json")) })
                    .build()

            val request =
                client
                    .get("http://example.com/api/v1")
                    .bearerAuth("secret-token-123")
                    .build()

            assertEquals("http://example.com/api/v1", request.url().asStr())
        }

    @Test
    fun testResponseErrorForStatus() {
        val errorResponse =
            Response
                .builder()
                .status(StatusCode.NOT_FOUND)
                .url(Url.parse("http://example.com/missing").getOrThrow())
                .build()

        val err = errorResponse.errorForStatusRef()
        assertNotNull(err)
        assertTrue(err.isStatus())
        assertEquals(StatusCode.NOT_FOUND, err.status())
    }
}
