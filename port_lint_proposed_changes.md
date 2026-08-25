# port-lint Proposed Changes

**Generated:** 2026-08-25
**Source:** tmp/reqwest/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/reqwest

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonTest/kotlin/io/github/kotlinmania/reqwest/LibTest.kt` | `// port-lint: tests tests/client.rs` | `// port-lint: tests async_impl/client.rs` | `async_impl/client.rs` | `port-lint provenance header matched only by basename: 'tests:tests/client.rs' vs expected 'async_impl/client.rs'` |
