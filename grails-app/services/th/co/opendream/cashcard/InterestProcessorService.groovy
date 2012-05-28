package th.co.opendream.cashcard

class InterestProcessorService {

    def process(Period period, date) {
        def processorName = period.contract.loanType.processor.toLowerCase()
        this."$processorName"(period, date)
    }

    def effective(Period period, date) {
        date = date ? date : new Date()

        def contract = period.contract

        def cal = new GregorianCalendar()
        def isLeap = cal.isLeapYear(date.year + 1900)
        def yearDivider = isLeap ? 366 : 365

        def lastPeriod = period
        if (period.no != 1) {
            def c = Period.createCriteria()
            lastPeriod = c.get {
                eq('contract', contract)
                eq('no', period.no - 1)
            }
        }

        def dayFromLastPeriod = 0
        if (period.no == 1) {
            dayFromLastPeriod = date - contract.approvalDate
        }
        else {
            dayFromLastPeriod = date - lastPeriod.payoffDate
        }

        def actualInterest = contract.loanBalance * (contract.interestRate / 100 / yearDivider) * dayFromLastPeriod
        def effectedInterest = contract.loanBalance * (0.18 / yearDivider) * dayFromLastPeriod
        def fee = 0.00

        actualInterest = actualInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        effectedInterest = effectedInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        if (effectedInterest < actualInterest) {
            fee = actualInterest - effectedInterest
        }

        [
            actualInterest: actualInterest,
            effectedInterest: effectedInterest,
            fee: fee
        ]
    }

    def flat(period, date) {
        date = date ? date : new Date()

        def contract = period.contract

        def cal = new GregorianCalendar()
        def isLeap = cal.isLeapYear(date.year + 1900)
        def yearDivider = isLeap ? 366 : 365

        def lastPeriod = period
        if (period.no != 1) {
            def c = Period.createCriteria()
            lastPeriod = c.get {
                eq('contract', contract)
                eq('no', period.no - 1)
            }
        }

        def dayFromLastPeriod = 0
        if (period.no == 1) {
            dayFromLastPeriod = date - contract.approvalDate
        }
        else {
            dayFromLastPeriod = date - lastPeriod.payoffDate
        }

        def effectiveInterestRate = contract.interestRate > contract.maxInterestRate ? contract.maxInterestRate : contract.interestRate

        def actualInterest = contract.loanBalance * (contract.interestRate / 100) / yearDivider * dayFromLastPeriod
        def effectedInterest = contract.loanBalance * (effectiveInterestRate / 100) / yearDivider * dayFromLastPeriod
        def fee = 0.00

        actualInterest = actualInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        effectedInterest = effectedInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        if (effectedInterest < actualInterest) {
            fee = actualInterest - effectedInterest
        }

        [
            actualInterest: actualInterest,
            effectedInterest: effectedInterest,
            fee: fee
        ]
    }

   def commission(Period period, date) {
        date = date ? date : new Date()

        def contract = period.contract

        def cal = new GregorianCalendar()
        def isLeap = cal.isLeapYear(date.year + 1900)
        def yearDivider = isLeap ? 366 : 365

        def lastPeriod = period
        if (period.no != 1) {
            def c = Period.createCriteria()
            lastPeriod = c.get {
                eq('contract', contract)
                eq('no', period.no - 1)
            }
        }

        def dayFromLastPeriod = 0
        if (period.no == 1) {
            dayFromLastPeriod = date - contract.approvalDate
        }
        else {
            dayFromLastPeriod = date - lastPeriod.payoffDate
        }

        def actualInterest = contract.loanBalance * (contract.interestRate / 100 / yearDivider) * dayFromLastPeriod
        def effectedInterest = contract.loanBalance * (0.18 / yearDivider) * dayFromLastPeriod
        def fee = 0.00

        actualInterest = actualInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        effectedInterest = effectedInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        if (effectedInterest < actualInterest) {
            fee = actualInterest - effectedInterest
        }

        [
            actualInterest: actualInterest,
            effectedInterest: effectedInterest,
            cooperativeInterest: period.contract.cooperativeShare * effectedInterest,
            fee: fee
        ]
    }
}
