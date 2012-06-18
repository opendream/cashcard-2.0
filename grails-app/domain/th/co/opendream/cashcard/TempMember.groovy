package th.co.opendream.cashcard

import th.co.opendream.cashcard.Member.Gender

class TempMember {

	//Long creditUnionMemberId
	String identificationNumber
	String creditUnionMemberNo
	String telNo
	Gender gender
	String firstname
	String lastname
	String address
	Boolean valid
	Boolean validIdentificationNumber
	Boolean validTelNo
	Boolean validFirstname
	Boolean validLastname
	Boolean validGender
	Boolean validCreditUnionMemberNo
	Boolean validCreditUnionMemberId
	Boolean validAddress

	static transients = ['creditUnionMemberId']

	def getCreditUnionMemberId() {
		return id
	}

    static constraints = {
    	identificationNumber(nullable: true, blank: true)    	
    	creditUnionMemberNo(nullable: true, blank: true)
    	telNo(nullable: true, blank: true)
    	gender(nullable: true, blank: true)
    	firstname(nullable: true, blank: true)
    	lastname(nullable: true, blank: true)
    	address(nullable: true, blank: true)
    }

    static mapping = {
    	table 'temp_member'
    	version false
    	id column: 'credit_union_member_id'
    }
}
