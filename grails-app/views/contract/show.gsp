<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.show.title" args="[contract.code]" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1><g:message code="contract.show.header" args="[contract.code]" /></h1>
		</header>
	</div>

	<section id="basic-info">
		<div class="row">
			<div class="span5">
				<dl class="dl-horizontal">
					<dt><g:message code="contract.show.loanType" /></dt>
					<dd>${loanType.name}</dd>

					<dt><g:message code="contract.show.interestRate" /></dt>
					<dd><g:formatNumber number="${contract.interestRate}" format="0.00" /></dd>

					<dt><g:message code="contract.show.guarantor1" /></dt>
					<dd>${contract.guarantor1}</dd>

					<dt><g:message code="contract.show.guarantor2" /></dt>
					<dd>${contract.guarantor2}</dd>

					<dt><g:message code="contract.show.signedDate" /></dt>
					<dd><g:formatDate date="${contract.dateCreated}" format="EEEE dd MMMM yyyy" /></dd>
				</dl>
			</div>

			<div class="span3 well">
				<table class="table">
					<tbody>
						<tr>
							<td><g:message code="contract.show.loanBalance" /></td>
							<td><h2><g:formatNumber number="${contract.loanBalance}" format="0.00" /></h2></td>
						</tr>
						<tr>
							<td><g:message code="contract.show.loanAmount" /></td>
							<td><h3><g:formatNumber number="${contract.loanAmount}" format="0.00" /></h3></td>
						</tr>
						<tr>
							<td><g:message code="contract.show.approvalStatus" /></td>
							<td><g:formatBoolean boolean="${contract.approvalStatus}" true="${message(code: 'contract.approvalStatus.true')}" false="${message(code: 'contract.approvalStatus.false')}" /></td>
						</tr>
						<tr>
							<td><g:message code="contract.show.loanReceiveStatus" /></td>
							<td><g:formatBoolean boolean="${contract.loanReceiveStatus}" true="${message(code: 'contract.loanReceiveStatus.true')}" false="${message(code: 'contract.loanReceiveStatus.false')}" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</section>

	<section id="period">
		<h2><g:message code="contract.show.period.header" /></h2>
		<table class="table table-condensed table-striped">
			<thead>

			</thead>

			<tbody>

			</tbody>
		</table>
	</section>

</body>
</html>