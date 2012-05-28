package th.co.opendream.cashcard

class PeriodGeneratorProcessorService {

    def generate(LoanType loanType, amount, numberOfPeriod) {
        def processorName = loanType.processor.toLowerCase()
        this."$processorName"(amount as BigDecimal, numberOfPeriod, loanType.interestRate)
    }

    def effective(amount, numberOfPeriod, intRatePerYear) {
        amount = amount + (intRatePerYear/12)

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod + (id == numberOfPeriod - 1 ? remain : 0), no: id + 1)
        }
    }

}
