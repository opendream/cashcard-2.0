package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Account)
class AccountTests extends DomainTestTemplate {

    def requiredProperties() {
        ['accountNumber', 'balance', 'registeredDate', 'dateCreated',
        'lastUpdated', 'status']
    }

    def domainClass() {
        Account.class
    }

    void testValidateAccountNumber() {
        mockForConstraintsTests(Account)

        def account = new Account()

        verifyNotNull(account, 'accountNumber')

        account.accountNumber = ''
        verifyNotBlank(account, 'accountNumber')
    }

    void testValidateBalance() {
        mockForConstraintsTests(Account)

        def account = new Account()

        verifyNotNull(account, 'balance')
    }

    void testValidateRegisteredDate() {
        mockForConstraintsTests(Account)

        def account = new Account()

        verifyNotNull(account, 'registeredDate')
    }

    void testStatus() {
        mockForConstraintsTests(Account)

        def account = new Account()

        assert account.status == true
    }
}
