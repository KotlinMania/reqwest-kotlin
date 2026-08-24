package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UrlTest {
    @Test
    fun testParseUrlWithPortQueryFragment() {
        val url = Url.parse("https://user:pass@example.com:8443/path/to/resource?query=1&b=2#frag").getOrThrow()
        assertEquals("https", url.scheme())
        assertEquals("user", url.username())
        assertEquals("pass", url.password())
        assertEquals("example.com", url.hostStr())
        assertEquals(8443, url.port())
        assertEquals("/path/to/resource", url.path())
        assertEquals("query=1&b=2", url.query())
        assertEquals("frag", url.fragment())
        assertEquals("https://user:pass@example.com:8443/path/to/resource?query=1&b=2#frag", url.asStr())
    }

    @Test
    fun testKnownDefaultPorts() {
        val http = Url.parse("http://example.com/").getOrThrow()
        assertEquals(80, http.portOrKnownDefault())

        val https = Url.parse("https://example.com/").getOrThrow()
        assertEquals(443, https.portOrKnownDefault())
    }

    @Test
    fun testInvalidUrl() {
        assertTrue(Url.parse("not-a-valid-url").isFailure)
    }
}
