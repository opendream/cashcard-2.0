package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UtilService)
class UtilServiceTests {

    void testCheckId() {
    	assertTrue service.check_id_card("1159900100015")
    	assertFalse service.check_id_card("1159900100016")
    }
}
