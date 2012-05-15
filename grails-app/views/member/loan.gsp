<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="member.loan" /></title>
</head>
<body>

	<div class="container">
		<header class="page-header">
			<h1><g:message code="member.loan" /></h1>
		</header>
	</div>

	<div class="container">

		<ul>
			<g:each var="loanType" in="${availableLoanType}">
				<li><g:link action="doLoan" id="${member.id}" params="[type: loanType.id]">${loanType.name}</g:link></li>
			</g:each>
		</ul>

	</div>

</body>
</html>