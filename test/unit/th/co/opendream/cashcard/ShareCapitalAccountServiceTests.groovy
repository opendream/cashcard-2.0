package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ShareCapitalAccountService)
@Mock([Member, ShareCapitalAccount, Users, AccountTransaction])
class ShareCapitalAccountServiceTests {

	def generateMember() {
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง", lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream")

        m1.save(validate: false)

        m1
    }

    def createAccount(memberInstance) {
        def a1 = new ShareCapitalAccount(accountNumber: '55-01', member: memberInstance, createdBy: [id: 1], balance: 1000.00, registeredDate: new Date())

        a1.save(validate: false)

        a1
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

    void testGetMemberAccount() {
        def member = generateMember()
        createAccount(member)

        def account = service.getMemberAccount(member)
        assert account.id == 1
        assert account.balance == 1000.00
        assert account.member == member
    }

    void testDeposit() {
        def member = generateMember(),
            account = createAccount(member)

        service.deposit(account, 100.00, new Date())

        // Reload account data
        account = ShareCapitalAccount.get(account.id)
        assert account.balance == 1100.00
        assert AccountTransaction.list().size() == 1
    }
}
