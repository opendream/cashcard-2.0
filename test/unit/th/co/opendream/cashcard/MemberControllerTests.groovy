package th.co.opendream.cashcard

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*


import th.co.opendream.cashcard.Member.Gender
import th.co.opendream.cashcard.Member.Status


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock([Member])
class MemberControllerTests {

    @Before
    void setUp() {
        def m1 = new Member(identificationNumber:"1159900100015", firstname:"Nat", lastname: "Weerawan", telNo: "111111111", gender: "MALE", address: "Opendream")
        def m2 = new Member(identificationNumber: "3660500550343", firstname: "Noomz", lastname: "Siriwat", telNo: "111111111", gender: "MALE", address: "Opendream2")
        mockDomain(Member, [m1, m2])
        assert Member.size() == 2
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    void testSomething() {
        //fail "Implement me"
        assert 3 == 4
    }
}
