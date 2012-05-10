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
        identificationNumber(blank: false, unique: true, matches: /\d{13}/)
        firstname(blank: false)
        lastname(blank: false)
        address(blank: false)
        telNo(blank: false, matches: /\d{9,11}/)
    }
}
