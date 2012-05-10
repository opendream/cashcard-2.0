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

                <div class="form-actions">
                    <g:link class="btn" action="edit" id="${memberInstance.id}">แก้ไขข้อมูลสมาชิก</g:link>
                </div>

            </div>

        </body>
</html>
