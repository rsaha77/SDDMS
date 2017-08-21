//$(function () {
//	$('#orderDate').datepicker({
//		dateFormat: 'yy-mm-dd'
//	});
//});

//$(function() {
//    $( "#client-details-for-order-modal" ).draggable();
//    $( "#product-details-for-order-modal" ).draggable();
//    $( "#createEventForTheDay-modal" ).draggable();
//    $( "#order-process-modal" ).draggable();
//    $( "#client-details-modal" ).draggable();
//    $( "#decision-modal" ).draggable();
//});

document.getElementById ("view-products-button2").onclick = function () {
	console.log (leadId);
	var win = window.open ("productmanagement", '_blank');
};


document.getElementById ("view-products-button3").onclick = function () {
	var win = window.open ("productmanagement", '_blank');
};


document.getElementById ("view-products-button5").onclick = function () {
	var win = window.open ("productmanagement", '_blank');
};


var clientIdToDisqualifyLead = "ZZZZ";
document.getElementById ("disqualify-lead-button2").onclick = function () {
	swal ({	title: "Are you sure?",
			text: "The lead will be added to special attention list!",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, Disqualify Lead!",   
			closeOnConfirm: false 
	 	}, 
		
		function() { 
			// Hide the modal
			$('#lead-details-modal2').modal('hide');
			
			// Insert data in disqualifiedLeads AND Delete Event From Calendar
			
			console.log ("Disqualify the Lead: ", clientIdToDisqualifyLead);
			$.ajax({
				url : "disqualifyLeadAndDeleteFromCal?clientId="+clientIdToDisqualifyLead,
				type : 'POST',
				xhrFields: {
			      withCredentials: true
			   }
			}).done(function() {
				swal ("Lead Disqualified !", "The selected Lead has been moved to special attention list.", "success");
				// Re-fetch Calendar
				$('#calendar').fullCalendar ('refetchEvents');
			});
		}
	);
};


var leadId_2;
var leadName_2;;


function fillLeadDetailsForLeadId (clientId) {
	$.ajax ({
		url : "getLeadForLeadId?clientId="+clientId,
		type : 'POST',
		contentType : "application/json",
	}).done (function(data) {
		console.log ("Done!");
		console.log (data);
		$("#displayLeadId2").html(data.clientId);
		leadId_2 = data.clientId;
		$("#displayLeadName2").html(data.leadName);
		leadName_2 = data.leadName;
		$("#displayLeadType2").html(data.leadType);
		$("#displayLeadRegion2").html(data.leadRegion);
		$("#displayLeadPhone2").html(data.leadContact);
		$("#displayLeadEmail2").html(data.leadEmail);
		$("#displayLeadAddress2").html(data.leadAddress);
	});
}


function getCurrentDateAndTime () {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	var HH = today.getHours();
	var MM = today.getMinutes();
	
	if(dd<10) {
	    dd='0'+dd
	} 

	if(mm<10) {
	    mm='0'+mm
	}
	
	if(HH<10) {
		HH='0'+HH;
	}
	
	if(MM<10) {
		MM='0'+MM;
	}

	today = yyyy + '-' + mm + '-' + dd + ' ' + HH + ':' + MM;
	return today;
}

function fillDecisionPanel (orderId) {
	console.log ("Ajax , orderId ", orderId)
	$.ajax ({
		url : "order/getOrderDetailsForOrderId?orderId="+orderId,
		type : 'POST',
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		console.log ("Done!");
		console.log (data);
		$("#displayOrderId").html(data.orderId);
		$("#displayDateOfDelivery").html(data.dateOfDelivery);
		$("#displayClientName8").html(data.clientName);
		$("#displayClientType8").html(data.clientType);
		$("#displayClientTransport8").html(data.clientTransport);
		$("#displayClientPhone8").html(data.clientPhone);
		$("#displayClientEmail8").html(data.clientEmail);
		$("#displayClientAddress8").html(data.clientAddress);
		console.log (data.items)
	});
}



