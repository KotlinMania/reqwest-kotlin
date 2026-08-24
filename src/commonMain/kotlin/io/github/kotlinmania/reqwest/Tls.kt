// port-lint: source tls.rs
package io.github.kotlinmania.reqwest

/**
 * Represents a server X.509 certificate.
 */
public class Certificate private constructor(
    private val raw: ByteArray,
    private val isPem: Boolean,
) {
    public fun toByteArray(): ByteArray = raw.copyOf()

    public companion object {
        public fun fromDer(der: ByteArray): Certificate = Certificate(der.copyOf(), false)

        public fun fromPem(pem: ByteArray): Certificate = Certificate(pem.copyOf(), true)

        public fun fromPem(pem: String): Certificate = Certificate(pem.encodeToByteArray(), true)
    }
}

/**
 * Represents a client certificate and private key identity.
 */
public class Identity private constructor(
    private val raw: ByteArray,
    private val format: Format,
) {
    public enum class Format {
        PKCS12,
        PKCS8,
        PEM,
    }

    public fun toByteArray(): ByteArray = raw.copyOf()

    public companion object {
        public fun fromPkcs12Der(der: ByteArray, password: String): Identity =
            Identity(der.copyOf(), Format.PKCS12)

        public fun fromPkcs8Pem(pem: ByteArray, key: ByteArray): Identity =
            Identity(pem.copyOf(), Format.PKCS8)

        public fun fromPem(pem: ByteArray): Identity =
            Identity(pem.copyOf(), Format.PEM)
    }
}

/**
 * TLS Protocol version.
 */
public enum class TlsVersion {
    TLS_1_0,
    TLS_1_1,
    TLS_1_2,
    TLS_1_3,
}
