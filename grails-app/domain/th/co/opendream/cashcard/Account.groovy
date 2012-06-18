package th.co.opendream.cashcard

class Account {
	String accountNumber
	BigDecimal balance
	Date registeredDate
	Boolean status = true

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	accountNumber blank: false
    }
}
