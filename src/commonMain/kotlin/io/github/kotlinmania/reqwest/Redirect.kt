// port-lint: source redirect.rs
package io.github.kotlinmania.reqwest

public sealed class ActionKind {
    public object Follow : ActionKind()

    public object Stop : ActionKind()

    public class Error(
        public val error: Throwable,
    ) : ActionKind()
}

public class Action(
    internal val kind: ActionKind,
) {
    public companion object {
        public fun follow(): Action = Action(ActionKind.Follow)

        public fun stop(): Action = Action(ActionKind.Stop)

        public fun error(err: Throwable): Action = Action(ActionKind.Error(err))
    }
}

public class Attempt(
    private val status: StatusCode,
    private val next: Url,
    private val previous: List<Url>,
) {
    public fun status(): StatusCode = status

    public fun url(): Url = next

    public fun previous(): List<Url> = previous

    public fun follow(): Action = Action.follow()

    public fun stop(): Action = Action.stop()

    public fun error(err: Throwable): Action = Action.error(err)
}

public class TooManyRedirects : Exception("Too many redirects")

public class Policy internal constructor(
    private val checkFn: (Attempt) -> Action,
) {
    public fun check(
        status: StatusCode,
        next: Url,
        previous: List<Url>,
    ): ActionKind = checkFn(Attempt(status, next, previous)).kind

    public companion object {
        public const val DEFAULT_MAX_REDIRECTS: Int = 10

        public fun default(): Policy = limited(DEFAULT_MAX_REDIRECTS)

        public fun defaultPolicy(): Policy = default()

        public fun none(): Policy = custom { it.stop() }

        public fun limited(max: Int): Policy =
            custom { attempt ->
                if (attempt.previous().size > max) {
                    attempt.error(TooManyRedirects())
                } else {
                    attempt.follow()
                }
            }

        public fun custom(policy: (Attempt) -> Action): Policy = Policy(policy)
    }
}

public fun removeSensitiveHeaders(
    headers: HeaderMap,
    next: Url,
    previous: List<Url>,
) {
    val prev = previous.lastOrNull() ?: return
    val crossHost =
        next.hostStr() != prev.hostStr() ||
            next.portOrKnownDefault() != prev.portOrKnownDefault()

    if (crossHost) {
        headers.remove(HeaderName.AUTHORIZATION)
        headers.remove(HeaderName.COOKIE)
        headers.remove(HeaderName.COOKIE2)
        headers.remove(HeaderName.PROXY_AUTHORIZATION)
        headers.remove(HeaderName.WWW_AUTHENTICATE)
    }
}
