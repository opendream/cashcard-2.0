package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ReceiveTransaction)
class ReceiveTransactionTests extends DomainTestTemplate {

    def requiredProperties() {
        ['amount',          'balanceForward',        'balancePaid',
         'interestRate',    'interestPaid',          'fee',
         'period',
        ]
    }

    def domainClass() {
        ReceiveTransaction.class
    }

    void testIsItTransactionSubClass() {
    	def receiveTx = new ReceiveTransaction()

    	assertTrue receiveTx.instanceOf(Transaction)
    }

    void testPeriod() {
    	mockForContraintsTests(ReceiveTransaction)

    	def instance = new ReceiveTransaction()

    	verifyNotNull(instance, 'period')

    	instance[field] = [id: 1]
    	assertTrue instance.validate([field])
    }

    void testFinanceField() {
    	verifyFinanceNumber(ReceiveTransaction, ['balanceForward', 'balancePaid', 'interestRate', 'interestPaid', 'fee'])
    }
}
