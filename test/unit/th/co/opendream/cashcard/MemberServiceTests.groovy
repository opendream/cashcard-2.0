package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MemberService)
@Mock([Member, UtilService])
class MemberServiceTests {

    @Before
    void setUp() {
        def utilService = [
            check_id_card: { id -> true }
        ]

        def m1 = new Member(identificationNumber:"1159900100015", firstname:"สมหญิง",
            lastname: "รักเรียน", telNo: "0818526122", gender: "MALE", address: "Opendream")

        m1.utilService = utilService

        def m2 = new Member(identificationNumber:"141190088198", firstname:"สมหนุ่ม",
            lastname: "รักเรียน", telNo: "0818526133", gender: "MALE", address: "Opendream")

        m2.utilService = utilService

        m1.save()
        m2.save()
    }

    void testValidSearch() {
        //search partial name
        def results

        results = service.search("สม")
        assert results.size() == 2
        results.each {
            assertTrue it.name.contains("สม")
        }

        // Search Exactly firstname
        results = service.search("สมหญิง")
        assert results.size() == 1

        // Search from surname
        results = service.search("รักเรียน")
        assert results.size() == 2

        // Search Exactly firstname+Lastname
        results = service.search("สมหญิง รักเรียน")
        assert results.size() == 1
    }
}
