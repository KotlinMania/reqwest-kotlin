import XCTest
import Reqwest

final class ReqwestExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Reqwest swift module imported cleanly")
    }

    func testStatusCode() throws {
        let code = StatusCode.Companion.shared.OK
        XCTAssertEqual(code.asU16(), 200)
        XCTAssertTrue(code.isSuccess())
        XCTAssertFalse(code.isClientError())
    }

    func testMethod() throws {
        let get = Method.Companion.shared.GET
        XCTAssertEqual(get.name, "GET")
    }
}
