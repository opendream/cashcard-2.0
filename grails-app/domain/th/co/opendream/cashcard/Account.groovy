package th.co.opendream.cashcard

class Account {
	String accountNumber
	BigDecimal balance
	Date registeredDate
	Boolean status = true
	Users createdBy

	Date dateCreated
	Date lastUpdated

	static belongsTo = [member: Member]

    static constraints = {
    	accountNumber blank: false
    }
}
