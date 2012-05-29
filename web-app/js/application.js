if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);

	(function ($) {
		$('a.disabled').click(function (e) {
			e.preventDefault();
		});
	})(jQuery);
}