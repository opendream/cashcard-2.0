<div class="subnav">
    <ul class="nav nav-pills">
        <li>
        	<g:link controller="member" action="show" params="[id:memberInstance.id]">
        		<g:message code="toolbar.member.info" />
        	</g:link>
        <li>
        	<g:link controller="contract" action="selectLoanType" params="[id:memberInstance.id]">
        		<g:message code="toolbar.member.loaning" />
        	</g:link>
        </li>
	</ul>
</div>
