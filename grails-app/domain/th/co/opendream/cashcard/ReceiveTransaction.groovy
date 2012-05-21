package th.co.opendream.cashcard

class ReceiveTransaction extends Transaction {
	Period period
	BigDecimal balanceForward
	BigDecimal balancePaid
	BigDecimal interestRate
	BigDecimal interestPaid
	BigDecimal fee

    static constraints = {
    }
}
