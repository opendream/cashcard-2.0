<html>
<head>
    <meta name="layout" content="main" />
    <title><g:message code="contract.sign.form.title" /></title>
</head>
<body>

    <header class="page-header">
        <h1><g:message code="contract.sign.form.title" /></h1>
    </header>

    <div class="container"><div class="row"><div class="span10">

        <g:hasErrors bean="${contract}">
            <div id="errors" class="alert alert-error">
                <g:renderErrors bean="${contract}" as="list"></g:renderErrors>
            </div><!-- /errors -->
        </g:hasErrors>

        <g:form class="form-horizontal" action="create" controller="contract">
            <g:hiddenField name="memberId" value="${member.id}" />
            <g:hiddenField name="loanType" value="${loanType.id}" />

            <div class="control-group">
                <label class="control-label">
                    <g:message code="contract.sign.form.loanType.label" />
                </label>
                <div class="controls">
                    <span class="input-xlarge uneditable-input">${loanType.name}</span>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label">
                    วันที่
                </label>
                <div class="controls">
                    <g:datePicker name="signedDate" precision="day"  value="${contract?.signedDate}"  />
                </div>
            </div>

            <div class="control-group ${hasErrors(bean:contract,field:'code', 'error')}">
                <label class="control-label" for="code">
                    <g:message code="contract.sign.form.code.label" />
                </label>
                <div class="controls">
                    <g:field type="text" id="code" name="code" required="true" value="${contract.code}" />
                </div>
            </div>

            <div class="control-group ${hasErrors(bean:contract,field:'loanAmount', 'error')}">
                <label class="control-label" for="loanAmount">
                    <g:message code="contract.sign.form.loanAmount.label" />
                </label>
                <div class="controls">
                    <g:field type="text" id="loanAmount" name="loanAmount" required="true" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: contract.loanAmount, format: '0.00')}" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="guarantor1">
                    <g:message code="contract.sign.form.guarantor.label" />
                </label>
                <div class="controls">

                    <div class="control-group ${hasErrors(bean:contract,field:'guarantor1', 'error')}">
                        <label for="guarantor1">
                            <g:message code="contract.sign.form.guarantor1.label" />
                        </label>
                        <input type="text" id="guarantor1" name="guarantor1" value="${contract.guarantor1}" />
                    </div>

                    <div class="control-group ${hasErrors(bean:contract,field:'guarantor2', 'error')}">
                        <label for="guarantor2">
                            <g:message code="contract.sign.form.guarantor2.label" />
                        </label>
                        <input type="text" id="guarantor1" name="guarantor2" value="${contract.guarantor2}" />
                    </div>
                </div>
            </div>

            <div class="control-group ${hasErrors(bean:contract,field:'numberOfPeriod', 'error')}">
                <label class="control-label" for="numberOfPeriod">
                    <g:message code="contract.sign.form.numberOfPeriod.label" />
                </label>
                <div class="controls">
                    <input type="text" id="numberOfPeriod" name="numberOfPeriod" value="${contract.numberOfPeriod}" required="true" />
                </div>
            </div>

            <div class="form-actions">
                <button class="btn btn-primary" type="submit">
                    <i class="icon-ok icon-white"></i> <g:message code="default.button.ok.label"></g:message>
                </button>

                <g:link action="show" controller="member" id="${member?.id}">
                    <g:message code="default.button.cancel.label"></g:message>
                </g:link>
            </div>

            <div id="preview-period">
            </div>
        </g:form>
    </div></div></div>

    <script>
        jQuery(function ($) {
            $('#numberOfPeriod, #loanAmount').change(function (e) {
                var amount = $('#loanAmount'),
                    nop = $('#numberOfPeriod')
                ;
                if (amount.val() && nop.val()) {
                    $('#preview-period').load("${createLink(action: 'preparePeriod')}", { 'amount': parseFloat(amount.val()),
                        'nop': nop.val(),
                        'loanType': "${loanType.id}"
                    });
                }
            });

            $('#guarantor1, #guarantor2').typeahead({
                property: 'name',
                source: function (typeahead, query) {
                    var dataSource = '${createLink(action: 'ajaxSearch', controller: 'member')}';
                    return $.post(dataSource, { name: query }, function (members) {
                        return typeahead.process(members);
                    });
                },
                onselect: function(member) {
                    console.log(member)
                }
            });
        });
    </script>
</body>
</html>