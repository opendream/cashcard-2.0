package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(AccountTransaction)
class AccountTransactionTests extends DomainTestTemplate {

	def requiredProperties() {
		['paymentDate', 'balance', 'balanceForward']
	}

	def domainClass() {
		AccountTransaction.class
	}

    void testIfInteritFromTransaction() {
       def tx = new AccountTransaction()

       assert tx.instanceOf(Transaction) == true
    }

    void testValidateFields() {
    	mockForConstraintsTests(AccountTransaction)

    	def tx = new AccountTransaction()

    	verifyNotNull(tx, 'paymentDate')
    	verifyNotNull(tx, 'balance')
    	verifyNotNull(tx, 'balanceForward')

    	tx.balance = -10.00
    	tx.validate(['balance'])
    	assert tx.errors['balance'] == 'min'

    	tx.balanceForward = -10.00
    	tx.validate(['balanceForward'])
    	assert tx.errors['balanceForward'] == 'min'
    }
}
