package th.co.opendream.cashcard


class Member {
    def utilService

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

    static transients = ["utilService"]

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
        identificationNumber(blank: false, unique: true, validator: { val, self ->
            Member.withNewSession {
                if (self.utilService.check_id_card(val) == false) {
                    return ['invalid.id']
                }
            }
        })
        firstname(blank: false)
        lastname(blank: false)
        address(nullable: true, blank: true)
        telNo(nullable: true, blank: true, matches: /\d{9,11}/)
        status(nullable: false, blank: false, inList: Status.list())
    }
}
