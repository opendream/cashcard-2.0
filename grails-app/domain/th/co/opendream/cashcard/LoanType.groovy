package th.co.opendream.cashcard

class LoanType {
	String name

    static constraints = {
    	name nullable: false, blank: false, unique: true
    }
}
