<table class="table table-condensed table-striped">
	<thead>
		<tr>
			<td>No.</td>
			<td>Amount</td>
		</tr>
	</thead>

	<tbody>
		<g:each var="period" in="${periodList}">
			<tr>
				<td>${period.no}</td>
				<td>${period.amount}</td>
			</tr>
		</g:each>
	</tbody>
</table>