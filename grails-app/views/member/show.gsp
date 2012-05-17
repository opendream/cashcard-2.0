<%@ page import="th.co.opendream.cashcard.Member" %>

<html>
    <head>
        <meta name="layout" content="main">
        <title>${"${memberInstance.firstname} ${memberInstance.lastname}"}</title>
    </head>
    <body>
        <div class="container">
            <header class="page-header">
                <h1>${"${memberInstance.firstname} ${memberInstance.lastname}"}</h1>
            </header>
        </div>

        <div class="container">
                <g:if test="${flash.message}">
                    <div class="message alert alert-success" role="status">${flash.message}</div>
                </g:if>
                <g:render template="toolbar" />

                <h3>ข้อมูลสมาชิก</h3>
                <table class="table table-striped table-bordered">
                    <tr>
                        <td><strong><g:message code="id_card_number" /></strong></div>
                        <td>
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
                <table class="table table-striped table-bordered">
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
                                    <g:formatNumber type="number" number="${contract.loanBalance}" maxFractionDigits="2" minFractionDigits="2" />
                                </td>
                                <td>
                                    <g:link controller="contract" action="show" id="${contract.id}">
                                        <span class="label label-info"><g:message code="contract.list.view.label" /></span>
                                    </g:link>
                                    <g:link controller="contract" action="approve" id="${contract.id}">
                                        <span class="label label-info"><g:message code="contract.list.approve.label" /></span>
                                    </g:link>
                                    <g:link controller="contract" action="payloan" id="${contract.id}">
                                        <span class="label label-info"><g:message code="contract.list.payloan.label" /></span>
                                    </g:link>
                                </td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>


                <div class="form-actions">
                    <g:link class="btn" action="edit" id="${memberInstance.id}">แก้ไขข้อมูลสมาชิก</g:link>
                </div>

            </div>

        </body>
</html>
