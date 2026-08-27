// port-lint: tests reqwest/src/lib.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MethodTest {
    @Test
    fun testStandardMethods() {
        assertEquals("GET", Method.GET.asStr())
        assertEquals("POST", Method.POST.asStr())
        assertEquals("PUT", Method.PUT.asStr())
        assertEquals("DELETE", Method.DELETE.asStr())
        assertEquals("HEAD", Method.HEAD.asStr())
        assertEquals("OPTIONS", Method.OPTIONS.asStr())
        assertEquals("CONNECT", Method.CONNECT.asStr())
        assertEquals("PATCH", Method.PATCH.asStr())
        assertEquals("TRACE", Method.TRACE.asStr())
    }

    @Test
    fun testCaseInsensitiveEquality() {
        val get1 = Method("GET")
        val get2 = Method("get")
        assertEquals(get1, get2)
        assertEquals(get1.hashCode(), get2.hashCode())
        assertNotEquals(Method.GET, Method.POST)
    }

    @Test
    fun testFromString() {
        val custom = Method.fromString("custom_verb")
        assertEquals("CUSTOM_VERB", custom.asStr())
    }
}
