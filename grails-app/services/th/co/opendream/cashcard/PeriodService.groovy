package th.co.opendream.cashcard

class PeriodService {

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

    def calculateInterestFormulaOne(period, date) {
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

    def periodPayoff(period, amount, fine, isShareCapital, date) {
        def actualPaymentAmount = amount
        def periodInterest = calculateInterestFormulaOne(period, date)

        def receiveTx = new ReceiveTransaction()

        if (amount >= periodInterest.actualInterest) {
            amount -= periodInterest.actualInterest
        }
        else {
            throw new Exception("Amount not enough for this period.")
        }

        receiveTx.amount = actualPaymentAmount
        receiveTx.sign = 1
        receiveTx.period = period
        receiveTx.balanceForward = period.contract.loanBalance
        receiveTx.interestRate = period.contract.interestRate
        receiveTx.interestPaid = periodInterest.effectedInterest
        receiveTx.fee = periodInterest.fee
        receiveTx.fine = fine
        receiveTx.isShareCapital = isShareCapital

        period.payoffDate = date
        period.payoffStatus = true
        period.save()

        def contract = period.contract
        if (amount > 0.00) {
            if (contract.loanBalance >= amount) {
                receiveTx.balancePaid = amount
                contract.loanBalance -= receiveTx.balancePaid
                amount = 0.00
            }
            else {
                amount -= contract.loanBalance
                receiveTx.balancePaid = contract.loanBalance
                contract.loanBalance = 0.00
            }
        }
        receiveTx.differential = amount
        receiveTx.save()
        contract.save()

        receiveTx
    }
}
