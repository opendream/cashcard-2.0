package th.co.opendream.cashcard



import grails.test.mixin.*
import org.junit.*

/**
 * @see {@link grails.test.mixin.domain.DomainClassUnitTestMixin}
 */
@TestFor(Member)
class MemberTests extends DomainTestTemplate {

    void testProperties() {
        def requireProperties = [
            'identificationNumber', 'firstname'       , 'lastname',
            'dateCreated'         , 'lastUpdated'     , 'gender'  ,
            'telNo'               , 'address'         , 'balance' ,
            'status'              , 'creditUnionMemberId',
            'creditUnionMemberNo' , 'memberNo'
        ]

        def instanceProperties = Member.metaClass.properties*.name

        assert 0 == (requireProperties - instanceProperties).size()
    }


    void testValidateIdentificationNumber() {
        def field = 'identificationNumber',
            existingMember = generateValidMember()

        mockForConstraintsTests(Member, [existingMember])

        def member = new Member()
        member.utilService = [ check_id_card: { id -> true } ] as UtilService

        assertFalse member.validate([field])
        assert "nullable" == member.errors[field]

        member.identificationNumber = '1411900088198'
        assertFalse member.validate([field])
        assert "unique" == member.errors[field]

        member.utilService = [ check_id_card: { id -> false } ] as UtilService
        member.identificationNumber = '1'
        assert member.validate([field]) == false
        assert "invalid.id" == member.errors[field]

        member.utilService = [ check_id_card: { id -> true } ] as UtilService
        member.identificationNumber = '1159900100015'
        assertTrue member.validate([field])
    }

    void testValidateFirstname() {
        def field = 'firstname',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        assertFalse member.validate([field])

        member.firstname = 'Foo'
        assertTrue member.validate([field])
    }

    void testValidateLastname() {
        def field = 'lastname',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        assertFalse member.validate([field])

        member.lastname = 'Bar'
        assertTrue member.validate([field])
    }

    void testValidateGender() {
        def field = 'gender',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        assertFalse member.validate([field])

        member.gender = Member.Gender.MALE
        assertTrue member.validate([field])
    }

    void testValidateStatus() {
        def field = 'status',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        assertTrue member.validate([field])

        member.status = null
        assertFalse member.validate([field])

        shouldFail(IllegalArgumentException) {
            member.status = "UnsupportedStatus"
        }

        member.status = Member.Status.ACTIVE
        assertTrue member.validate([field])
    }

    void testValidateAddress() {
        def field = 'address',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        assertTrue member.validate([field])

        member.address = "299/99 Areeya Mandarina\nSuthisarn Vinijchai Rd.\nSamsen Nok Huaykwang\nBangkok 10310"
        assertTrue member.validate([field])
    }

    void testValidateTelNo() {
        def field = 'telNo',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        assertTrue member.validate([field])

        member.telNo = '12345ก67890'
        assertFalse member.validate([field])
        assert "matches" == member.errors[field]

        member.telNo = '1234567890'
        assertTrue member.validate([field])
    }

    void testValidateMemberNo() {
        def field = 'memberNo',
            member = new Member()

        mockForConstraintsTests(Member, [member])

        verifyNotNull(member, field)

        def exists = generateValidMember()
        member[field] = exists.memberNo
        verifyUnique(member, field)

        member[field] = ''
        verifyNotBlank(member, field)

        member[field] = '1234567890'
        assertTrue member.validate([field])
    }
}
