<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.selectLoanType.title" /></title>
</head>
<body>

	<header class="page-header">
		<h1><g:message code="contract.selectLoanType.title" /></h1>
	</header>

	<div class="container"><div class="row"><div class="span10">

		<ul class="thumbnails" id="loan-select">
			<g:each var="loanType" in="${availableLoanType}">
				<li class="span3 thumbnail well">
					<g:link action="sign" id="${member.id}" params="[type: loanType.id]">${loanType.name}</g:link>
				</li>
			</g:each>
		</ul>

	</div></div></div>

</body>
</html>