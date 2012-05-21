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
         'period',          'fine',                  'isShareCapital'
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
    	mockForConstraintsTests(ReceiveTransaction)

    	def instance = new ReceiveTransaction(),
            field = 'period'

    	verifyNotNull(instance, field)

    	instance[field] = [id: 1]
    	assertTrue instance.validate([field])
    }

    void testFinanceField() {
    	verifyFinanceNumber(ReceiveTransaction, ['balanceForward', 'balancePaid', 'interestRate', 'interestPaid', 'fee', 'fine'])
    }

    void testIsShareCapital() {
        mockForConstraintsTests(ReceiveTransaction)

        def instance = new ReceiveTransaction(),
            field = 'isShareCapital'

        verifyNotNull(instance, field)

        instance[field] = [id: 1]
        assertTrue instance.validate([field])
    }
}
