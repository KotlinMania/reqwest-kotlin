// port-lint: tests redirect.rs
package io.github.kotlinmania.reqwest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RedirectTest {
    @Test
    fun testRedirectPolicyLimit() {
        val policy = Policy.defaultPolicy()
        val next = Url.parse("http://x.y/z").getOrThrow()
        val previous = (0..9).map { i -> Url.parse("http://a.b/c/$i").getOrThrow() }.toMutableList()

        val action1 = policy.check(StatusCode.FOUND, next, previous)
        assertTrue(action1 is ActionKind.Follow)

        previous.add(Url.parse("http://a.b.d/e/33").getOrThrow())
        val action2 = policy.check(StatusCode.FOUND, next, previous)
        assertTrue(action2 is ActionKind.Error)
        assertTrue(action2.error is TooManyRedirects)
    }

    @Test
    fun testRedirectPolicyLimitTo0() {
        val policy = Policy.limited(0)
        val next = Url.parse("http://x.y/z").getOrThrow()
        val previous = listOf(Url.parse("http://a.b/c").getOrThrow())

        val action = policy.check(StatusCode.FOUND, next, previous)
        assertTrue(action is ActionKind.Error)
        assertTrue(action.error is TooManyRedirects)
    }

    @Test
    fun testRedirectPolicyCustom() {
        val policy =
            Policy.custom { attempt ->
                if (attempt.url().hostStr() == "foo") {
                    attempt.stop()
                } else {
                    attempt.follow()
                }
            }

        val next1 = Url.parse("http://bar/baz").getOrThrow()
        val action1 = policy.check(StatusCode.FOUND, next1, emptyList())
        assertTrue(action1 is ActionKind.Follow)

        val next2 = Url.parse("http://foo/baz").getOrThrow()
        val action2 = policy.check(StatusCode.FOUND, next2, emptyList())
        assertTrue(action2 is ActionKind.Stop)
    }

    @Test
    fun testRemoveSensitiveHeaders() {
        val headers = HeaderMap()
        headers.insert(HeaderName.ACCEPT, HeaderValue.fromStatic("*/*"))
        headers.insert(HeaderName.AUTHORIZATION, HeaderValue.fromStatic("let me in"))
        headers.insert(HeaderName.COOKIE, HeaderValue.fromStatic("foo=bar"))

        val next = Url.parse("http://initial-domain.com/path").getOrThrow()
        val prev = mutableListOf(Url.parse("http://initial-domain.com/new_path").getOrThrow())
        val filteredHeaders = headers.clone()

        removeSensitiveHeaders(headers, next, prev)
        assertEquals(filteredHeaders, headers)

        prev.add(Url.parse("http://new-domain.com/path").getOrThrow())
        filteredHeaders.remove(HeaderName.AUTHORIZATION)
        filteredHeaders.remove(HeaderName.COOKIE)

        removeSensitiveHeaders(headers, next, prev)
        assertEquals(filteredHeaders, headers)
    }
}
