package th.co.opendream.cashcard

class AccountTransaction extends Transaction {
	BigDecimal balance
	BigDecimal balanceForward
	Date paymentDate

    static constraints = {
    	balance min: 0.00
    	balanceForward min: 0.00
    }
}
