/*-- Header Resize + Show Content while scrolling*/
    $(function(){
        var shrinkHeader = 100;

  //  $('.header-logo').addClass('animated fadeIn');
   // $('.header-nav').addClass('animated fadeIn');
   $(window).scroll(function() {
        var scroll = getCurrentScroll();
        if ( scroll >= shrinkHeader ) {
         $('.navbar').addClass('shrink');
         $('.navbar-brand').addClass('l-shrink');
         $('.navbar').addClass('n-shrink');




     }
     else {
        $('.navbar').removeClass('shrink');
        $('.navbar-brand').removeClass('l-shrink');
        $('.navbar').removeClass('n-shrink');


    }

});

      $(window).scroll(function () {

        /* Check the location of each desired element */
        $('.categories_wrapper').each(function (i) {

            var bottom_of_object = $(this).position().top + $(this).outerHeight();
            var bottom_of_window = $(window).scrollTop() + $(window).height();

            /* If the object is completely visible in the window, fade it it */
            if (bottom_of_window > bottom_of_object) {

                $('.categories_wrapper').addClass('animated fadeIn');

            }

        });

    });


       function getCurrentScroll() {
        return window.pageYOffset || document.documentElement.scrollTop;
    }
});
