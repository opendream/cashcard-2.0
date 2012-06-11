package th.co.opendream.cashcard

class ReportController {

    def overdue() {
    	render(view:'overdue')
    }

    def doPaymentOverdueReport() {
        if(params.selectedDate) {
            params._name = params.name
            params._file = params.file
            params._format = params.format
    	    params.selectedDate = params.selectedDate.format("yyyy-MM-dd'T'HH:mm:ss'Z'")
            // fixed for demo
            params.selectedDate = "$params.selectedDate_year-$params.selectedDate_month-$params.selectedDate_day"

    	    chain(controller:'jasper',action:'index',params:params)
        } else {
            render(view:'overdue')
        }
    }

    def payment() {
    	render(view:'payment', model:[type:params.paymenttype])
    }

    def doPaymentReport() {
        if(params.fromDate <= params.toDate) {
            def d1 = params.fromDate
            def d2 = params.toDate
            params._name = params.name
            params._file = params.file
            params._format = params.format
            params.toDate = params.toDate.next()



            if (params.paymenttype == 'daily-report') {
                def c = Contract.createCriteria()
                def payloan = c.list {
                    between('payloanDate', d1, d2)
                }
                def payloanCollected = payloan.collect { contract ->
                    [
                        reportType: "จ่ายเงินกู้",
                        id: contract.id, contract_code: contract.code,
                        date: contract.payloanDate,
                        member_firstname: contract.member.firstname,
                        member_lastname: contract.member.lastname,
                        amount: contract.loanAmount,
                        fromDate: params.fromDate.format("dd/MM/yyyy"),
                        toDate: params.toDate.format("dd/MM/yyyy"),
                        date: contract.payloanDate.format("dd/MM/yyyy")
                    ]
                }

                def data = payloanCollected
                def rc = ReceiveTransaction.createCriteria()
                def rtx = rc.list {
                    between('paymentDate', d1, d2)
                }

                def payoffCollected = rtx.collect {  tx ->
                    def _contract = tx.period.contract
                    [
                        reportType: "ชำระเงินกู้",
                        id: tx.id,
                        date: tx.paymentDate,
                        member_firstname: _contract.member.firstname,
                        member_lastname: _contract.member.lastname,
                        amount: tx.balancePaid + tx.interestPaid,
                        fromDate: params.fromDate.format("dd/MM/yyyy"),
                        toDate: params.toDate.format("dd/MM/yyyy"),
                        date: tx.paymentDate.format("dd/MM/yyyy"),
                        contract_code: _contract.code
                    ]
                }

                data += payoffCollected

                chain(controller:'jasper',action:'index',params:params, model: [data: data])
            }
            else {
                // fixed for demo
                params.fromDate = "$params.fromDate_year-$params.fromDate_month-$params.fromDate_day"
                params.toDate = "$params.toDate_year-$params.toDate_month-$params.toDate_day"
                chain(controller:'jasper',action:'index',params:params)
            }
        } else {
            render(view:'payment', model:[type:params._file])
        }
    }

    def parseDateToString(def date) {
        def calendar = Calendar.getInstance(new Locale('en', 'US'))
        calendar.setTime(date)
        calendar.format('yyyy-MM-dd')
    }
}
