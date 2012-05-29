<table class="table table-condensed table-striped">
	<thead>
		<tr>
			<td>งวด</td>
			<td>จำนวนเงินที่ต้องจ่าย</td>
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