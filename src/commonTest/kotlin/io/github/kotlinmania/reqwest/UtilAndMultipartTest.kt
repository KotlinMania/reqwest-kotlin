// port-lint: tests reqwest/src/lib.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UtilAndMultipartTest {
    @Test
    fun testBasicAuth() {
        val auth = Util.basicAuth("user", "pass")
        assertEquals("Basic dXNlcjpwYXNz", auth.asStr())
        assertTrue(auth.isSensitive())
    }

    @Test
    fun testMultipartForm() {
        val form =
            Form("TESTBOUNDARY")
                .text("key", "value")
                .part("file", Part.bytes("binary content".encodeToByteArray()).fileName("test.txt").mimeStr("text/plain"))

        val body = form.toBody()
        val text = body.text()
        assertTrue(text.contains("--TESTBOUNDARY"))
        assertTrue(text.contains("name=\"key\""))
        assertTrue(text.contains("value"))
        assertTrue(text.contains("filename=\"test.txt\""))
        assertTrue(text.contains("binary content"))
    }
}
