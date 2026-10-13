// port-lint: tests response.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseExtTest {
    @Test
    fun testResponseBuilderExt() {
        val url = Url.parse("http://example.com").getOrThrow()
        val response =
            ResponseBuilder()
                .status(StatusCode.OK)
                .url(url)
                .body(Body.empty())
                .build()

        assertEquals(
            ResponseUrl(url),
            response.extensions().get<ResponseUrl>(),
        )
    }
}
