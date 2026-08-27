// port-lint: tests lib.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class VersionTest {
    @Test
    fun testVersions() {
        assertEquals("HTTP/0.9", Version.HTTP_09.asStr())
        assertEquals("HTTP/1.0", Version.HTTP_10.asStr())
        assertEquals("HTTP/1.1", Version.HTTP_11.asStr())
        assertEquals("HTTP/2.0", Version.HTTP_2.asStr())
        assertEquals("HTTP/3.0", Version.HTTP_3.asStr())

        assertEquals(Version.HTTP_11, Version("HTTP/1.1"))
        assertNotEquals(Version.HTTP_11, Version.HTTP_2)
    }
}
