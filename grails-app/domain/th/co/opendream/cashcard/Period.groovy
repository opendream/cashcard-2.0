package th.co.opendream.cashcard

class Period {
	BigDecimal amount = 0.000000
	Integer no
	Date dueDate
	Status status

	static belongsTo = [contract: Contract]

	enum Status {
		PAID,
		QUEUED,
		CANCELLED,
	}

    static constraints = {
    	dueDate nullable: true
    	status nullable: true
    }
}
