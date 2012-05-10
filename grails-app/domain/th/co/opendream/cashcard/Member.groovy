package th.co.opendream.cashcard


class Member {
    String identificationNumber
    String firstname
    String lastname
    Gender gender
    Status status = Status.ACTIVE
    String address
    String telNo
    BigDecimal balance = 0.000000
    Date dateCreated
    Date lastUpdated

    public enum Gender {
      MALE,
      FEMALE,
      static list() {
       [MALE, FEMALE]
      }
    }

    public enum Status {
        ACTIVE,
        DELETED
        static list() {
            [ACTIVE, DELETED]
        }
    }

    String toString() {
        "${firstname} ${lastname}"
    }

    static constraints = {
        identificationNumber(blank: false, unique: true, validator: { val, obj ->
            if (UtilService.check_id_card(val) == false) {
                ['invalid id']
            }
        })
        firstname(blank: false)
        lastname(blank: false)
        address(nullable: true, blank: true)
        telNo(nullable: true, blank: true, matches: /\d{9,11}/)
        status(nullable: false, blank: false, inList: Status.list())
    }
}
