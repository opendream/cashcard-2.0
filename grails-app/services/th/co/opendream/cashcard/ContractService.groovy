package th.co.opendream.cashcard

class ContractService {

    def copyLoanProperties(contract, loanType) {
        contract.processor = loanType.interestProcessor
        contract.interestProcessor = loanType.interestProcessor
        contract.periodProcessor = loanType.periodProcessor
        contract.periodGeneratorProcessor = loanType.periodGeneratorProcessor
        contract.interestRate = loanType.interestRate
        contract.maxInterestRate = loanType.maxInterestRate

        contract
    }
}
