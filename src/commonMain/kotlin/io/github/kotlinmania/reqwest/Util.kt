// port-lint: source reqwest/src/util.rs
package io.github.kotlinmania.reqwest

import kotlin.random.Random

public fun basicAuth(
    username: String,
    password: String? = null,
): HeaderValue = Util.basicAuth(username, password)

public fun fastRandom(): String = Util.fastRandom()

public fun replaceHeaders(
    dst: HeaderMap,
    src: HeaderMap,
): Unit = Util.replaceHeaders(dst, src)

public fun base64Encode(bytes: ByteArray): String = Util.base64Encode(bytes)

public object Util {
    public fun basicAuth(
        username: String,
        password: String? = null,
    ): HeaderValue {
        val raw = if (password != null) "$username:$password" else "$username:"
        val encoded = base64Encode(raw.encodeToByteArray())
        val value = HeaderValue.fromStr("Basic $encoded")
        value.setSensitive(true)
        return value
    }

    public fun fastRandom(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..16).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }

    public fun replaceHeaders(
        dst: HeaderMap,
        src: HeaderMap,
    ) {
        for ((k, v) in src.entries()) {
            dst.insert(k, v)
        }
    }

    public fun base64Encode(bytes: ByteArray): String {
        val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val sb = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val b0 = bytes[i].toInt() and 0xFF
            val b1 = if (i + 1 < bytes.size) bytes[i + 1].toInt() and 0xFF else 0
            val b2 = if (i + 2 < bytes.size) bytes[i + 2].toInt() and 0xFF else 0

            val triple = (b0 shl 16) or (b1 shl 8) or b2

            sb.append(table[(triple shr 18) and 0x3F])
            sb.append(table[(triple shr 12) and 0x3F])
            if (i + 1 < bytes.size) {
                sb.append(table[(triple shr 6) and 0x3F])
            } else {
                sb.append('=')
            }
            if (i + 2 < bytes.size) {
                sb.append(table[triple and 0x3F])
            } else {
                sb.append('=')
            }
            i += 3
        }
        return sb.toString()
    }
}
