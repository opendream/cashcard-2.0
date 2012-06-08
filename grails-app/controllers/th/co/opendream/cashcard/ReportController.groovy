package th.co.opendream.cashcard

class ReportController {

    def overdue() {
    	render(view:'overdue')
    }

    def doPaymentOverdueReport() {
        //println params
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
            println ">>${params.fromDate},, ${params.toDate}<<"
            def d1 = params.fromDate
            def d2 = params.toDate
            params._name = params.name
            params._file = params.file
            params._format = params.format
            //params.fromDate = params.fromDate.format("yyyy-MM-dd'T'HH:mm:ss'Z'")
            params.toDate = params.toDate.next()
            // fixed for demo
            params.fromDate = "$params.fromDate_year-$params.fromDate_month-$params.fromDate_day"
            params.toDate = "$params.toDate_year-$params.toDate_month-$params.toDate_day"


            println params.paymenttype
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
                    fromDate: params.fromDate,
                    toDate: params.toDate,
                    date: contract.payloanDate
                    ]
                }

                def data = payloanCollected
                def rc = ReceiveTransaction.createCriteria()
                def rtx = rc.list {
                    between('paymentDate', d1, d2)
                    /*
                    projections {
                        groupProperty("period.contract")
                    }
                    */
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
                        fromDate: params.fromDate,
                        toDate: params.toDate,
                        date: tx.paymentDate,
                        contract_code: _contract.code
                    ]
                }

                data += payoffCollected

                chain(controller:'jasper',action:'index',params:params, model: [data: data])
            }
            else {
                chain(controller:'jasper',action:'index',params:params)
            }
        } else {
            render(view:'payment', model:[type:params._file])
        }
    }

    def doDailyReport() {
        params._name = "Daily Report"
        params._file = "dailyreport"
        params._format = "PDF"


        def data = []

        data << [
                 reportType: "ชำระเงินกู้", id: 1, fromDate: '30/05/2555',
                 toDate: '30/06/2555', date: '30/06/2555',
                 member_firstname: "AAAA", member_lastname: "Last Name",
                 contract_code: "ฉ. 30", amount: 100
                ]
        data << [
                 reportType: "ชำระเงินกู้", id: 2, fromDate: '30/05/2555',
                 toDate: '30/06/2555', date: '30/06/2555',
                 member_firstname: "BBBB", member_lastname: "Last Name",
                 contract_code: "ฉ. 30", amount: 100
                ]
        data << [
                 reportType: "จ่ายเงินกู้", id: 3, fromDate: '30/05/2555',
                 toDate: '30/06/2555', date: '30/06/2555',
                 member_firstname: "CCCC", member_lastname: "Last Name",
                 contract_code: "ฉ. 30", amount: 200
                ]


        chain(controller:'jasper',action:'index',params:params, model: [data: data])
    }

    def parseDateToString(def date) {
        def calendar = Calendar.getInstance(new Locale('en', 'US'))
        calendar.setTime(date)
        calendar.format('yyyy-MM-dd')
    }
}
