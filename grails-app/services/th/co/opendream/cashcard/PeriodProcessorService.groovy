package th.co.opendream.cashcard

import org.springframework.context.i18n.LocaleContextHolder as LCH

class PeriodProcessorService {
	def interestProcessorService,
        messageSource

    def process(Period period, amount, fine, isShareCapital, date) {
        def processorName = period.contract.processor.toLowerCase()
        this."$processorName"(period, amount, fine, isShareCapital, date)
    }

    def cancelReceiveTransaction(receiveTx) {
        def period = Period.get(receiveTx.period.id)
        if (! receiveTx.id || ! period?.receiveTransaction) {
            throw new Exception("Receive Transaction must already exists before cancel.");
        }

        // Not allow cancelling receive transaction that is not the latest.
        def c = ReceiveTransaction.createCriteria()
        def receiveTxList = c.list(sort: "paymentDate", order: "asc") {
            eq("period", period)
            eq("status", true)
        }
        if (receiveTxList.last() != receiveTx) {
            throw new Exception("Receive Transaction must be the latest to cancel.");
        }

        def processorName = receiveTx.period.contract.processor
        this."cancelReceiveTransaction$processorName"(receiveTx)
    }

    def cancelReceiveTransactionEffective(receiveTx) {
        def period = receiveTx.period,
            contract = period.contract

        receiveTx.status = false
        receiveTx.save()

        period.payoffStatus = false
        period.payAmount -= receiveTx.amount
        period.outstanding += receiveTx.amount
        period.save()

        contract.loanBalance += receiveTx.balancePaid
        contract.save()
    }

    def cancelReceiveTransactionFlat(receiveTx) {
        def period = receiveTx.period,
            contract = period.contract

        receiveTx.status = false
        receiveTx.save()

        period.payoffStatus = false
        period.payAmount -= receiveTx.amount
        period.outstanding += receiveTx.amount
        period.save()

        contract.loanBalance += receiveTx.balancePaid
        contract.advancedInterestBalance += receiveTx.interestPaid + receiveTx.fee
        contract.save()
    }

    def cancelReceiveTransactionCommission(receiveTx) {
        def period = receiveTx.period,
            contract = period.contract

        receiveTx.status = false
        receiveTx.save()

        period.payoffStatus = false
        period.payAmount -= receiveTx.amount
        period.outstanding += receiveTx.amount
        period.cooperativeInterest = 0.00
        period.save()

        contract.loanBalance += receiveTx.balancePaid
        contract.save()
    }

    def effective(Period period, amount, fine, isShareCapital, date) {
        if (amount > period.outstanding) {
            throw new RuntimeException(messageSource.getMessage("errors.receiveOverpayAmount", null, null, LCH.getLocale()))
        }

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
        period.payAmount = actualPaymentAmount
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
        if (amount > period.outstanding) {
            throw new RuntimeException(messageSource.getMessage("errors.receiveOverpayAmount", null, null, LCH.getLocale()))
        }

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
        period.cooperativeInterest = periodInterest.cooperativeInterest
        period.payAmount = actualPaymentAmount
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
        if (amount > period.outstanding) {
            throw new RuntimeException(messageSource.getMessage("errors.receiveOverpayAmount", null, null, LCH.getLocale()))
        }

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
        period.payAmount = amount
        period.save()

        contract.loanBalance -= balancePaid
        contract.advancedInterestBalance = advancedInterestBalance
        contract.save()

        receiveTx
    }
}
