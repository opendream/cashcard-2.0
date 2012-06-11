package th.co.opendream.cashcard

class PeriodGeneratorProcessorService {
    def interestProcessorService

    def generate(loanType, amount, numberOfPeriod) {
        if (loanType instanceof Integer) {
           loanType = LoanType.get(loanType)
        }
        def processorName = loanType.periodGeneratorProcessor.toLowerCase()
        this."$processorName"(amount as BigDecimal, numberOfPeriod, loanType.interestRate)
    }

    def effective(amount, numberOfPeriod, intRatePerYear) {
        amount += interestProcessorService.calculateInterestInMonthUnit(amount, intRatePerYear, numberOfPeriod)

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod, no: id + 1)
        }
    }

    def commission(amount, numberOfPeriod, intRatePerYear) {
        amount += interestProcessorService.calculateInterestInMonthUnit(amount, intRatePerYear, numberOfPeriod)

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod, no: id + 1)
        }
    }


    def flat(amount, numberOfPeriod, intRatePerYear) {
        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod, no: id + 1)
        }
    }

}
