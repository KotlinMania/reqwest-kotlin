# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 0/40 (0.0%)
- **Function parity:** 0/1053 matched — 0.0%
- **Class/type parity:** 0/198 matched — 0.0%
- **Combined symbol parity:** 0/1251 matched — 0.0%
- **Average inline-code cosine:** 0.00 (function body across 0 matched files)
- **Average documentation cosine:** 0.00 (doc text across 0 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 0 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **wasm.body** (12 deps)
   - Path: `wasm/body.rs`
   - Essential for 12 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `h3_client.mod` | `asyncimpl.h3client.Mod` | 0 | `async_impl/h3_client/mod.rs` | `asyncimpl/h3client/Mod.kt` |
| `async_impl.mod` | `asyncimpl.Mod` | 0 | `async_impl/mod.rs` | `asyncimpl/Mod.kt` |
| `blocking.mod` | `blocking.Mod` | 0 | `blocking/mod.rs` | `blocking/Mod.kt` |
| `dns.mod` | `dns.Mod` | 0 | `dns/mod.rs` | `dns/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `wasm.mod` | `wasm.Mod` | 0 | `wasm/mod.rs` | `wasm/Mod.kt` |

