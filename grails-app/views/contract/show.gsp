<html>
<head>
	<meta name="layout" content="main" />
	<title><g:message code="contract.show.title" args="[contract.code]" /></title>
</head>
<body>

	<header class="page-header">
		<h1><g:message code="contract.show.header" args="[contract.code]" /></h1>
	</header>

	<g:if test="${flash.error}">
        <div id="errors" class="alert alert-error">
          ${flash.error}
        </div><!-- /errors -->
    </g:if>

    <g:if test="${flash.message}">
        <div id="messages" class="alert alert-success">
          ${flash.message}
        </div><!-- /errors -->
    </g:if>

	<section id="contract-basic-info">
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
						<td><h2 class="pull-right">
                        	<g:formatNumber type="number" number="${totalDebt}" maxFractionDigits="2" minFractionDigits="2" />
						</h2></td>
					</tr>
					<tr>
						<td><g:message code="contract.show.loanAmount" /></td>
						<td><h3 class="pull-right">
							<g:formatNumber type="number" number="${contract.loanAmount}" maxFractionDigits="2" minFractionDigits="2" />
						</h3></td>
					</tr>
					<tr>
						<td><g:message code="contract.show.approvalStatus" /></td>
						<td>
							<div class="pull-right">
								<g:formatBoolean boolean="${contract.approvalStatus}" true="${message(code: 'contract.approvalStatus.true')}" false="${message(code: 'contract.approvalStatus.false')}" />
								<g:if test="${contract.approvalStatus}">
									<br /><g:formatDate date="${contract.approvalDate}" format="dd MMM yyyy" />
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
									<br /><g:formatDate date="${contract.payloanDate}" format="dd MMM yyyy" />
								</g:if>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="form-actions">
				<g:if test="${!contract.approvalStatus}">
                    <g:link controller="contract" action="approve" id="${contract.id}" class="btn btn-success span1">
                    	<g:message code="contract.show.approve.label" />
                    </g:link>
                </g:if>
                <g:else>
                	<a href="#" class="btn span1 disabled">
                    	<g:message code="contract.show.approve.label" />
                    </a>
                </g:else>
                <g:if test="${contract.isPayable}">
                    <g:link controller="contract" action="payloan" id="${contract.id}" class="btn btn-warning span1">
                    	<g:message code="contract.show.payloan.label" />
                    </g:link>
                </g:if>
                <g:else>
                	<a href="#" class="btn span1 disabled">
                    	<g:message code="contract.show.payloan.label" />
                    </a>
                </g:else>
            </div>
		</div>
	</section>

	<section id="period">
		<h2>
			<g:message code="contract.show.period.header" />

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
		</h2>
		<hr />
		<table class="table table-condensed">
			<thead>
				<tr>

					<th class="string" rowspan="2"><g:message code="contract.show.period.thead.no" /></th>
					<th><g:message code="contract.show.period.thead.amount" /></th>
					<g:if test="${contract.approvalStatus}">
						<th><g:message code="contract.show.period.thead.dueDate" /></th>
						<th><g:message code="contract.show.period.thead.payoffStatus" /></th>
						<th><g:message code="contract.show.period.thead.payoffDate" /></th>
					</g:if>
				</tr>
			</thead>

			<tbody>
				<g:each var="period" in="${periodList}">
					<tr>
						<g:if test="${contract.loanReceiveStatus}">
							<td class='span2 period-no' rowspan="2">${period.no}</td>
						</g:if>
						<g:else>
							<td class='span2'>${period.no}</td>
						</g:else>
						<td class="date">
							<span class="label label-inverse label-amount">
								<g:formatNumber type="number" number="${period.amount}" maxFractionDigits="2" minFractionDigits="2" />
							</span>
						</td>
						<g:if test="${contract.approvalStatus}">
							<td><g:formatDate date="${period.dueDate}" format="EEEE d MMMM yyyy" /></td>
							<td class="date">${period.payoffStatusText}</td>
							<td>
								<g:if test="${period.payoffStatus}">
									<g:formatDate date="${period.payoffDate}" format="EEEE dd MMMM yyyy" />
								</g:if>
								<g:else>-</g:else>
							</td>
						</g:if>
					</tr>

					<g:if test="${contract.loanReceiveStatus}">
						<tr class="period-transaction-row">
							<td colspan="5">
								<!-- receive transaction details -->
								<div class="period-transaction">
									<h4>
										<span class="expand-tx-list"> + </span>
										<g:message code="contract.show.receiveTx.header" />
									</h4>
									<table class="table table-striped hide">
										<thead>
											<tr>
												<td><g:message code="contract.show.receiveTx.paymentDate" /></td>
												<td class="number"><g:message code="contract.show.receiveTx.amount" /></td>
												<td></td>
											</tr>
										</thead>
										<tbody>
											<g:if test="${period.effectiveReceiveTransaction}">
												<g:each var="rtx" in="${period.effectiveReceiveTransaction}">
												<tr>
													<td><g:formatDate date="${rtx.paymentDate}" format="EE dd MMM yyyy" /></td>
													<td class="number">${rtx.amount}</td>
													<td class="date">
														<g:link controller="receiveTransaction" action="cancel" id="${rtx.id}" class="btn cancel-btn">
									                        <g:message code="contract.show.receiveTx.cancel" />
									                    </g:link>
									                </td>
												</tr>
												</g:each>
											</g:if>
											<g:else>
												<tr>
													<td colspan="3"><g:message code="contract.show.receiveTx.noTransaction" /></td>
												</tr>
											</g:else>
										</tbody>
									</table>
								</div>
								<!-- /receive transaction details -->
							</td>
						</tr>
					</g:if>
					</tr>
				</g:each>
			</tbody>
		</table>
	</section>

	<r:script>
	!function ($) {
		var context = $('.period-transaction');

		$('h4', context).click(function (e) {
			var h = $(this);

			$('table', $(this).parent()).slideToggle('fast');

			var sign = $('.expand-tx-list', h),
				current_sign = sign.text()
			;

			sign.text( current_sign == ' + ' ? ' - ' : ' + ' );
		});

		$('.btn', context).hide();
		$('.btn', context).eq(-1).show();

		// Bind cancel btn
		$('.cancel-btn').click(function (e) {
			var ans = confirm("<g:message code="default.button.delete.confirm.message" />");
			return ans;
		});
	}(jQuery);
	</r:script>

</body>
</html>