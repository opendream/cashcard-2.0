package th.co.opendream.cashcard

class MemberService {

    def search(input) {
        def (name, surname) = input.tokenize(" ")
        def c = Member.createCriteria()
        def members = c.list(sort:"firstname") {
            if (surname == null) {
                or {
                    ilike ('firstname', "%${name}%")
                    ilike ('lastname', "%${name}%")
                }
            }
            else {
                and {
                    ilike ('firstname', "%${name}%")
                    ilike ('lastname', "%${surname}%")
                }
            }
        }
        members.collect {
            [id: it.id, name: it.toString()]
        }
    }
}
