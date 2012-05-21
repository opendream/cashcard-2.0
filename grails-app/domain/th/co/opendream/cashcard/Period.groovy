package th.co.opendream.cashcard

class Period {
	BigDecimal amount = 0.000000
	Integer no
	Date dueDate
	Boolean status = false
	Boolean payoffStatus = false

	static belongsTo = [contract: Contract]

    static constraints = {
    	dueDate nullable: true
    	status nullable: true
    }
}