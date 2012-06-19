package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ShareCapitalAccountService)
@Mock([Member, ShareCapitalAccount, Users])
class ShareCapitalAccountServiceTests {

	def generateMember() {
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง", lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream")

        m1.save(validate: false)

        m1
    }

    void testCreateAccountFromMember() {
    	def member = generateMember(),
    		accountNumber = '110-55-0001'

    	def count = 0

    	ShareCapitalAccount.metaClass.save = { -> count++ }
    	service.springSecurityService = [principal: [id: 1]]

        service.createAccountFromMember(member, accountNumber, new Date())
        assert count == 1
    }
}