function fillClientDetailsForOrderId (orderId) {
	$.ajax ({
		url : "clientmanagement/getClientForOrderId?orderId="+orderId,
		type : 'POST',
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		console.log ("Done!");
		console.log (data);
		
		// Client details for Order Reminder
		$("#displayClientName8").html(data.name);
		$("#displayClientPhone8").html(data.contact);
		$("#displayClientEmail8").html(data.email);
		$("#displayClientAddress8").html(data.address);
		$("#displayClientType8").html(data.type);
		$("#displayClientTransport8").html(data.transport);
	});
}


function fillProductDetailsForOrderId (orderId) {
	$.ajax ({
		url : "productmanagement/getProductForOrderId?orderId="+orderId,
		type : 'POST',
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		console.log ("Done!");
		console.log (data);
		
		// Client details for Order Reminder
		$("#displayProductId2").html(data.productId);
		$("#displayProductName2").html(data.name);
		$("#displayProductPrice2").html(data.price);
		$("#displayProductType2").html(data.type);
	});
}


var clientId_2;
var clientName_2;

function fillClientDetailsForClientId (clientId) {
	$.ajax ({
		url : "clientmanagement/getClientForClientId?clientId="+clientId,
		type : 'POST',
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		console.log ("Done!");
		console.log (data);
		
		// Client details for Order Reminder
		$("#displayClientName2").html(data.name);
		clientName_2 = data.name;
		$("#displayClientPhone2").html(data.contact);
		$("#displayClientEmail2").html(data.email);
		$("#displayClientAddress2").html(data.address);
		$("#displayClientType2").html(data.type);
		$("#displayClientTransport2").html(data.transport);
	});
}



function fillDisqDetailsForClientId (clientId) {
	$.ajax ({
		url : "clientmanagement/getDisqForClientId?clientId="+clientId,
		type : 'POST',
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		console.log ("Done!");
		console.log (data);
		$("#displayClientName5").html(data.disLeadName);
		$("#displayClientPhone5").html(data.disLeadContact);
		$("#displayClientEmail5").html(data.disLeadEmail);
		$("#displayClientAddress5").html(data.disLeadAddress);
		$("#displayClientType5").html(data.disLeadType);
		$("#displayClientTransport5").html(data.disLeadTransport);
	});
}


var ORDER_ID;

