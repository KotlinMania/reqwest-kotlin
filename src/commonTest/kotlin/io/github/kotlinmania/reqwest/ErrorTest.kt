// port-lint: tests reqwest/src/error.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ErrorTest {
    @Test
    fun testSourceChain() {
        val root = Error.create(Kind.Request, null)
        assertNull(root.source())

        val link = Error.body(root)
        assertEquals(root, link.source())
    }

    @Test
    fun testIsTimeout() {
        val err = Error.request(TimedOut())
        assertTrue(err.isTimeout())

        val nested = Error.request(Exception("operation timed out"))
        assertTrue(nested.isTimeout())

        val nonTimeout = Error.request(Exception("connection refused"))
        assertFalse(nonTimeout.isTimeout())
        assertTrue(nonTimeout.isConnect())
    }

    @Test
    fun testErrorKinds() {
        val bErr = Error.builder("invalid config")
        assertTrue(bErr.isBuilder())

        val dErr = Error.decode(Exception("bad json"))
        assertTrue(dErr.isDecode())

        val rErr = Error.redirect(Exception("loop"), Url.parse("http://example.com").getOrThrow())
        assertTrue(rErr.isRedirect())
        assertEquals("http://example.com/", rErr.url()?.asStr())

        val sErr = Error.statusCode(Url.parse("http://example.com").getOrThrow(), StatusCode.NOT_FOUND)
        assertTrue(sErr.isStatus())
        assertEquals(StatusCode.NOT_FOUND, sErr.status())
    }
}
