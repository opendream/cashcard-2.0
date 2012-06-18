package th.co.opendream.cashcard

class ContractService {

    // Services
    def interestProcessorService,
        utilService

    def copyLoanProperties(contract, loanType) {
        contract.processor = loanType.interestProcessor
        contract.interestProcessor = loanType.interestProcessor
        contract.periodProcessor = loanType.periodProcessor
        contract.periodGeneratorProcessor = loanType.periodGeneratorProcessor
        contract.interestRate = loanType.interestRate
        contract.maxInterestRate = loanType.maxInterestRate
        contract.canPayAllDebt = loanType.canPayAllDebt

        contract
    }

    def getInterestAmountOnCloseContract(period, paymentDate) {
        def periodInterest = interestProcessorService.process(period, paymentDate)

        def callInterest = period.interestOutstanding

        return [
            totalDebt: period.contract.loanBalance + callInterest,
            loanBalance: period.contract.loanBalance,
            goalInterest: period.interestOutstanding,
            realInterest: periodInterest.actualInterest,
            callInterest: callInterest
        ]
    }
}
