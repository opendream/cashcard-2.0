package th.co.opendream.cashcard

class ReceiveTransaction extends Transaction {
	Period period
	BigDecimal balanceForward
	BigDecimal balancePaid
	BigDecimal interestRate
	BigDecimal interestPaid
	BigDecimal fee
	BigDecimal fine
	BigDecimal differential
	Boolean isShareCapital
	Date paymentDate = new Date()

    static constraints = {
    }
}