$(document).ready(function() {
	
	/* _________________________________________________CALENDAR (II) ___________________________________________________________ */
	
    $('#calendar2').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		
	    events: function (start, end, timezone,	 callback) {
	    	$.ajax ({
	    		url : 'calendar/getEventsForCalendar',
	    		type : 'POST',
	    		contentType : "application/json",
	    	}).done (function(data) {
	    		var events = [];
	    		for (i = 0; i < data.length; ++i) {
	    			var bgColour;
	    			if (data [i].title [0] == 'P') {
	    				bgColour = "#0000ff";
	    			} else if (data [i].title.indexOf ("Lead") > -1) {
	    				bgColour = "#A00000";
	    			} else {
	    				bgColour = "#c7c700";
	    			}
	    			events.push ({
	    				title: data [i].title,
	    				start: data [i].start,
	    				orderId: data [i].orderId,
	    				clientId: data [i].clientId,
	    				backgroundColor: bgColour
	    			});
	    	    }
	    		callback(events);
	    	});
	    },
	    dayClick: function (date, allDay, jsEvent, view) {
            $('#calendar2').fullCalendar('gotoDate', date);
            $('#calendar2').fullCalendar('changeView', 'agendaDay');
	        
            console.log ("date : ", date.format ('YYYY-MM-DD H:mm'));
            $('#leadDealingDate').val (date.format ('YYYY-MM-DD H:mm'));
        },
	    
	    color: 'yellow',   // an option!
	    textColor: 'black', // an option!
    })
    // Calendar2 Ends
    
    
    $('#set-reminder-for-lead-modal').on('shown.bs.modal',function(){
    	$('#calendar2').fullCalendar('render');
    })
    
    
    
    /* _________________________________________________CALENDAR (I) ___________________________________________________________ */
    
     $('#calendar').fullCalendar({
//    	theme: true,
    	draggable: true,
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		
		/*
		events: [
			{
				title: 'Contact client FP01',
				start: '2015-12-07 13:00',
				end: '2015-12-07 15:00',
				url: 'clientmanagement'
			},
			{
				title: 'Process Order 81',
				start: '2015-12-07',
				url: 'orders'
			}
		],
		 */
		
	    events: function (start, end, timezone,	 callback) {
	    	$.ajax ({
	    		url : 'calendar/getEventsForCalendar',
	    		type : 'POST',
	    		contentType : "application/json",
	    	}).done (function(data) {
	    		var events = [];
	    		for (i = 0; i < data.length; ++i) {
	    			var bgColour;
	    			if (data [i].title [0] == 'P') {
	    				bgColour = "#0000ff";
	    			} else if (data [i].title.indexOf ("Lead") > -1) {
	    				bgColour = "#A00000";
	    			} else {
	    				bgColour = "#c7c700";
	    			}
	    			events.push ({
	    				title: data [i].title,
	    				start: data [i].start,
	    				orderId: data [i].orderId,
	    				clientId: data [i].clientId,
	    				backgroundColor: bgColour
	    			});
	    	    }
	    		callback(events);
	    	});
	    },
	    
	    eventRender: function (event, element) {
	    	// This function is called for each event
	    	if (event.title[0] == 'P') {
	    		element.click (function () {
	    			ORDER_ID = event.orderId;
	    			fillDecisionPanel (event.orderId);
//	    			fillClientDetailsForOrderId (event.orderId);
//	    			fillProductDetailsForOrderId (event.orderId);
	    			$("#decision-modal").modal ("show");
	    		});
	    	} else if (event.title.indexOf ("Lead") > -1) {
    			element.click (function () {
	    			console.log ("Hi Lead");
	    			fillLeadDetailsForLeadId (event.clientId);
	    			$("#lead-details-modal2").modal ("show"); // Open Lead Details Modal (Special attention to contact)
	    			clientIdToDisqualifyLead = event.clientId;
    			});
	    	} else if (event.title.indexOf ("Disqualified") > -1) {
	    		element.click (function () {
	    			console.log ("Hi Disq: ", event.clientId);
	    			fillDisqDetailsForClientId (event.clientId);
	    			$("#disq-details-modal").modal ("show"); // Open Client Details Modal (Special attention to contact)
    			});
	    	} else if (event.title.indexOf ("Client") > -1) {
	    		element.click (function () {
	    			console.log ("Hi Client: ", event.clientId);
	    			clientId_2 = event.clientId;
	    			fillClientDetailsForClientId (event.clientId);
	    			$("#client-details-modal").modal ("show"); // Open Client Details Modal (Special attention to contact)
    			});
	    	}
	    },
	    
	    
	    color: 'yellow',   // an option!
	    textColor: 'black', // an option!
        // put your options and callback's here
	    dayClick: function (date, allDay, jsEvent, view) {
            $('#calendar').fullCalendar('gotoDate', date);
            $('#calendar').fullCalendar('changeView', 'agendaDay');
	    },
    })
    // Calendar Ends
    
    $('#order-process-confirmation-button').click(orderProcessConfirmation);
    $('#dismiss-process-order-modal-btn').click(dismissProcessOrderModal);
    $('#client-details-by-clientId-btn').click(clientDetailsByClientIdButton);
    $('#backFromClientDetailsForOrder-btn').click(backFromClientDetailsForOrder);
    $('#product-details-by-productId-btn').click(productDetailsByProductIdButton);
    $('#backFromProductDetailsForOrder-btn').click(backFromProductDetailsForOrder);
    
    $('#order-process-confirmation-button').click(sddms.orderProcess);
    $('#mark-as-done-7').click(deleteClientReminder);
    $('#mark-as-done2').click(deleteLeadReminder);    
});




/** ______________________________________AUTOCOMPLETE FOR CLIENT NAME________________________________________________**/

var clientNameList = new Array ();

