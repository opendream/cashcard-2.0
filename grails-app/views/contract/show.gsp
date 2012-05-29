<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.show.title" args="[contract.code]" /></title>
</head>
<body>

	<header class="page-header">
		<h1><g:message code="contract.show.header" args="[contract.code]" /></h1>
	</header>

	<section id="contract-basic-info">
		<div class="container">
			<div class="row">
				<div class="span6">
					<dl class="dl-horizontal">
						<dt><g:message code="contract.show.debtor" /></dt>
						<dd>
							<g:link controller="member" action="show" id="${contract.member.id}">${contract.member}</g:link> <i class="icon-arrow-right"></i>
						</dd>

						<dt><g:message code="contract.show.loanType" /></dt>
						<dd>${loanType.name}</dd>

						<dt><g:message code="contract.show.interestRate" /></dt>
						<dd>
							<g:set var="formattedInterestRate">
								<g:formatNumber number="${contract.interestRate}" format="0.00" />
							</g:set>
							<g:message code="contract.show.interestRateValue" args="[formattedInterestRate]" />
						</dd>

						<dt><g:message code="contract.show.guarantor1" /></dt>
						<dd>
							<g:if test="${contract.guarantor1}">
								${contract.guarantor1}
							</g:if>
							<g:else>-</g:else>
						</dd>

						<dt><g:message code="contract.show.guarantor2" /></dt>
						<dd>
							<g:if test="${contract.guarantor2}">
								${contract.guarantor2}
							</g:if>
							<g:else>-</g:else>
						</dd>

						<dt><g:message code="contract.show.signedDate" /></dt>
						<dd><g:formatDate date="${contract.signedDate}" format="EEEE dd MMMM yyyy" /></dd>
					</dl>
				</div>

				<div class="span4">
					<table class="table">
						<tbody>
							<tr>
								<td><g:message code="contract.show.totalDebt" /></td>
								<td><h2 class="pull-right"><g:formatNumber number="${totalDebt}" format="0.00" /></h2></td>
							</tr>
							<tr>
								<td><g:message code="contract.show.loanAmount" /></td>
								<td><h3 class="pull-right"><g:formatNumber number="${contract.loanAmount}" format="0.00" /></h3></td>
							</tr>
							<tr>
								<td><g:message code="contract.show.approvalStatus" /></td>
								<td>
									<div class="pull-right">
										<g:formatBoolean boolean="${contract.approvalStatus}" true="${message(code: 'contract.approvalStatus.true')}" false="${message(code: 'contract.approvalStatus.false')}" />
										<g:if test="${contract.approvalStatus}">
											<g:formatDate date="${contract.approvalDate}" format="dd MMM yyyy" />
										</g:if>
									</div>
								</td>
							</tr>
							<tr>
								<td><g:message code="contract.show.loanReceiveStatus" /></td>
								<td>
									<div class="pull-right">
										<g:formatBoolean boolean="${contract.loanReceiveStatus}" true="${message(code: 'contract.loanReceiveStatus.true')}" false="${message(code: 'contract.loanReceiveStatus.false')}" />
										<g:if test="${contract.loanReceiveStatus}">
											<g:formatDate date="${contract.payloanDate}" format="dd MMM yyyy" />
										</g:if>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</section>

	<section id="period">
		<h2><g:message code="contract.show.period.header" /></h2>
		<hr />
		<table class="table table-condensed table-striped">
			<thead>
				<tr>
					<td><g:message code="contract.show.period.thead.no" /></td>
					<td><g:message code="contract.show.period.thead.amount" /></td>
					<g:if test="${contract.approvalStatus}">
						<td><g:message code="contract.show.period.thead.dueDate" /></td>
						<td><g:message code="contract.show.period.thead.payoffStatus" /></td>
						<td><g:message code="contract.show.period.thead.payoffDate" /></td>
					</g:if>
				</tr>
			</thead>

			<tbody>
				<g:each var="period" in="${periodList}">
					<tr>
						<td>${period.no}</td>
						<td>${period.amount}</td>
						<g:if test="${contract.approvalStatus}">
							<td><g:formatDate date="${period.dueDate}" format="EEEE dd MMMM yyyy" /></td>
							<td>${period.payoffStatusText}</td>
							<td>
								<g:if test="${period.payoffStatus}">
									<g:formatDate date="${period.payoffDate}" format="EEEE dd MMMM yyyy" />
								</g:if>
								<g:else>-</g:else>
							</td>
						</g:if>
					</tr>
				</g:each>
			</tbody>
		</table>
	</section>

</body>
</html>