

$(document).ready(function() {
	console.log ("foo");
	
	$('#target').fadeOut('normal');
	
//	$('.OFF_USER').click(function() {
//		console.log ("OFF_USER");
//	    $('#target').fadeOut('normal');
//	});
	
	$('.ON_USER').click(function() {
	    $('#target').fadeIn('ON_USER');
	});
});

jQuery(function ($) {
    $('#rate1').shieldRating({
        max: 7,
        step: 0.1,
        value: 6.3,
        markPreset: false
    });
    $('#rate2').shieldRating({
        max: 7,
        step: 0.1,
        value: 6,
        markPreset: false
    });
    $('#rate3').shieldRating({
        max: 7,
        step: 0.1,
        value: 3,
        markPreset: false
    });
    $('#rate4').shieldRating({
        max: 7,
        step: 0.1,
        value: 5,
        markPreset: false
    });
    $('#rate5').shieldRating({
        max: 7,
        step: 0.1,
        value: 5.7,
        markPreset: false
    });
});