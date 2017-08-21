jQuery(function ($) {
    function check_values() {
        if ($("#username").val().length != 0 && $("#password").val().length != 0) {
            $("#button1").removeClass("hidden").animate({ left: '250px' });
            $("#lock1").addClass("hidden").animate({ left: '250px' });
        }
    }
});