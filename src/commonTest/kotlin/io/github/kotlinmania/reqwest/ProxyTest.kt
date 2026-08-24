// port-lint: tests proxy.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ProxyTest {

    @Test
    fun testHttpProxy() {
        val proxy = Proxy.http("http://proxy.internal:8080").getOrThrow()
        val httpUrl = Url.parse("http://example.com/api").getOrThrow()
        val httpsUrl = Url.parse("https://example.com/api").getOrThrow()

        val target = proxy.matches(httpUrl)
        assertNotNull(target)
        assertEquals("proxy.internal", target.hostStr())
        assertEquals(8080, target.port())

        assertNull(proxy.matches(httpsUrl))
    }

    @Test
    fun testAllProxyWithNoProxy() {
        val proxy =
            Proxy.all("http://proxy.internal:8080").getOrThrow()
                .noProxy(NoProxy.fromCustom("localhost, 127.0.0.1, internal.domain"))

        val extUrl = Url.parse("https://example.com/").getOrThrow()
        val localUrl = Url.parse("http://localhost:3000/").getOrThrow()
        val intUrl = Url.parse("https://sub.internal.domain/foo").getOrThrow()

        assertNotNull(proxy.matches(extUrl))
        assertNull(proxy.matches(localUrl))
        assertNull(proxy.matches(intUrl))
    }
}
