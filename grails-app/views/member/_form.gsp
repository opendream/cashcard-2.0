<%@ page import="th.co.opendream.cashcard.Member" %>



<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'identificationNumber', 'error')} required">
	<label for="identificationNumber">
		<g:message code="member.identificationNumber.label" default="Identification Number" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="identificationNumber" pattern="${memberInstance.constraints.identificationNumber.matches}" required="" value="${memberInstance?.identificationNumber}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'firstname', 'error')} required">
	<label for="firstname">
		<g:message code="member.firstname.label" default="Firstname" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="firstname" required="" value="${memberInstance?.firstname}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'lastname', 'error')} required">
	<label for="lastname">
		<g:message code="member.lastname.label" default="Lastname" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="lastname" required="" value="${memberInstance?.lastname}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'address', 'error')} required">
	<label for="address">
		<g:message code="member.address.label" default="Address" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="address" required="" value="${memberInstance?.address}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'telNo', 'error')} required">
	<label for="telNo">
		<g:message code="member.telNo.label" default="Tel No" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="telNo" pattern="${memberInstance.constraints.telNo.matches}" required="" value="${memberInstance?.telNo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'balance', 'error')} required">
	<label for="balance">
		<g:message code="member.balance.label" default="Balance" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="balance" required="" value="${fieldValue(bean: memberInstance, field: 'balance')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'gender', 'error')} required">
	<label for="gender">
		<g:message code="member.gender.label" default="Gender" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="gender" from="${th.co.opendream.cashcard.Member$Gender?.values()}" keys="${th.co.opendream.cashcard.Member$Gender.values()*.name()}" required="" value="${memberInstance?.gender?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: memberInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="member.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="status" from="${th.co.opendream.cashcard.Member$Status?.values()}" keys="${th.co.opendream.cashcard.Member$Status.values()*.name()}" required="" value="${memberInstance?.status?.name()}"/>
</div>

