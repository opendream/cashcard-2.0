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

		var pathname = document.location.pathname
		// Navbar, Not dropdown
		$('.navbar li > a[href="' + pathname + '"]').parent().addClass('active');
		// Navbar, dropdown
		$('.navbar li.dropdown a[href="' + pathname + '"]').parents('li.dropdown').addClass('active');
		// Subnav
		$('.subnav li > a[href="' + pathname + '"]').parent().addClass('active');
	})(jQuery);
}