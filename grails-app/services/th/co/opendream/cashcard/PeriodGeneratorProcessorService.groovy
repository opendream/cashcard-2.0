package th.co.opendream.cashcard

class PeriodGeneratorProcessorService {

    def generate(loanType, amount, numberOfPeriod) {
        if (loanType instanceof Integer) {
           loanType = LoanType.get(loanType)
        }
        def processorName = loanType.processor.toLowerCase()
        this."$processorName"(amount as BigDecimal, numberOfPeriod, loanType.interestRate)
    }

    def effective(amount, numberOfPeriod, intRatePerYear) {
        amount += (intRatePerYear/12) * numberOfPeriod

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod + (id == numberOfPeriod - 1 ? remain : 0), no: id + 1)
        }
    }

    def commission(amount, numberOfPeriod, intRatePerYear) {
        amount += (intRatePerYear/12) * numberOfPeriod

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod + (id == numberOfPeriod - 1 ? remain : 0), no: id + 1)
        }
    }


    def flat(amount, numberOfPeriod, intRatePerYear) {
        amount += (intRatePerYear/12) * numberOfPeriod

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod + (id == numberOfPeriod - 1 ? remain : 0), no: id + 1)
        }
    }


}
