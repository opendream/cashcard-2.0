package th.co.opendream.cashcard

import org.springframework.context.i18n.LocaleContextHolder as LCH

class MessageService {

    def openmsngrClientService,
        messageSource

    def isMobileNumber(number) {
        number.toString() ==~ /\+?(?:66)?0?((8|9)\d{8})/
    }

    def makeMSISDN(number) {
        number = number.toString()

        if (!isMobileNumber(number)) return null

        number.replaceAll(/\+?(?:66)?0?((8|9)\d{8})/, '66$1')
    }

    def send(member, message) {
        def msisdn = makeMSISDN(member.telNo)

        if (!msisdn) return null

        try {
            openmsngrClientService.sendMessage(msisdn, message)
            return true
        }
        catch (Exception e) {
            return false
        }
    }

    def sendApproved(contract) {
        Object[] args = [
            contract.loanType.name,
            contract.loanAmount.setScale(2, BigDecimal.ROUND_HALF_UP),
            contract.approvalDate.format('dd-MM-yyyy')
        ]

        def member = contract.member,
            message = messageSource.getMessage("messages.approved", args, "messages.approved", LCH.getLocale())

        send(member, message)
    }

    def sendPayoff(receiveTx) {
        Object[] args = [
            receiveTx.period.no,
            receiveTx.period.dueDate.format('dd-MM-yyyy'),
            receiveTx.amount
        ]

        def member = receiveTx.period.contract.member,
            message = messageSource.getMessage("messages.payoff", args, "messages.payoff", LCH.getLocale())

        send(member, message)
    }
}
