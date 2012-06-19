package th.co.opendream.cashcard

import th.co.opendream.cashcard.Member.Status

class MemberService {

    def runNoService

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

    def findNewMembers() {
        def newMembers = TempMember.findAllByValidAndValidCreditUnionMemberId(false, false)
    }

    def findUpdateMembers() {
        def updateMembers = TempMember.findAllByValidAndValidCreditUnionMemberId(false, true)
    }

    def findUnChangeMembers() {
        def unchangeMembers = TempMember.findAllByValid(true)
    }

    def findDisabledMembers() {
        def uploadMembers = []
        TempMember.list().collect(uploadMembers) { it.id }

        def c = Member.createCriteria()
        def disabledMembers = c.list(sort:"firstname") {
            not {
                    inList ('creditUnionMemberId', uploadMembers)
                }
        }
        disabledMembers
    }

    def findChangedInMemberUpload() {
        def members = [:]
        members.newMembers = findNewMembers()
        members.updateMembers = findUpdateMembers()
        members.unchangeMembers = findUnChangeMembers()
        members.disabledMembers = findDisabledMembers()
        members
    }

    def mergeMembers() {
        def members = findChangedInMemberUpload()

        def disableMembers = members.disabledMembers
        disableMembers.each {
            def member = Member.findByCreditUnionMemberId(it.creditUnionMemberId)
            member.status = Status.DELETED
            member.save()
        }

        def updateMembers = members.updateMembers
        updateMembers.each {
            def member = Member.findByCreditUnionMemberId(it.creditUnionMemberId)
            member.properties = it.properties
            member.status = Status.ACTIVE
            member.save()
        }

        def newMembers = members.newMembers
        newMembers.each {
            def member = new Member()
            member.properties = it.properties
            if (!member.memberNo) {
                member.memberNo = runNoService.next('Member', member.dateCreated)
            }
            member.save()
        }
    }
}
