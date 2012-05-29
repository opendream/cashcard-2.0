<%@ page import="th.co.opendream.cashcard.Contract" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'interestRate.label', default: 'InterestRate')}" />
		<title><g:message code="interestRate.create.title"></g:message></title>
	</head>
	<body>
		<div class="container">
			<header class="page-header">
				<h1>อนุมัติเงินกู้ของ ${contractInstance.member}</h1>
			</header>
		</div>

		<div class="container" >
			<g:form action="doApprove" class="form-horizontal" useToken="true">
					<g:hiddenField name="id" value="${contractInstance.id}"/>
					<div class="controls">
						<g:datePicker name="approvalDate" precision="day"  value="${contractInstance?.approvalDate}"  />
					</div>
					<div class="form-actions">
						<button class="btn btn-primary" type="submit"><i class="icon-ok icon-white"></i> ${message(code: 'default.button.approve.label', default: 'Approve')}</button>
						<g:link action="list"><g:message code="default.button.cancel.label"></g:message></g:link>
					</div>
			</g:form>
		</div>
	</body>
</html>
