package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(LoanType)
class LoanTypeTests extends DomainTestTemplate {

    def requiredProperties() {
        [
            'name', 'processor', 'interestRate', 'maxInterestRate',
            'mustKeepAdvancedInterest', 'numberOfPeriod', 'interestProcessor',
			'periodProcessor', 'periodGeneratorProcessor'
        ]
    }

    def domainClass() {
        LoanType.class
    }

    def generateValidLoanType(name, processor) {
        new LoanType(
            name: name, processor: processor, interestRate: 18.00,
            maxInterestRate: 18.00, mustKeepAdvancedInterest: false,
            numberOfPeriod: 3, interestProcessor: processor,
			periodProcessor: processor, periodGeneratorProcessor: processor
        )
    }
	
	void testProcessor() {
		mockForConstraintsTests(LoanType)
		
		def loanType = new LoanType()

		verifyNotNull(loanType, 'interestProcessor')
		loanType.interestProcessor = ''
		verifyNotBlank(loanType, 'interestProcessor')
		
		verifyNotNull(loanType, 'periodProcessor')
		loanType.periodProcessor = ''
		verifyNotBlank(loanType, 'periodProcessor')
		
		verifyNotNull(loanType, 'periodGeneratorProcessor')
		loanType.periodGeneratorProcessor = ''
		verifyNotBlank(loanType, 'periodGeneratorProcessor')
	}

    void testValidateName() {
        mockForConstraintsTests(LoanType)
        def processorName = 'Effective'

        def loanType = new LoanType(),
            field = 'name'

        loanType.validate([field])
        assertEquals "Name must fail null validation.",
            "nullable", loanType.errors[field]

        loanType.name = ""
        loanType.validate([field])
        assertEquals "Name must fail blank validation.",
            "blank", loanType.errors[field]

        def existingLoanType = generateValidLoanType("Existing Loan", processorName).save()
        loanType.name = "Existing Loan"
        loanType.processor = processorName
        loanType.validate([field])
        assertEquals "Name must fail unique validation.",
            "unique", loanType.errors[field]

        loanType.name = "Common Loan"
        assertTrue "Name ${loanType.name} must pass all validations.",
            loanType.validate([field])
    }

    void testFinanceField() {
        verifyFinanceNumber(LoanType, ['interestRate', 'maxInterestRate'])
    }

    void testValidateMustKeepAdvancedInterest() {
        mockForConstraintsTests(LoanType)

        def loanType = new LoanType(),
            field = 'mustKeepAdvancedInterest'

        loanType[field] = true 
        assertTrue "Name ${loanType.name} must pass all validations.",
            loanType.validate([field])
    }

    void testValidateNumberOfPeriod() {
        mockForConstraintsTests(LoanType)

        def loanType = new LoanType(),
            field = 'numberOfPeriod'

        verifyNotNull(loanType, field)

        loanType[field] = 3
        assertTrue "${field} must pass all validations.",
            loanType.validate([field])
    }
}
