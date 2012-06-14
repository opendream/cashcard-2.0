package th.co.opendream.cashcard

class PeriodService {
    def interestProcessorService

    def getCurrentPeriod(contract) {
        def c = Period.createCriteria()
        c.list(sort: 'no', order: 'asc', max: 1) {
            eq('contract', contract)
            eq('status', true)
            eq('payoffStatus', false)
            isNotNull('dueDate')
        }[0]
    }

    def beforeInsert(period) {
        period.outstanding = period.amount

        period
    }

    def beforeUpdate(period) {
        period.with {
            def remaining = outstanding - payAmount

            if (remaining > 0) {
                partialPayoff = true
                payoffStatus = false
                outstanding = remaining
            }
            else if (remaining <= 0) {
                partialPayoff = false
                payoffStatus = true
                outstanding = 0.00
            }
        }

        period
    }
}
