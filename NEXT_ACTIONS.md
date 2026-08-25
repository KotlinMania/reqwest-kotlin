# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 16/40 (40.0%)
- **Function parity:** 146/963 matched (target 268) — 15.2%
- **Class/type parity:** 35/198 matched (target 72) — 17.7%
- **Combined symbol parity:** 181/1161 matched (target 340) — 15.6%
- **Average inline-code cosine:** 0.18 (function body across 16 matched files)
- **Average documentation cosine:** 0.33 (doc text across 16 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 16 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **wasm.body** (12 deps)
   - Path: `wasm/body.rs`
   - Essential for 12 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. response

- **Target:** `reqwest.ResponseExt [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 7
- **Priority Score:** 7020410.0
- **Functions:** 0/2 matched (target 0)
- **Missing functions:** `url`, `test_response_builder_ext`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 2. cookie

- **Target:** `reqwest.Cookie`
- **Similarity:** 0.35
- **Dependents:** 3
- **Priority Score:** 3143006.5
- **Functions:** 13/21 matched (target 20)
- **Missing functions:** `expires`, `fmt`, `extract_response_cookie_headers`, `extract_response_cookies`, `new`, `poll_ready`, `call`, `poll`
- **Types:** 3/9 matched (target 4)
- **Missing types:** `CookieParseError`, `CookieService`, `Response`, `Error`, `Future`, `Output`

### 3. error

- **Target:** `reqwest.Error`
- **Similarity:** 0.44
- **Dependents:** 2
- **Priority Score:** 2134405.5
- **Functions:** 26/37 matched (target 32)
- **Missing functions:** `new`, `url_mut`, `into_io`, `cast_to_internal_error`, `fmt`, `from`, `assert_send`, `assert_sync`, `mem_size_of`, `roundtrip_io_error`, `from_unknown_io_error`
- **Types:** 5/7 matched (target 14)
- **Missing types:** `Result`, `BoxError`
- **Tests:** 1/6 matched

### 4. into_url

- **Target:** `reqwest.Url`
- **Similarity:** 0.30
- **Dependents:** 2
- **Priority Score:** 2000607.0
- **Functions:** 4/4 matched (target 33)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 5. tls

- **Target:** `reqwest.Tls`
- **Similarity:** 0.09
- **Dependents:** 1
- **Priority Score:** 1344009.1
- **Functions:** 4/29 matched (target 8)
- **Missing functions:** `clone`, `from_pem_bundle`, `add_to_native_tls`, `add_to_rustls`, `read_pem_certs`, `as_rustls_crl`, `fmt`, `to_native_tls`, `from_rustls`, `default`, `verify_server_cert`, `verify_tls12_signature`, `verify_tls13_signature`, `supported_verify_schemes`, `new`, `peer_certificate`, `certificate_from_der_invalid`, `certificate_from_pem_invalid`, `identity_from_pkcs12_der_invalid`, `identity_from_pkcs8_pem_invalid`, `identity_from_pem_invalid`, `identity_from_pem_pkcs1_key`, `certificates_from_pem_bundle`, `crl_from_pem`, `crl_from_pem_bundle`
- **Types:** 2/11 matched (target 4)
- **Missing types:** `CertificateRevocationList`, `Cert`, `ClientCert`, `Version`, `InnerVersion`, `TlsBackend`, `NoVerifier`, `IgnoreHostname`, `TlsInfo`
- **Tests:** 0/9 matched

### 6. async_impl.client

- **Target:** `reqwest.Client [PROVENANCE-FALLBACK]`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 1032708.7
- **Functions:** 23/109 matched (target 36)
- **Missing functions:** `default`, `poll_ready`, `call`, `gzip`, `brotli`, `zstd`, `deflate`, `no_gzip`, `no_brotli`, `no_zstd`, `no_deflate`, `referer`, `connection_verbose`, `pool_idle_timeout`, `pool_max_idle_per_host`, `http1_title_case_headers`, `http1_allow_obsolete_multiline_headers_in_responses`, `http1_ignore_invalid_headers_in_responses`, `http1_allow_spaces_after_header_name_in_responses`, `http1_only`, `http09_responses`, `http2_prior_knowledge`, `http3_prior_knowledge`, `http2_initial_stream_window_size`, `http2_initial_connection_window_size`, `http2_adaptive_window`, `http2_max_frame_size`, `http2_max_header_list_size`, `http2_keep_alive_interval`, `http2_keep_alive_timeout`, `http2_keep_alive_while_idle`, `tcp_nodelay`, `local_address`, `interface`, `tcp_keepalive`, `tcp_keepalive_interval`, `tcp_keepalive_retries`, `tcp_user_timeout`, `unix_socket`, `windows_named_pipe`, `add_root_certificate`, `add_crl`, `add_crls`, `tls_built_in_root_certs`, `tls_built_in_webpki_certs`, `tls_built_in_native_certs`, `identity`, `danger_accept_invalid_hostnames`, `danger_accept_invalid_certs`, `tls_sni`, `min_tls_version`, `max_tls_version`, `use_native_tls`, `use_rustls_tls`, `use_preconfigured_tls`, `tls_info`, `trust_dns`, `hickory_dns`, `no_trust_dns`, `no_hickory_dns`, `resolve`, `resolve_to_addrs`, `dns_resolver`, `dns_resolver2`, `tls_early_data`, `http3_max_idle_timeout`, `http3_stream_receive_window`, `http3_conn_receive_window`, `http3_send_window`, `http3_congestion_bbr`, `http3_max_field_section_size`, `http3_send_grease`, `connector_layer`, `execute_request`, `proxy_auth`, `proxy_custom_headers`, `fmt`, `fmt_fields`, `in_flight`, `total_timeout`, `new_err`, `inner`, `poll`, `execute_request_rejects_invalid_urls`, `execute_request_rejects_invalid_hostname`, `test_future_size`
- **Types:** 2/18 matched (target 4)
- **Missing types:** `HttpVersionPref`, `Accepts`, `HyperService`, `Error`, `Response`, `Future`, `Config`, `HyperClient`, `MaybeCookieService`, `MaybeDecompression`, `LayeredService`, `LayeredFuture`, `ClientRef`, `PendingInner`, `ResponseFuture`, `Output`
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:tests/client.rs` vs expected `async_impl/client.rs`
- **Proposed provenance header:** `// port-lint: tests async_impl/client.rs` (current: `// port-lint: tests tests/client.rs`)
- **Lint issues:** 1

### 7. async_impl.request

- **Target:** `reqwest.Request`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 375408.3
- **Functions:** 15/50 matched (target 32)
- **Missing functions:** `method_mut`, `url_mut`, `headers_mut`, `body_mut`, `extensions`, `extensions_mut`, `timeout_mut`, `version_mut`, `try_clone`, `pieces`, `from_parts`, `header_sensitive`, `multipart`, `fetch_mode_no_cors`, `build_split`, `fmt`, `fmt_request_fields`, `extract_authority`, `try_from`, `add_query_append`, `add_query_append_same`, `add_query_struct`, `add_query_map`, `test_replace_headers`, `normalize_empty_query`, `try_clone_reusable`, `try_clone_no_body`, `try_clone_stream`, `convert_url_authority_into_basic_auth`, `test_basic_auth_sensitive_header`, `test_bearer_auth_sensitive_header`, `test_explicit_sensitive_header`, `convert_from_http_request`, `set_http_request_version`, `builder_split_reassemble`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `Error`, `Params`
- **Tests:** 0/16 matched

### 8. proxy

- **Target:** `reqwest.Proxy`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 344808.8
- **Functions:** 8/40 matched (target 21)
- **Missing functions:** `_implied_bounds`, `prox`, `url`, `new`, `custom_http_auth`, `headers`, `into_matcher`, `cache_maybe_has_http_auth`, `cache_maybe_has_http_custom_headers`, `fmt`, `from_env`, `system`, `intercept`, `maybe_has_http_auth`, `http_non_tunnel_basic_auth`, `maybe_has_http_custom_headers`, `http_non_tunnel_custom_headers`, `uri`, `custom_headers`, `raw_auth`, `url_auth`, `call`, `encode_basic_auth`, `intercepted_uri`, `test_http`, `test_https`, `test_all`, `test_custom`, `test_standard_with_custom_auth_header`, `test_custom_with_custom_auth_header`, `test_maybe_has_http_auth`, `test_socks_proxy_default_port`
- **Types:** 6/8 matched (target 13)
- **Missing types:** `Matcher`, `Intercepted`
- **Tests:** 0/9 matched

### 9. async_impl.multipart

- **Target:** `reqwest.Multipart`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 283908.7
- **Functions:** 9/33 matched (target 14)
- **Missing functions:** `default`, `file`, `percent_encode_path_segment`, `percent_encode_attr_chars`, `percent_encode_noop`, `stream`, `into_stream`, `part_stream`, `compute_length`, `with_inner`, `fmt`, `stream_with_length`, `value_len`, `metadata`, `take_fields`, `fmt_fields`, `encode_headers`, `percent_encode`, `gen_boundary`, `form_empty`, `stream_to_end`, `stream_to_end_with_header`, `correct_content_length`, `header_percent_encoding`
- **Types:** 2/6 matched (target 2)
- **Missing types:** `FormParts`, `PartMetadata`, `PartProps`, `PercentEncoding`
- **Tests:** 0/5 matched

### 10. retry

- **Target:** `reqwest.Retry`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 263409.0
- **Functions:** 7/22 matched (target 10)
- **Missing functions:** `no_budget`, `max_extra_load`, `classify`, `retry`, `clone_request`, `is_retryable_error`, `applies_to`, `fmt`, `method`, `uri`, `status`, `error`, `retryable`, `success`, `is_protocol_nack`
- **Types:** 1/12 matched (target 5)
- **Missing types:** `Policy`, `Req`, `Future`, `Scope`, `ScopeFn`, `Scoped`, `Classify`, `ClassifyFn`, `ReqRep`, `Action`, `Classifier`

### 11. async_impl.body

- **Target:** `reqwest.Body`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 192609.2
- **Functions:** 6/21 matched (target 10)
- **Missing functions:** `wrap_stream`, `stream`, `wrap`, `default`, `fmt`, `poll_frame`, `size_hint`, `is_end_stream`, `total_timeout`, `with_read_timeout`, `boxed`, `response`, `box_err`, `test_as_bytes`, `body_exact_length`
- **Types:** 1/5 matched (target 1)
- **Missing types:** `Inner`, `Data`, `Error`, `ResponseBody`
- **Tests:** 0/2 matched

### 12. async_impl.response

- **Target:** `reqwest.Response`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 132407.6
- **Functions:** 10/23 matched (target 20)
- **Missing functions:** `new`, `headers_mut`, `remote_addr`, `extensions`, `extensions_mut`, `text_with_charset`, `json`, `chunk`, `bytes_stream`, `body_mut`, `fmt`, `from`, `test_from_http_response`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 13. redirect

- **Target:** `reqwest.Redirect`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 113206.0
- **Functions:** 16/25 matched (target 20)
- **Missing functions:** `redirect`, `is_default`, `fmt`, `new`, `with_referer`, `with_https_only`, `make_referer`, `on_request`, `clone_body`
- **Types:** 5/7 matched (target 9)
- **Missing types:** `PolicyKind`, `TowerRedirectPolicy`
- **Tests:** 4/4 matched

### 14. config

- **Target:** `reqwest.Config`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 81009.8
- **Functions:** 1/6 matched (target 2)
- **Missing functions:** `default`, `new`, `fmt_as_field`, `get`, `get_mut`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `RequestConfigValue`, `TotalTimeout`, `Value`

### 15. util

- **Target:** `reqwest.Util`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 40708.3
- **Functions:** 3/6 matched (target 8)
- **Missing functions:** `add_cookie_header`, `new`, `fmt`
- **Types:** 0/1 matched
- **Missing types:** `Escape`

### 16. lib

- **Target:** `reqwest.Lib`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 40508.5
- **Functions:** 1/5 matched (target 2)
- **Missing functions:** `_assert_impls`, `assert_send`, `assert_sync`, `assert_clone`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

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
| `wasm.mod` | `wasm.Mod` | 0 | `wasm/mod.rs` | `wasm/Mod.kt` |

