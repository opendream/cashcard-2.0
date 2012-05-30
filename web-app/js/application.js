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

		var pathname = document.location.pathname,
			search = document.location.search;

		// Navbar, Not dropdown
		$('.navbar li > a[href="' + pathname + '"], .navbar li > a[href="' + pathname + search + '"]').parent().addClass('active');
		// Navbar, dropdown
		$('.navbar li.dropdown a[href="' + pathname + '"], .navbar li.dropdown a[href="' + pathname + search + '"]').parents('li.dropdown').addClass('active');
		// Subnav
		$('.subnav li > a[href="' + pathname + '"], .subnav li > a[href="' + pathname + search + '"]').parent().addClass('active');
	})(jQuery);
}