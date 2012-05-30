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
        receiveTx.paymentDate = date

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

    def commission(Period period, amount, fine, isShareCapital, date) {
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
        receiveTx.paymentDate = date

        period.payoffDate = date
        period.payoffStatus = true
        period.cooperativeInterest = periodInterest.cooperativeInterest
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


    def flat(Period period, amount, fine, isShareCapital, date) {
        def periodInterest = interestProcessorService.process(period, date),
            contract = period.contract,
            receiveTx = new ReceiveTransaction()

        def outstanding = amount,
            balancePaid = 0.00,
            interestPaid = 0.00,
            loanBalance = 0.00,
            advancedInterestBalance = contract.advancedInterestBalance

        advancedInterestBalance -= periodInterest.actualInterest

        if (outstanding > contract.loanBalance) {
            outstanding -= contract.loanBalance
            balancePaid = contract.loanBalance
            loanBalance = 0.00
        }
        else {
            balancePaid = outstanding
            outstanding = 0.00
            loanBalance = contract.loanBalance - balancePaid
        }

        receiveTx.amount = amount
        receiveTx.sign = 1
        receiveTx.period = period
        receiveTx.balanceForward = contract.loanBalance
        receiveTx.balancePaid = balancePaid
        receiveTx.interestRate = contract.interestRate
        receiveTx.interestPaid = periodInterest.effectedInterest
        receiveTx.fee = periodInterest.fee
        receiveTx.fine = fine
        receiveTx.isShareCapital = isShareCapital
        receiveTx.paymentDate = date

        // check if is the last period by checking loanBalance == 0.00
        if (loanBalance == 0.00) {
            receiveTx.differential = outstanding + advancedInterestBalance
        }
        else {
            receiveTx.differential = outstanding
        }
        receiveTx.save()

        period.payoffDate = date
        period.payoffStatus = true
        period.save()

        contract.loanBalance -= balancePaid
        contract.advancedInterestBalance = advancedInterestBalance
        contract.save()

        receiveTx
    }
}
