package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ContractService)
@Mock([Contract, LoanType])
class ContractServiceTests {

    void testCopyLoanProperties() {
        def loanType = new LoanType(
            name: "Common",
            processor: "effective",
            interestProcessor: "effective",
            periodProcessor: "effective",
            periodGeneratorProcessor: "effective",
            interestRate: 12.00,
            maxInterestRate: 18.00
        )
        loanType.save()

        def contract = new Contract()

        service.copyLoanProperties(contract, loanType)
        assert contract.processor == "effective"
        assert contract.interestProcessor == "effective"
        assert contract.periodProcessor == "effective"
        assert contract.periodGeneratorProcessor == "effective"
        assert contract.interestRate == 12.00
        assert contract.maxInterestRate == 18.00

        loanType.interestProcessor = "flat"
        loanType.periodProcessor = "effective"
        loanType.periodGeneratorProcessor = "commission"
        loanType.interestRate = 17.00
        loanType.maxInterestRate = 21.00

        service.copyLoanProperties(contract, loanType)
        assert contract.interestProcessor == "flat"
        assert contract.periodProcessor == "effective"
        assert contract.periodGeneratorProcessor == "commission"
        assert contract.interestRate == 17.00
        assert contract.maxInterestRate == 21.00
    }
}
