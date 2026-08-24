// port-lint: tests tests/client.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LibTest {
    @Test
    fun testReqwestVersion() {
        assertEquals("0.12.28", Reqwest.VERSION)
    }

    @Test
    fun testGetShortcut() {
        val rb = get("https://httpbin.org/get")
        assertNotNull(rb)
    }
}
