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

        // Special case 
        assertTrue service.check_id_card("1411900088180")
        assertTrue service.check_id_card("1411900088091")
    }

    void testIsPayable() {
    	def contract
    	contract = [id: 1, approvalStatus: false] as Contract
    	assertFalse service.isPayable(contract)

        contract = [id: 1, approvalStatus: true, loanReceiveStatus: true] as Contract
        assertFalse service.isPayable(contract)

    	contract = [id: 1, approvalStatus: true] as Contract
    	assertTrue service.isPayable(contract)
    }
}