// port-lint: source reqwest/src/into_url.rs
package io.github.kotlinmania.reqwest

/**
 * A trait to try to convert some type into a [Url].
 *
 * This trait is sealed, such that only types within reqwest can implement it.
 */
public interface IntoUrl : IntoUrlSealed

public interface IntoUrlSealed {
    public fun intoUrl(): Result<Url>

    public fun asStr(): String
}

public class StringIntoUrl(
    private val str: String,
) : IntoUrl {
    override fun intoUrl(): Result<Url> {
        val parsed =
            Url.parse(str).getOrElse { err ->
                return Result.failure(Error.builder(err))
            }
        return parsed.intoUrl()
    }

    override fun asStr(): String = str

    override fun toString(): String = str
}

public fun String.asIntoUrl(): IntoUrl = StringIntoUrl(this)

public fun String.toReqwestUrl(): Url = StringIntoUrl(this).intoUrl().getOrThrow()

public fun tryUri(url: Url): Result<String> {
    if (!url.hasHost() && url.scheme() != "blob" && url.scheme() != "about") {
        return Result.failure(Error.urlInvalidUri(url))
    }
    return Result.success(url.asStr())
}
