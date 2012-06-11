package th.co.opendream.cashcard

class InterestProcessorService {

    def process(Period period, date) {
        def processorName = period.contract.interestProcessor.toLowerCase()
        this."$processorName"(period, date)
    }

    def calculateInterestInMonthUnit(amount, interestRate, numberOfPeriod) {
        ( interestRate / 12 / 100 ) * numberOfPeriod * amount
    }

    def getEffectiveInterestRate(contract) {
        contract.maxInterestRate >= contract.interestRate ?
            contract.interestRate
            : contract.maxInterestRate
    }

    def getLastPayment(c) {
        def criteria = ReceiveTransaction.createCriteria()
        criteria.get {
            period {
                contract {
                    eq('id', c.id)
                }
            }
            eq('status', true)
            maxResults(1)
            order('paymentDate', 'desc')
        }
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

        def lastPayment = getLastPayment(contract)

        def dayFromLastPeriod = 0
        if (lastPayment == null) {
            dayFromLastPeriod = date - contract.approvalDate
        }
        else {
            dayFromLastPeriod = date - lastPayment.paymentDate
        }

        def interestRate = contract.interestRate
        def effectiveInterestRate = getEffectiveInterestRate(contract)

        def actualInterest = contract.loanBalance * (interestRate / 100) / yearDivider * dayFromLastPeriod
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

        def lastPayment = getLastPayment(contract)

        def dayFromLastPeriod = 0
        if (lastPayment == null) {
            dayFromLastPeriod = date - contract.approvalDate
        }
        else {
            dayFromLastPeriod = date - lastPayment.paymentDate
        }

        def interestRate = contract.interestRate
        def effectiveInterestRate = getEffectiveInterestRate(contract)

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

        def lastPayment = getLastPayment(contract)

        def dayFromLastPeriod = 0
        if (lastPayment == null) {
            dayFromLastPeriod = date - contract.approvalDate
        }
        else {
            dayFromLastPeriod = date - lastPayment.paymentDate
        }

        def interestRate = contract.interestRate
        def effectiveInterestRate = getEffectiveInterestRate(contract)

        def actualInterest = contract.loanBalance * (interestRate / 100) / yearDivider * dayFromLastPeriod
        def effectedInterest = contract.loanBalance * (effectiveInterestRate / 100) / yearDivider * dayFromLastPeriod
        def fee = 0.00

        actualInterest = actualInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        effectedInterest = effectedInterest.setScale(6, BigDecimal.ROUND_HALF_UP)
        if (effectedInterest < actualInterest) {
            fee = actualInterest - effectedInterest
        }

        def cooperativeInterest = period.contract.cooperativeShare * effectedInterest
        cooperativeInterest = cooperativeInterest.setScale(6, BigDecimal.ROUND_HALF_UP)

        [
            actualInterest: actualInterest,
            effectedInterest: effectedInterest,
            cooperativeInterest: cooperativeInterest,
            fee: fee
        ]
    }
}
