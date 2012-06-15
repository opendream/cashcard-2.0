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
         'period',          'fine',                  'isShareCapital',
         'differential',    'paymentDate',           'status',
         'isAdvancedInterest', 'periodAmountPaid',   'periodVirtualInterestPaid'
        ]
    }

    def domainClass() {
        ReceiveTransaction.class
    }

    void testIsItTransactionSubClass() {
    	def receiveTx = new ReceiveTransaction()

    	assertTrue receiveTx.instanceOf(Transaction)
    }

    void testIsAdvancedInterest() {
        mockForConstraintsTests(ReceiveTransaction)

        def instance = new ReceiveTransaction(),
            field = 'isAdvancedInterest'

        assertTrue instance.validate([field])
    }

    void testPeriodAmountPaid() {
        mockForConstraintsTests(ReceiveTransaction)

        def instance = new ReceiveTransaction(),
            field = 'periodAmountPaid'

        assertTrue instance.validate([field])

        instance[field] = 20.00
        assertTrue instance.validate([field])
    }


    void testPeriod() {
    	mockForConstraintsTests(ReceiveTransaction)

    	def instance = new ReceiveTransaction(),
            field = 'periodVirtualInterestPaid'

    	assertTrue instance.validate([field])

    	instance[field] = 20.00
    	assertTrue instance.validate([field])
    }

    void testFinanceField() {
    	verifyFinanceNumber(ReceiveTransaction, ['balanceForward', 'balancePaid', 'interestRate', 'interestPaid', 'fee', 'fine', 'differential'])
    }

    void testIsShareCapital() {
        mockForConstraintsTests(ReceiveTransaction)

        def instance = new ReceiveTransaction(),
            field = 'isShareCapital'

        verifyNotNull(instance, field)

        instance[field] = [id: 1]
        assertTrue instance.validate([field])
    }

    void testValidatePaymentDate() {
        mockForConstraintsTests(ReceiveTransaction)

        def instance = new ReceiveTransaction(),
            field = 'paymentDate'

        // default value
        assert instance[field].getDateString() == new Date().getDateString()

        instance[field] = null
        verifyNotNull(instance, field)

        instance[field] = new Date()
        assertTrue instance.validate([field])
    }
}