$.ajax ({
	url : 'clientmanagement/getClientDetailsList',
	type : 'POST',
	contentType : "application/json",
}).done (function(data) {
	for(i = 0; i < data.length; ++i) {
        clientNameList.push (data [i].clientName);
    }
	$("#clientName").shieldAutoComplete({
        dataSource: {
            data: clientNameList
        },
        minLength: 1
    });
});

/** ___________________________________________________________________________________________________________________**/





/** _________________________________________________JUMPS_____________________________________________________________**/

document.getElementById ("jump-to-orders-button").onclick = function () {
	window.open(
		'orders',
		'_blank' // <- This is what makes it open in a new window.
	);
};

/** ___________________________________________________________________________________________________________________**/





/** _____________________________________________SHOW / HIDE ___________________________________________________**/

function showDecisionModal () {
	$('#decision-modal').modal('show');
}

function hideDecisionModal () {
	$('#decision-modal').modal('hide');
}

function orderProcessConfirmation () {
	hideDecisionModal ();
};

function dismissProcessOrderModal () {
	showDecisionModal ();
};

function clientDetailsByClientIdButton () {
	hideDecisionModal ();
};

function backFromClientDetailsForOrder () {
	showDecisionModal ();
};

function productDetailsByProductIdButton () {
	hideDecisionModal ();
};

function backFromProductDetailsForOrder () {
	showDecisionModal ();
};

/** ___________________________________________________________________________________________________________________**/





/** _______________________________________________ORDER PROCESSING____________________________________________________**/


function deleteClientReminder () {
	swal ({	title: "Are you done with the client (" + clientName_2 +") ?",
			text: "",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, I'm Done!",   
			closeOnConfirm: false 
	 	},
	 	function() {
			console.log (clientId_2);
			$.ajax({
				url : "reminder/deleteClientReminder?clientId="+clientId_2,
				type : 'POST',
				contentType : "application/json",
				xhrFields: {
			      withCredentials: true
			   }
			}).done(function() {
				$('#client-details-modal').modal('hide');
				$('#calendar').fullCalendar ('refetchEvents');
				swal ("Good job!", "", "success");
			});
	 	}
	);
}



function deleteLeadReminder () {
	swal ({	title: "Are you done with the lead (" + leadName_2 +") ?",
			text: "",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, I'm Done!",   
			closeOnConfirm: false 
	 	},
	 	function() {
			console.log (leadId_2);
			$.ajax({
				url : "reminder/deleteLeadReminder?clientId="+leadId_2,
				type : 'POST',
				contentType : "application/json",
				xhrFields: {
			      withCredentials: true
			   }
			}).done(function() {
				$('#lead-details-modal2').modal('hide');
				$('#calendar').fullCalendar ('refetchEvents');
				swal ("Good job!", "", "success");
			});
	 	}
	);
}


sddms.orderProcess = function(evt) {
	console.log ("\nInside order process");
//	var formData = $ ('#order-process-form').serializeObject();
//	
//	formData.productId = formData.proId; delete (formData.proId);
//	formData.clientId = formData.clientId2; delete (formData.clientId2);
//	formData.clientName = formData.clientName3; delete (formData.clientName3);
//	
//	formData.productName = formData.productName2;
//	delete (formData.productName2);
//	console.log ("formData: ", formData);
//	$.ajax({
//		url : "order/orderProcess",
//		data : JSON.stringify (formData),
//		type : 'POST',
//		contentType : "application/json",
//		xhrFields: {
//	      withCredentials: true
//	   }
//	}).done(function() {
//		$('#order-process-modal').modal('hide');
//		$('#decision-modal').modal('hide');
//		swal ("Good job!", "The order is successfully processed!", "success");
//		$('#calendar').fullCalendar ('refetchEvents');
//	});
	
	$.ajax({
		url : "reminder/deleteOrderReminder?orderId="+ORDER_ID,
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#decision-modal').modal('hide');
		swal ("Good job!", "The order is successfully processed!", "success");
		$('#calendar').fullCalendar ('refetchEvents');
	});
};

/** ___________________________________________________________________________________________________________________**/



