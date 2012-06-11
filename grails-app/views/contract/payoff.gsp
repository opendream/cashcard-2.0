<!doctype html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="contract.payoff.title" args="[period.contract.code, period.no]" /></title>
    </head>
    <body>
        <header class="page-header">
            <p>
                <g:link controller="member" action="show" id="${member.id}">
                    <em><i class="icon-arrow-left"></i><g:message code="back.to.member" args="[period.contract.member]" /></em>
                </g:link>
            </p>
            <h1><g:message code="contract.payoff.title" args="[period.contract.code, period.no]" /></h1>
        </header>

        <g:if test="${flash.error}">
          <div id="errors" class="alert alert-error">
              ${flash.error}
          </div><!-- /errors -->
        </g:if>

        <div class="container" >
            <div class="row">
                <div class="span10">
                    <g:form action="doPayoff" class="form-horizontal" useToken="true">
                        <g:hiddenField name="id" value="${period.id}" />

                        <div class="control-group">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.dueDate.label" />
                            </label>
                            <div class="controls">
                                <span class="input-xlarge uneditable-input">
                                    <g:formatDate date="${period.dueDate}" format="EEEE dd MMMM yyyy" />
                                </span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.paymentDate.label" />
                            </label>
                            <div class="controls">
                                <g:datePicker name="paymentDate" precision="day"  value="${receiveTx?.paymentDate}"  />
                            </div>
                        </div>


                        <div class="control-group ${hasErrors(bean:period,field:'isShareCapital', 'error')}">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.payAll.label" />
                            </label>
                            <div class="controls">
                                <label class="checkbox payAll">
                                    <g:field type="checkbox" id="payAll" name="payAll" value="${receiveTx?.isShareCapital}" />
                                    <g:message code="contract.payoff.form.payAll.checkbox.label" />
                                </label>
                            </div>
                        </div>

                        <div class="control-group ${hasErrors(bean:period,field:'amount', 'error')}">
                            <label class="control-label loanAmount">
                                <g:message code="contract.payoff.form.amount.label" />
                            </label>
                            <div class="controls payAmount">
                                <g:field type="text" id="payAmount" name="payAmount" required="true" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: period.outstanding, format: '0.00')}" />
                                <span class="input-xlarge uneditable-input payAmount" style="display:none;">
                                    ${contract.loanAmount}
                                </span>
                            </div>
                        </div>

                        <div style="display:none" class="control-group interest ${hasErrors(bean:period,field:'fine', 'error')}">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.interest.label" />
                            </label>
                            <div class="controls">
                                <span class="input-xlarge uneditable-input">${contract.currentInterest}</span>
                            </div>
                        </div>

                        <div style="display:none" class="control-group totalDebt ${hasErrors(bean:period,field:'fine', 'error')}">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.totalDebt.label" />
                            </label>
                            <div class="controls">
                                <span class="input-xlarge uneditable-input">${contract.totalDebt}</span>
                            </div>
                        </div>

                        <div class="control-group fine ${hasErrors(bean:period,field:'fine', 'error')}">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.fine.label" />
                            </label>
                            <div class="controls">
                                <g:field type="text" id="fine" name="fine" pattern="\\d*(\\.\\d\\d)?" value="${formatNumber(number: receiveTx?.fine, format: '0.00')}" />
                            </div>
                        </div>


                        <div class="control-group ${hasErrors(bean:period,field:'isShareCapital', 'error')}">
                            <label class="control-label">
                                <g:message code="contract.payoff.form.isShareCapital.label" />
                            </label>
                            <div class="controls">
                                <label class="checkbox">
                                    <g:field type="checkbox" id="isShareCapital" name="isShareCapital" value="${receiveTx?.isShareCapital}" />
                                    <g:message code="contract.payoff.form.isShareCapital.checkbox.label" />
                                </label>
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
                    </g:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            jQuery(function($) {
                // cache selected elements
                var inputBox = $('.controls.payAmount input');
                var innerSpan = $('.controls.payAmount span');
                var controlGroup =  $('.control-group.totalDebt, .control-group.interest');
                var labelLoanAmount = $('.control-label.loanAmount');

                // bind event
                $('input#payAll').change(function(e){
                    var checked = $(this).attr('checked');
                    var msg = '';
                    labelLoanAmount.hide();
                    if (checked) {
                      msg = "จำนวนเงินที่กู้";
                      // DisAppear
                      labelLoanAmount.text(msg);
                      inputBox.hide();

                      // WillDidAppear
                      innerSpan.fadeIn();
                      labelLoanAmount.fadeIn();
                      controlGroup.slideDown();
                    }
                    else {
                      msg = '<g:message code="contract.payoff.form.amount.label" />';
                      // Dissappear
                      labelLoanAmount.text(msg);
                      innerSpan.hide();

                      // WillDidAppear
                      inputBox.fadeIn();
                      labelLoanAmount.fadeIn();
                      controlGroup.slideUp();
                    }
                });
            });
        </script>
    </body>
</html>
