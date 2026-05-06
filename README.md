# reqwest-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Freqwest--kotlin-blue.svg)](https://github.com/KotlinMania/reqwest-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/reqwest-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/reqwest-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/reqwest-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/reqwest-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`seanmonstar/reqwes`](https://github.com/seanmonstar/reqwest).

**Original Project:** This port is based on [`seanmonstar/reqwes`](https://github.com/seanmonstar/reqwest). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:reqwest-kotlin:0.1.0-SNAPSHOT")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`seanmonstar/reqwes`](https://github.com/seanmonstar/reqwest). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the reqwes authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`seanmonstar/reqwes`](https://github.com/seanmonstar/reqwest) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
