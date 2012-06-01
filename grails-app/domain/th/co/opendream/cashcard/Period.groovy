package th.co.opendream.cashcard

class Period {
    BigDecimal amount = 0.000000
    BigDecimal payAmount = 0.000000
    BigDecimal outstanding = 0.000000
    BigDecimal cooperativeInterest = 0.000000
    Integer no
    Date dueDate
    Boolean status = false
    Boolean payoffStatus = false
    Boolean partialPayoff
    Date payoffDate

    static belongsTo = [contract: Contract]

    def beforeInsert = {
        outstanding = amount
    }

    def beforeUpdate = {
        def remaining = outstanding - payAmount

        if (remaining > 0) {
            partialPayoff = true
            payoffStatus = false
            outstanding = remaining
        }
        else if (remaining == 0) {
            partialPayoff = false
            payoffStatus = true
            outstanding = 0.00
        }
        else {
           // ERROR
        }
    }

    static hasMany = [receiveTransaction: ReceiveTransaction]

    static mapping = {
        receiveTransaction sort:"paymentDate", order: "asc"
    }

    static constraints = {
        dueDate nullable: true
        status nullable: true
        payoffDate nullable: true
        partialPayoff nullable: true
    }

}
