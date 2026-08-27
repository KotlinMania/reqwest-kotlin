// port-lint: source retry.rs
package io.github.kotlinmania.reqwest

public enum class RetryAction {
    Retryable,
    Success,
    NonRetryable,
}

public interface RetryPolicy {
    public fun shouldRetry(
        method: String,
        url: Url,
        status: StatusCode?,
        error: Throwable?,
    ): Boolean

    public fun retryCount(): Int
}

public class DefaultRetryPolicy(
    private val maxRetries: Int = 2,
    private val scopePredicate: ((Url) -> Boolean)? = null,
    private val classifier: (String, Url, StatusCode?, Throwable?) -> RetryAction = { _, _, status, error ->
        if (error != null) {
            RetryAction.Retryable
        } else if (status != null && (status.asU16() == 429 || status.asU16() == 503 || status.asU16() == 504)) {
            RetryAction.Retryable
        } else {
            RetryAction.NonRetryable
        }
    },
) : RetryPolicy {
    private var count: Int = 0

    override fun shouldRetry(
        method: String,
        url: Url,
        status: StatusCode?,
        error: Throwable?,
    ): Boolean {
        if (count >= maxRetries) return false
        if (scopePredicate != null && !scopePredicate.invoke(url)) return false
        val action = classifier(method, url, status, error)
        if (action == RetryAction.Retryable) {
            count++
            return true
        }
        return false
    }

    override fun retryCount(): Int = count
}

public object Retry {
    public fun never(): Builder =
        Builder(
            maxRetries = 0,
            classify = { _, _, _, _ -> RetryAction.NonRetryable },
        )

    public fun default(): Builder = Builder.default()

    public fun forHost(host: String): Builder =
        Builder(
            maxRetries = 2,
            scopePredicate = { it.hostStr() == host },
        )

    public fun scoped(predicate: (Url) -> Boolean): Builder =
        Builder(
            maxRetries = 2,
            scopePredicate = predicate,
        )
}

public class Builder(
    private var maxRetries: Int = 2,
    private var scopePredicate: ((Url) -> Boolean)? = null,
    private var classify: (String, Url, StatusCode?, Throwable?) -> RetryAction = { _, _, status, error ->
        if (error != null) {
            RetryAction.Retryable
        } else {
            RetryAction.NonRetryable
        }
    },
) {
    public fun maxRetriesPerRequest(max: Int): Builder {
        this.maxRetries = max
        return this
    }

    public fun classifyFn(classifier: (StatusCode?) -> RetryAction): Builder {
        this.classify = { _, _, status, error ->
            if (error != null) RetryAction.Retryable else classifier(status)
        }
        return this
    }

    public fun intoPolicy(): RetryPolicy = DefaultRetryPolicy(maxRetries, scopePredicate, classify)

    public companion object {
        public fun default(): Builder = Builder()
    }
}
