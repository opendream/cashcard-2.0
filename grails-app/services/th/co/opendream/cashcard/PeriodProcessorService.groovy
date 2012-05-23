package th.co.opendream.cashcard

class PeriodProcessorService {
	def interestProcessorService

    def process(Period period, amount, fine, isShareCapital, date) {
        def processorName = period.contract.loanType.processor.toLowerCase()
        this."$processorName"(period, amount, fine, isShareCapital, date)
    }

    def effective(Period period, amount, fine, isShareCapital, date) {
        def actualPaymentAmount = amount
        def periodInterest = interestProcessorService.process(period, date)

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
