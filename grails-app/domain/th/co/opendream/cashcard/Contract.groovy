package th.co.opendream.cashcard

class Contract {
    String code
    LoanType loanType
    String processor
    String interestProcessor
    String periodProcessor
    String periodGeneratorProcessor
    Date signedDate = new Date()
    BigDecimal loanAmount = 0.000000
    BigDecimal interestRate = 0.00
    BigDecimal cooperativeShare= 0.00
    BigDecimal maxInterestRate = 18.00
    BigDecimal loanBalance = 0.000000
    BigDecimal advancedInterestKeep = 0.000000
    BigDecimal advancedInterestBalance = 0.000000
    Boolean approvalStatus = false
    Boolean loanReceiveStatus = false
    Boolean canPayAllDebt
    String guarantor1
    String guarantor2
    Integer numberOfPeriod
    Date approvalDate
    Date payloanDate
    Date dateCreated
    Date lastUpdated

    static belongsTo = [member: Member, _guarantor1: Member, _guarantor2: Member]

    def beforeInsert = {
        cooperativeShare = loanType.cooperativeShare
    }

    static constraints = {
        code nullable: false, blank: false, unique: true
        approvalDate nullable: true, blank: true
        payloanDate nullable: true, blank: true
        processor nullable: false, blank: false
        interestProcessor nullable: false, blank: false
        periodProcessor nullable: false, blank: false
        periodGeneratorProcessor nullable: false, blank: false
        _guarantor1 blank: true, nullable: true
        _guarantor2 blank: true, nullable: true
        canPayAllDebt nullable: true, blank: true
    }
}
