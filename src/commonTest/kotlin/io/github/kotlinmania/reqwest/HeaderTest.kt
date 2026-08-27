// port-lint: tests lib.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HeaderTest {
    @Test
    fun testHeaderMapOperations() {
        val headers = HeaderMap()

        headers.insert(HeaderName.CONTENT_TYPE, HeaderValue.fromStr("application/json"))
        assertEquals("application/json", headers.get(HeaderName.CONTENT_TYPE)?.asStr())

        headers.append(HeaderName.SET_COOKIE, HeaderValue.fromStr("a=1"))
        headers.append(HeaderName.SET_COOKIE, HeaderValue.fromStr("b=2"))
        val cookies = headers.getAll(HeaderName.SET_COOKIE)
        assertEquals(2, cookies.size)
        assertEquals("a=1", cookies[0].asStr())
        assertEquals("b=2", cookies[1].asStr())

        val removed = headers.remove(HeaderName.CONTENT_TYPE)
        assertEquals(1, removed?.size)
        assertEquals("application/json", removed?.get(0)?.asStr())
        assertFalse(headers.containsKey(HeaderName.CONTENT_TYPE))
    }

    @Test
    fun testHeaderValueSensitive() {
        val auth = HeaderValue.fromStr("Bearer secret-token")
        assertFalse(auth.isSensitive())
        assertEquals("Bearer secret-token", auth.toString())

        auth.setSensitive(true)
        assertTrue(auth.isSensitive())
        assertEquals("[sensitive]", auth.toString())
        assertEquals("Bearer secret-token", auth.asStr())
    }
}
