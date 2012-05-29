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
				<h1>จ่ายเงินกู้ให้ ${contractInstance.member}</h1>
			</header>
		</div>

		<div class="container" >
			<g:form action="doPayloan" class="form-horizontal" useToken="true">
					<g:hiddenField name="id" value="${contractInstance.id}"/>
					<div class="control-group">
						<label class="control-label">
							วันที่
						</label>
						<div class="controls">
							<g:datePicker name="payloanDate" precision="day"  value="${contractInstance?.approvalDate}"  />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">
							จำนวนเงินที่จ่าย
						</label>
						<div class="controls">
							<span class="input-xlarge uneditable-input">${contractInstance?.loanAmount}</span>
						</div>
					</div>
					<div class="form-actions">
						<button class="btn btn-primary" type="submit"><i class="icon-ok icon-white"></i> ${message(code: 'default.button.pay.label', default: 'Pay')}</button>
						<g:link controller="member" action="show" id="${contractInstance.member.id}"><g:message code="default.button.cancel.label"></g:message></g:link>
					</div>
			</g:form>
		</div>
	</body>
</html>
