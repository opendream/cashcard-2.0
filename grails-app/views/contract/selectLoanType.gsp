<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.selectLoanType.title" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1><g:message code="contract.selectLoanType.title" /></h1>
		</header>
	</div>

	<div class="container">

		<ul>
			<g:each var="loanType" in="${availableLoanType}">
				<li>
					<g:link action="sign" id="${member.id}" params="[type: loanType.id]">${loanType.name}</g:link>
				</li>
			</g:each>
		</ul>

	</div>

</body>
</html>