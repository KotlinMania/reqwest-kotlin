package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ClientAndRequestBuilderTest {
    @Test
    fun testRequestBuilderProperties() {
        val client =
            Client
                .builder()
                .userAgent("test-agent/1.0")
                .timeout(30.seconds)
                .build()

        val req =
            client
                .post("https://example.com/api/v1".asIntoUrl())
                .header("X-Custom", "custom-value")
                .bearerAuth("my-token")
                .json("{\"hello\":\"world\"}")
                .build()

        assertEquals(Method.POST, req.method())
        assertEquals("https://example.com/api/v1", req.url().asStr())
        assertEquals("custom-value", req.headers().get("x-custom")?.asStr())
        assertEquals("Bearer my-token", req.headers().get(HeaderName.AUTHORIZATION)?.asStr())
        assertEquals("application/json", req.headers().get(HeaderName.CONTENT_TYPE)?.asStr())
        assertEquals("{\"hello\":\"world\"}", req.body()?.text())
    }

    @Test
    fun testResponseErrorForStatus() {
        val url = Url.parse("https://example.com/status/500").getOrThrow()
        val res = Response.create(StatusCode.INTERNAL_SERVER_ERROR, url)
        val err = res.errorForStatusRef()
        assertNotNull(err)
        assertTrue(err.isStatus())
        assertEquals(StatusCode.INTERNAL_SERVER_ERROR, err.status())
    }
}
