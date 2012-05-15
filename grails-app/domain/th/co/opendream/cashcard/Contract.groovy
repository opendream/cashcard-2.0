package th.co.opendream.cashcard

class Contract {
	String code
	LoanType loanType
	BigDecimal loanAmount = 0.000000
	BigDecimal interestRate = 0.00
	BigDecimal loanBalance = 0.000000
	Boolean approvalStatus = false
	Boolean loanReceiveStatus = false

    static constraints = {
    	code nullable: false, blank: false, unique: true
    }
}
