package th.co.opendream.cashcard

class Transaction {

	BigDecimal amount
	Date dateCreated
	Date lastUpdated
	Integer sign

    static constraints = {
    	sign inList: [-1, 1]
    }
}
