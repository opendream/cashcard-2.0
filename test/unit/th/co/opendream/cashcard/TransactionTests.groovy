package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Transaction)
class TransactionTests {

    Transaction generateValidTransaction() {
    	def transactions = []
        new Transaction(
        	amount: 500,
        	sign: -1
        ).save()

        new Transaction(
        	amount: 500,
        	sign: +1
        ).save()

        transactions
    }


    void testSomething() {
    	generateValidTransaction()
    }

    void testValidateSign() {
        mockForConstraintsTests(Transaction)

        def field = 'sign'
        def transaction = new Transaction()


        [-1, 1, +1].each {
            transaction[field] = it
            assertTrue transaction.validate([field])
        }

        [0, 2, -2].each {
            transaction[field] = it
            assertFalse "MUST FALSE", transaction.validate([field])
        }

    }


}
