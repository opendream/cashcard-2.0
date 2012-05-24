package th.co.opendream.cashcard

import org.springframework.dao.DataIntegrityViolationException

class MemberController {

    def utilService, periodService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.offset = params.offset ? params.int('offset') : 0
        params.max = params.max ? params.int('max') : 10

        def c = Member.createCriteria()
        def memberList = c.list(offset: params.offset, max: params.max) {
            if (params.identificationNumber) {
                ilike('identificationNumber', "%${params.identificationNumber}%")
            }
            if (params.firstname) {
                ilike('firstname', '%' + params.firstname + '%')
            }
            if (params.lastname) {
                ilike('lastname', '%' + params.lastname + '%')
            }
            if (params.telNo) {
                ilike('telNo' , '%' + params.telNo + '%')
            }
        }

        render (view: 'list', model:[memberList: memberList, memberCount: memberList.totalCount])
    }


    def create() {
        [memberInstance: new Member(params)]
    }

    def save() {
        def memberInstance = new Member(params)
        if (!memberInstance.save()) {
            render(view: "create", model: [memberInstance: memberInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'member.label', default: 'Member'), memberInstance.id])
        redirect(action: "show", id: memberInstance.id)
    }

    def show() {
        def memberInstance = Member.get(params.id)
        if (!memberInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'member.label', default: 'Member'), params.id])
            redirect(action: "list")
            return
        }

        def c = Contract.createCriteria()
        def contractList = c.list(sort: 'dateCreated', order: 'asc') {
            eq('member', memberInstance)
        }

        contractList.each {
            it.metaClass.isPayable = utilService.isPayable(it)
            it.metaClass.currentPeriod = periodService.getCurrentPeriod(it)
        }
        render view: 'show', model: [memberInstance: memberInstance, contractList: contractList]
    }

    def edit() {
        def memberInstance = Member.get(params.id)
        withForm {
            if (memberInstance) {
                render(view: 'edit', model: [memberInstance: memberInstance])
            }
            else {
                redirect(uri: '/error')
            }
        }.invalidToken {
            render(view: 'edit', model: [memberInstance: memberInstance])
        }
    }

    def update() {
        def memberInstance = Member.get(params.id)
        if (!memberInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'member.label', default: 'Member'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (memberInstance.version > version) {
                memberInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'member.label', default: 'Member')] as Object[],
                          "Another user has updated this Member while you were editing")
                render(view: "edit", model: [memberInstance: memberInstance])
                return
            }
        }

        memberInstance.properties = params

        if (!memberInstance.save(flush: true)) {
            render(view: "edit", model: [memberInstance: memberInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'member.label', default: 'Member'), memberInstance.id])
        redirect(action: "show", id: memberInstance.id)
    }

    def verifyCard(String cardId) {
        flash.error = null // Clear flash

        def memberInstance = Member.findByIdentificationNumber("${cardId}")
        if (memberInstance) {
            redirect(action: "show", id: memberInstance.id)
        }
        else if (cardId?:true) {
            flash.error = "ไม่พบสมาชิกที่มีหมายเลขบัตรประชาชน ${cardId}, ต้องการลงทะเบียนสมาชิกใหม่? โปรดไปที่ " + link(controller: "member", action: "create") { "ลงทะเบียน" }
            render(view: 'verifyCard')
        }
        else {
            render(view: 'verifyCard')
        }
    }
}
