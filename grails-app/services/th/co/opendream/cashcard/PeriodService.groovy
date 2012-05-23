package th.co.opendream.cashcard

class PeriodService {
    def interestProcessorService

    def generatePeriod(amount, numberOfPeriod) {
        amount = amount as BigDecimal

        def amountPerPeriod = (int)(amount / numberOfPeriod),
            remain = ((int)amount) % numberOfPeriod

        (0..<numberOfPeriod).collect {
            new Period(amount: amountPerPeriod + (it == numberOfPeriod - 1 ? remain : 0), no: it + 1)
        }
    }

    def getCurrentPeriod(contract) {
        def c = Period.createCriteria()
        c.list(sort: 'no', order: 'asc', max: 1) {
            eq('contract', contract)
            eq('status', true)
            eq('payoffStatus', false)
            isNotNull('dueDate')
        }[0]
    }
}
