<!doctype html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <link rel="SHORTCUT ICON" href="${resource(dir: 'images', file: 'credit-union-logo-blue.png')}" />
        <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'credit-union-logo.png')}">
        <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'credit-union-logo.png')}">


        <link rel="stylesheet" href="${resource(dir: 'css', file: 'cashcard.css')}" media="screen"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'print.css')}" type="text/css" media="print">

    </head>
    <body class="printoutbody">
        <div>
            <table class="printout">
                <tr class="subtitle">
                    <td width="125"></td>
                    <td width="125"></td>
                    <td width="125"></td>
                    <td width="125" class="stringlabel"><label>เลขที่</label></td>
                    <td width="125" class="string"><label>${printout.code}</label>
                    </td>
                </tr>
                <tr class="subtitle">
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="stringlabel"><label>วันที่</label></td>
                    <td class="string"><label>${formatDate(date: printout.paymentDate, format: 'dd/MM/yyyy')}</label>
                    </td>
                </tr>
                <tr class="title">
                    <td class="stringlabel">ได้รับเงินจาก</td>
                    <td class="string"><label>${printout.member}</label></td>
                    <td class="stringlabel"><label>หมายเลขสมาชิก</label></td>
                    <td class="string"><label>${printout.identificationNumber}</label></td>

                    <td></td>
                </tr>
                <tr class="subtitle">
                    <td class="stringlabel"><label>ประเภทเงินกู้</label></td>
                    <td class="string"><label>${printout.loanType}</label>
                    <td class="stringlabel"><label>หมายเลขสัญญา</label></td>
                    <td class="string"><label>${printout.contractCode}</label></td>
                    </td>
                    <td></td>
                </tr>
                <tr class="subtitle">
                    <td class="stringlabel"><label>งวด</label></td>
                    <td class="string"><label>${printout.periodNo}</label>
                    <td class="stringlabel"><label>จำนวนเงิน</label></td>
                    <td class="string"><label>${printout.periodAmount}</label></td>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"><label>ชำระ</label></td>
                    <td class="number"><label>${formatNumber(number: printout.amount, format: '#,##0.00')}</label>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"><label>ค่าปรับ</label></td>
                    <td class="number"><label>${formatNumber(number: printout?.find, format: '#,##0.00')}</label>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"><label></label></td>
                    <td class="number"><label>${formatNumber(number: printout?.totalamount, format: '#,##0.00')}</label>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr>
                    <td class="stringlabel"><label>ผู้ทำรายการ</label></td>
                    <td class="string"><label>(${printout.username})</label></td>
                    <td class="stringlabel"><label>ผู้ชำระเงิน</label></td>
                    <td class="string"></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td class="string"><label>(${printout.username})</label>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>
        <div class="print noprint"><a href="javascript:window.print();" class="string">Print</a></div>
    </body>
</html>