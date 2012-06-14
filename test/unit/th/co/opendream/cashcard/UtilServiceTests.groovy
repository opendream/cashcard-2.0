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
        assertFalse service.check_id_card("111111111")
        assertFalse service.check_id_card("1111111111")
        assertFalse service.check_id_card("11111111111")
        assertFalse service.check_id_card("111111111111")
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

    void testMoneyRoundUp() {
        assert service.moneyRoundUp(0.00) == 0.00
        assert service.moneyRoundUp(100.20) == 100.25
        assert service.moneyRoundUp(100.34) == 100.50
        assert service.moneyRoundUp(100.73) == 100.75
        assert service.moneyRoundUp(100.84) == 101.00
    }
}