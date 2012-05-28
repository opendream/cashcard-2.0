package th.co.opendream.cashcard

class PeriodGeneratorProcessorService {

    def serviceMethod() {
    def effective(amount, numberOfPeriod, intRatePerYear) {
        amount = amount + (intRatePerYear/12)

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect { id ->
            new Period(amount: amountPerPeriod + (id == numberOfPeriod - 1 ? remain : 0), no: id + 1)
        }
    }
}
