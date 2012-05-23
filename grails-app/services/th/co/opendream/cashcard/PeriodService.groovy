package th.co.opendream.cashcard

class PeriodService {
    def processorService

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

    def periodPayoff(period, amount, fine, isShareCapital, date) {
        def actualPaymentAmount = amount
        def periodInterest = processorService.process(period, date)

        def receiveTx = new ReceiveTransaction()

        if (amount >= periodInterest.actualInterest) {
            amount -= periodInterest.actualInterest
        }
        else {
            throw new Exception("Amount not enough for this period.")
        }

        if (amount > 0.00) {
            receiveTx.balancePaid = amount
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
        contract.loanBalance -= receiveTx.balancePaid
        if (contract.loanBalance < 0.00) {
            receiveTx.differential = contract.loanBalance.abs()
            contract.loanBalance = 0.00
        }
        else {
            receiveTx.differential = 0.00
        }
        receiveTx.save()
        contract.save()

        receiveTx
    }
}
