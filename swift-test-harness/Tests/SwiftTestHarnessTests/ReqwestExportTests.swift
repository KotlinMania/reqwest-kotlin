import Testing
import Reqwest

@Suite("Reqwest Swift Export Smoke Tests")
struct ReqwestExportTests {
    @Test("Reqwest swift module imports cleanly")
    func swiftModuleLoads() {
        #expect(Bool(true))
    }

    @Test("Reqwest exported types instantiate cleanly")
    func exportedTypesInstantiate() {
        let code = StatusCode.Companion.shared.OK
        #expect(code.asU16() == 200)
        #expect(code.isSuccess() == true)
        #expect(code.isClientError() == false)

        let get = Method.Companion.shared.GET
        #expect(get.name == "GET")
    }
}
