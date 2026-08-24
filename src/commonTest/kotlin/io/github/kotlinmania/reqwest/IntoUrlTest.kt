// port-lint: tests into_url.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IntoUrlTest {

    @Test
    fun intoUrlFileScheme() {
        val err = assertFailsWith<Error> {
            "file:///etc/hosts".toReqwestUrl()
        }
        assertTrue(err.source() is BadScheme || err.message?.contains("URL scheme is not allowed") == true)
    }

    @Test
    fun intoUrlBlobScheme() {
        val err = assertFailsWith<Error> {
            "blob:https://example.com".toReqwestUrl()
        }
        assertTrue(err.source() is BadScheme || err.message?.contains("URL scheme is not allowed") == true)
    }

    @Test
    fun intoUrlValidHttp() {
        val url = "http://example.com/foo/bar".toReqwestUrl()
        assertEquals("http", url.scheme())
        assertEquals("example.com", url.hostStr())
        assertEquals("/foo/bar", url.path())
    }
}
