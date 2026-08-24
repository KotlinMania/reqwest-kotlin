package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StatusCodeTest {
    @Test
    fun testStatusCodeCategories() {
        assertTrue(StatusCode.CONTINUE.isInformational())
        assertTrue(StatusCode.OK.isSuccess())
        assertTrue(StatusCode.MOVED_PERMANENTLY.isRedirection())
        assertTrue(StatusCode.NOT_FOUND.isClientError())
        assertTrue(StatusCode.INTERNAL_SERVER_ERROR.isServerError())

        assertFalse(StatusCode.OK.isClientError())
        assertFalse(StatusCode.OK.isServerError())
    }

    @Test
    fun testFromU16() {
        val ok = StatusCode.fromU16(200).getOrThrow()
        assertEquals(StatusCode.OK, ok)
        assertEquals(200, ok.asU16())
        assertEquals("200", ok.asStr())

        val custom = StatusCode.fromU16(499).getOrThrow()
        assertEquals(499, custom.asU16())
        assertTrue(custom.isClientError())

        assertTrue(StatusCode.fromU16(99).isFailure)
        assertTrue(StatusCode.fromU16(1000).isFailure)
    }
}
