// port-lint: tests reqwest/src/cookie.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CookieTest {
    @Test
    fun testCookieParse() {
        val result = Cookie.parse(HeaderValue.fromStr("foo=bar; Domain=example.com; Path=/api; HttpOnly; Secure"))
        assertTrue(result.isSuccess)
        val cookie = result.getOrThrow()
        assertEquals("foo", cookie.name())
        assertEquals("bar", cookie.value())
        assertEquals("example.com", cookie.domain())
        assertEquals("/api", cookie.path())
        assertTrue(cookie.httpOnly())
        assertTrue(cookie.secure())
    }

    @Test
    fun testJarStorage() {
        val jar = Jar()
        val url = Url.parse("https://example.com/api/test").getOrThrow()
        jar.setCookies(listOf(HeaderValue.fromStr("session_id=12345; Domain=example.com; Path=/api")), url)

        val header = jar.cookies(url)
        assertNotNull(header)
        assertEquals("session_id=12345", header.asStr())

        val otherUrl = Url.parse("https://other.com/api/test").getOrThrow()
        assertNull(jar.cookies(otherUrl))
    }
}
