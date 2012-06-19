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
            <table class="printout" style="float:left" width="351px">
                <tr class="subtitle">
                    <td width="2cm"></td>
                    <td width="80px" class="stringlabel"></td>
                    <td width="95px">  </td>
                    <td width="90px"><label>${printout.code}</label></td>
                </tr>
                <tr class="subtitle">
                    <td></td>
                    <td class="stringlabel"></td>
                    <td></td>
                    <td class="string"><label>${formatDate(date: printout.paymentDate, format: 'dd/MM/yyyy')}</label>
                </tr>
                <tr class="title">
                    <td class="stringlabel"></td>
                    <td class="string" colspan="3">
                        <label>${printout.member}</label>
                        <label>${printout.creditUnionMemberNo?: "000-1000"}</label>
                    </td>
                </tr>
                <tr><td>&nbsp;</td><tr>
                <tr class="subtitle">
                    <td class="stringlabel"><label>ประเภท</label></td>
                    <td class="string" width="10cm"><label>${printout.loanType}</label>
                    <td class="stringlabel"><label>สัญญา</label></td>
                    <td class="string"><label>${printout.contractCode}</label></td>
                    <td></td>
                </tr>
                <tr class="subtitle">
                    <td class="stringlabel"><label>งวด</label></td>
                    <td class="string"><label>${printout.periodNo}</label></td>
                    <td class="stringlabel"><label>จำนวนเงิน</label></td>
                    <td class="string"><label>${printout.periodAmount}</label></td>
                </tr>
                <tr>
                    <td class="stringlabel"><label>ชำระ</label></td>
                    <td class="number"><label>${formatNumber(number: printout.amount, format: '#,##0.00')}</label>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"><label>ค่าปรับ</label></td>
                    <td class="number"><label>${formatNumber(number: printout?.find, format: '#,##0.00')?: '-'}</label>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"></td>
                    <td class="number"><label>${formatNumber(number: printout?.totalamount, format: '#,##0.00')}</label>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr>
                    <td class="string" colspan=2 style="text-align: center;"><label>(${printout.username})</label></td>
                    <td class="stringlabel"></td>
                    <td class="string"></td>
                </tr>
                <tr>
                    <td class="string" colspan=2 style="text-align: center;"><label>(${printout.username})</label></td>

                    <td class="stringlabel"></td>
                    <td class="string"></td>
                </tr>
            </table>

            <table class="printout" style="float:left; margin-left: 6mm" width="351px">
                <tr class="subtitle">
                    <td width="2cm"></td>
                    <td width="80px" class="stringlabel"></td>
                    <td width="80px"> </td>
                    <td><label>${printout.code}</label></td>
                </tr>
                <tr class="subtitle">
                    <td></td>
                    <td class="stringlabel"></td>
                    <td></td>
                    <td class="string"><label>${formatDate(date: printout.paymentDate, format: 'dd/MM/yyyy')}</label>
                </tr>
                <tr class="title" height=5cm>
                    <td class="stringlabel"></td>
                    <td class="string" colspan="3">
                        <label>${printout.member}</label>
                        <label>${printout.creditUnionMemberNo?: "000-1000"}</label>
                    </td>
                </tr>
                <tr><td>&nbsp;</td><tr>
                <tr class="subtitle">
                    <td class="stringlabel" style="text-align: right;"><label>ประเภท</label></td>
                    <td class="string" width="10cm"><label>${printout.loanType}</label>
                    <td class="stringlabel" style="text-align: right;"><label>สัญญา</label></td>
                    <td class="string"><label>${printout.contractCode}</label></td>
                    <td></td>
                </tr>
                <tr class="subtitle">
                    <td class="stringlabel" style="text-align: right;" ><label>งวด</label></td>
                    <td class="string"><label>${printout.periodNo}</label>
                    <td class="stringlabel"><label>จำนวนเงิน</label></td>
                    <td class="string"><label>${printout.periodAmount}</label></td>
                </tr>
                <tr>
                    <td class="stringlabel" style="text-align: right;"><label>ชำระ</label></td>
                    <td class="number"><label>${formatNumber(number: printout.amount, format: '#,##0.00')}</label>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"><label>ค่าปรับ</label></td>
                    <td class="number"><label>${formatNumber(number: printout?.find, format: '#,##0.00')?: '-'}</label>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="stringlabel"></td>
                    <td class="number"><label>${formatNumber(number: printout?.totalamount, format: '#,##0.00')}</label>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>
                <tr class="counter"><td colspan=5>&nbsp;</td></tr>

                <tr>
                    <td class="string" colspan=2 style="text-align: center;"><label>(${printout.username})</label></td>
                    <td class="stringlabel"></td>
                    <td class="string"></td>
                </tr>
                <tr>
                    <td class="string" colspan=2 style="text-align: center;"><label>(${printout.username})</label></td>
                    <td class="stringlabel"></td>
                    <td class="string"></td>
                </tr>
            </table>
        </div>
        <div class="print noprint" style="clear:both"><a href="javascript:window.print();" class="string">Print</a></div>
    </body>
</html>