<%@ page import="th.co.opendream.cashcard.Member" %>

<html>
    <head>
        <meta name="layout" content="main">
        <title>${"${memberInstance.firstname} ${memberInstance.lastname}"}</title>
    </head>
    <body>
        <header class="page-header">
            <h1>${"${memberInstance.firstname} ${memberInstance.lastname}"}</h1>
        </header>

        <div>
                <g:if test="${flash.message}">
                    <div class="message alert alert-success" role="status">${flash.message}</div>
                </g:if>
                <g:render template="toolbar" />

                <h3>ข้อมูลสมาชิก</h3>
                <table class="table table-striped table-bordered member-info">
                    <tr>
                        <td class='span2 identificationNumber'><strong><g:message code="id_card_number" /></strong></div>
                        <td class='identificationNumber'>
                            ${memberInstance?.identificationNumber}
                        </td>
                    </tr>

                    <tr>
                        <td><strong><g:message code="firstname" /></strong></div>
                        <td>
                            ${memberInstance?.firstname}
                        </td>
                    </tr>

                    <tr>
                        <td><strong><g:message code="lastname" /></strong></td>
                        <td>
                            ${memberInstance?.lastname}
                        </td>
                    </tr>

                    <tr>
                        <td><strong><g:message code="tel_no" /></strong></td>
                        <td>
                            <g:if test="${memberInstance.telNo}">
                                ${memberInstance.telNo}
                            </g:if>
                            <g:else>-</g:else>
                        </td>
                    </tr>

                    <tr>
                        <td><strong><g:message code="gender" /></strong></td>
                        <td>
                            ${message(code: 'member.label.'+memberInstance?.gender.toString().toLowerCase(), default: memberInstance?.gender.toString())}
                        </td>
                    </tr>

                    <tr>
                        <td><strong><g:message code="address" /></strong></td>
                        <td>
                            <g:if test="${memberInstance.address}">
                                ${memberInstance.address}
                            </g:if>
                            <g:else>-</g:else>
                        </td>
                    </tr>
                </table>

                <h3><g:message code="contract.list" /></h3>
                <table id="member-contract-list" class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th class="id"><g:message code="contract.list.id" /></th>
                            <th class="string"><g:message code="contract.list.type" /></th>
                            <th class="number"><g:message code="contract.list.loanAmount" /></th>
                            <th class="number"><g:message code="contract.list.loanBalance" /></th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>
                        <g:each var="contract" in="${contractList}">
                            <tr>
                                <td class="id">${contract.code}</td>
                                <td class="string">${contract.loanType.name}</td>
                                <td class="number">
                                    <g:formatNumber type="number" number="${contract.loanAmount}" maxFractionDigits="2" minFractionDigits="2" />
                                <td class="number">
                                    <g:formatNumber type="number" number="${contract.totalDebt}" maxFractionDigits="2" minFractionDigits="2" />
                                </td>
                                <td>
                                    <g:link controller="contract" action="show" id="${contract.id}" class="btn btn-info" title="${message(code: 'contract.list.view.label.title')}">
                                        <g:message code="contract.list.view.label" />
                                    </g:link>

                                    <g:if test="${!contract.approvalStatus}">
                                        <g:link controller="contract" action="approve" id="${contract.id}" class="btn btn-success">
                                            <g:message code="contract.list.approve.label" />
                                        </g:link>
                                    </g:if>
                                    <g:else>
                                        <a href="#" class="btn disabled">
                                            <g:message code="contract.show.approve.label" />
                                        </a>
                                    </g:else>

                                    <g:if test="${contract.isPayable}">
                                        <g:link controller="contract" action="payloan" id="${contract.id}" class="btn btn-warning">
                                            <g:message code="contract.list.payloan.label" />
                                        </g:link>
                                    </g:if>
                                    <g:else>
                                        <a href="#" class="btn disabled">
                                            <g:message code="contract.show.payloan.label" />
                                        </a>
                                    </g:else>

                                    <g:if test="${contract.currentPeriod}">
                                        <g:if test="${contract.loanReceiveStatus}">
                                            <g:link controller="contract" action="payoff" id="${contract.currentPeriod?.id}" class="btn btn-danger">
                                                <g:message code="contract.list.payoff.label" />
                                            </g:link>
                                        </g:if>
                                        <g:else>
                                            <a href="#" class="btn disabled">
                                                <g:message code="contract.list.payoff.label" />
                                            </a>
                                        </g:else>

                                    </g:if>
                                    <g:else>
                                        <a href="#" class="btn disabled">
                                            <g:message code="contract.list.payoff.label" />
                                        </a>
                                    </g:else>
                                </td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>


                <div class="form-actions">
                    <g:link class="btn" action="edit" id="${memberInstance.id}"><i class="icon-edit"></i> <g:message code="member.label.update" /></g:link>
                </div>

        </div>


        <r:script>
            !(function ($) {
                $('.btn-info').tipsy({gravity: 's'});
                <g:if test="${printSlip}">
                var url = '<g:createLink controller="contract" action="payoffPrintout" params ="[receiveTxId: printSlip]" />';
                var win = window.open (url,"mywindow");
                win.print();
                </g:if>

            })(jQuery);
        </r:script>

    </body>
</html>
