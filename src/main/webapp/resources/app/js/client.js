function format_details (d) {
	return '<span class="label label-info"> IMPORTANT NOTE </span> <br>' + d.importantNote +
		   '<br> <br> <span class="label label-info"> TRANSPORT </span> <br>' + d.transport;
}


function format_email (d) {
	return '<span class="label label-warning"> SEND EMAIL TO </span> <br>' + d.email;
}


document.getElementById ("view-products-button").onclick = function () {
	var selectedData = sddms.dataTable2.row ('.selected').data ();
	var leadId = selectedData.clientId;
	var leadName = selectedData.leadName;
	console.log (leadName + " " + leadId);
	sessionStorage.setItem ("leadInfo", leadName + " (" + leadId + ")"); 
	sessionStorage.setItem ("productIdSent2", "");
	var win = window.open ("productmanagement", '_blank');
};


document.getElementById ("view-products-button2").onclick = function () {
	var selectedData = sddms.dataTable.row ('.selected').data ();
	var clientId = selectedData.clientId;
	var clientName = selectedData.name;
	sessionStorage.setItem ("leadInfo", clientName + " (" + clientId + ")");  // Not renaming it to clientInfo because change has to be made at productmanagement page too
	sessionStorage.setItem ("productIdSent2", "");
	var win = window.open ("productmanagement", '_blank');
};




function CheckNewType (val) {
	var element=document.getElementById ('type');
	if (val == 'others') {
		element.style.display='block';
	} else {
		element.style.display='none';
	}
}

function CheckNewRegion (val) {
	var element=document.getElementById ('region');
	if (val == 'others') {
		element.style.display='block';
	} else {
		element.style.display='none';
	}
}


$(function () {
	$('#next-order').datetimepicker({
		dateFormat: 'yy-mm-dd H:MM'
	});
	$('#next-order_1').datetimepicker({
		dateFormat: 'yy-mm-dd H:MM'
	});
});



$(document).ready (function () {
	var initPage = function () {
		switchActiveTab ('nav-client');
		var detailRows = [];
		
		/* _________________________________________________CALENDAR 5 ___________________________________________________________ */
		
	    $('#calendar5').fullCalendar({
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
	            $('#calendar5').fullCalendar('gotoDate', date);
	            $('#calendar5').fullCalendar('changeView', 'agendaDay');
		        
	            $('#orderDate3').val (date.format ('YYYY-MM-DD H:mm'));
	        },
		    
		    color: 'yellow',   // an option!
		    textColor: 'black', // an option!
	    })
	    // Calendar5 Ends
	    
	    
	    
	    
	    /* _________________________________________________CALENDAR 7 ___________________________________________________________ */
		
	    $('#calendar7').fullCalendar({
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
	            $('#calendar7').fullCalendar('gotoDate', date);
	            $('#calendar7').fullCalendar('changeView', 'agendaDay');
		        
	            $('#leadDealingDate').val (date.format ('YYYY-MM-DD H:mm'));
	        },
		    
		    color: 'yellow',   // an option!
		    textColor: 'black', // an option!
	    })
	    // Calendar7 Ends
	    
	    
	    $ ('#set-reminder-for-lead-button').click (sddms.fillLeadDetails);
	    
	    
	    
	    $('#confirm-next-order-date-button5').on ('click', function () {
	    	var remindOn = $('#orderDate3').val ();
	    	var sendData = {
    			title: "Contact Disqualified Client " + sddms.dataTable3.data () [sddms.dataTable3.row ('.selected') [0]].clientId,
    			start: remindOn,
    			clientId: sddms.dataTable3.data () [sddms.dataTable3.row ('.selected') [0]].clientId,
    			productId: "N/A",
    			orderID: -1
    		};
    		console.log (sendData);
    		$.ajax({
    			url: 'reminder/setReminderForDisq',
    			data : JSON.stringify (sendData),
    			type : 'POST',
    			contentType : "application/json",
    			xhrFields: {
    		      withCredentials: true
    		   }
    		}).done(function() {
    			$('#calendar5').fullCalendar ('refetchEvents');
    			swal ("Good job!", "You will be notified about the reminder!", "success");
    		});
	    });
	    

	    
	    
		sddms.dataTable2 = $('#lead-table').DataTable ({
			autoFill: true,
			bfilter: true,
			'ajax' : {
				url: 'clientmanagement/fetchLeadClientsAll',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
			    },
			    dataSrc: "leadDetailsEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			
			columns: [
				{ data: 'clientId' },
				{ data: 'leadName' },
				{ data: 'leadContact' },
				{ data: 'leadEmail' },
				{ data: 'leadAddress' },
				{ data: 'leadType' },
				{ data: 'leadRegion'},
				{ data: 'leadReminderStatus' }
			],
			select: "single",
		});
		
		sddms.dataTable = $('#client-table').DataTable ({
			autoFill: true,
			bfilter: true,
			'ajax' : {
				url: 'clientmanagement/findClientByClientId',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					delete (d.columns);
					delete (d.order);
					delete (d.search);
//					d.regionId = $('#client-region-filter').val ();
//					d.typeId = $('#client-type-filter').val ();
					return JSON.stringify (d);
			    },
			    dataSrc: "clientDetailsEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			
			columns: [
				{
				    "class":          "details-control",
				    "orderable":      false,
				    "data":           null,
				    "defaultContent": ""
				},
				{ data: 'clientId' },
				{ data: 'name' },
				{ data: 'email' },
				{ data: 'type' },
				{ data: 'address' },
				{ data: 'contact' },
				{ data: 'nextOrderDate' },
				{ data: 'region'}
			],
			select: "single"
		});
		
		
		
		
		sddms.dataTable3 = $('#disqualified_client_table').DataTable ({
			autoFill: true,
			bfilter: true,
			'ajax' : {
				url: 'clientmanagement/fetchDisqualifiedLeadClientsAll',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
			    },
			    dataSrc: "disqualifiedLeadEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			
			columns: [
				{ data: 'clientId' },
				{ data: 'disLeadName' },
				{ data: 'disLeadContact' },
				{ data: 'disLeadEmail'},
				{ data: 'disLeadAddress'},
				{ data: 'disLeadType'},
				{ data: 'disLeadRegion'}
			],
			select: "single"
		});
		
		
		/* _____________________________________________________TOGGLE FOR COLUMN STARTS___________________________________________________ */
		
//	    $('a.toggle-vis').on( 'click', function (e) {
//	        e.preventDefault();
//	        
//	        CONSOLE.LOG ("HERE");
//	 
//	        // Get the column API object
//	        var column = table.column( $(this).attr('data-column') );
//	 
//	        // Toggle the visibility
//	        column.visible( ! column.visible() );
//	    } );
		
	    /* _____________________________________________________TOGGLE FOR COLUMN ENDS___________________________________________________ */
	    
	    
		
		
		/* _____________________________________________________SEARCH FOR COLUMN STARTS___________________________________________________ */
		
	    // Setup - add a text input to each footer cell
	    $('#client-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder = "'+title+'"/>' );
	        }
	    } );
	 
	    // DataTable
	    var table = $('#client-table').DataTable();
	 
	    // Apply the search
	    table.columns().every( function () {
	        var that = this;
	        $( 'input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        } );
	    } );
	    
	    
	 // Setup - add a text input to each footer cell
	    $('#lead-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder = "'+title+'"/>' );
	        }
	    } );
	 
	    // DataTable
	    var table = $('#lead-table').DataTable();
	 
	    // Apply the search
	    table.columns().every( function () {
	        var that = this;
	        $( 'input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        } );
	    } );
	    
	    
	    
	 // Setup - add a text input to each footer cell
	    $('#disqualified_client_table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder = "'+title+'"/>' );
	        }
	    } );
	 
	    // DataTable
	    var table = $('#disqualified_client_table').DataTable();
	 
	    // Apply the search
	    table.columns().every( function () {
	        var that = this;
	        $( 'input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        } );
	    } );
		
	    /* _____________________________________________________SEARCH FOR COLUMN ENDS___________________________________________________ */
		
		
		
	    /* _____________________________________________________ROW DETAIL STARTS___________________________________________________ */
		
	    $('#client-table tbody').on( 'click', 'tr td.details-control', function () {
	        var tr = $(this).closest ('tr');
	        var row = sddms.dataTable.row (tr);
	        var idx = $.inArray (tr.attr('id'), detailRows);
	        if (!row.child.isShown ()) {
	            tr.addClass ('details');
	            row.child (format_details (row.data())).show ();
	            // Add to the 'open' array
	            if (idx === -1) {
	                detailRows.push (tr.attr('id'));
	            }
	        } else {
	        	tr.removeClass ('details');
	            row.child.hide ();
	            // Remove from the 'open' array
	            detailRows.splice ( idx, 1 );
	        }
	    } );
	 
	    // On each draw, loop over the `detailRows` array and show any child rows
//	    $('#client-table').DataTable.on ('draw', function () {
//	        $.each (detailRows, function (i, id) {
//	            $('#'+id+' td.details-control').trigger ('click');
//	        } );
//	    } );
	    
	    /* _____________________________________________________ROW DETAIL ENDS___________________________________________________ */
		
	    
	    
	    /* _____________________________________________________EMAIL STARTS___________________________________________________ */
	    
	    $('#client-table tbody').on( 'click', 'tr td.email-control', function () {
	        var tr = $(this).closest ('tr');
	        var row = sddms.dataTable.row (tr);
	        var idx = $.inArray (tr.attr('id'), detailRows);
	        if (!row.child.isShown ()) {
	            tr.addClass ('email');
	            row.child (format_email (row.data())).show ();
	            // Add to the 'open' array
	            if (idx === -1) {
	                detailRows.push (tr.attr('id'));
	            }
	        } else {
	        	tr.removeClass ('email');
	            row.child.hide ();
	            // Remove from the 'open' array
	            detailRows.splice (idx, 1);
	        }
	    } );
	 
	    /* _____________________________________________________EMAIL ENDS___________________________________________________ */

	    
	    
	    
	    
		/** _______________________________Filtering via region starts_______________________________ **/
		
		
		$.ajax ({
			url : 'regions',
			type : 'POST',
			contentType : "application/json",
			data: JSON.stringify ({ draw: 0, start: 0, length: 10})
		}).done (function(data) {
			var regionSelects = $('.region-selects');
			$.each (data.regions, function (i, region) {
				$.each (regionSelects, function (i, select) {
					$ (select).append ($ ('<option data-display = "' + region.name + '" value="' + region.id + '">' + region.name + '</li>'));
				});
			});
		});
		
		$('#client-region-filter').change (function() {
//			console.log ("regionFilter");
			//console.log ( "avc: " + $('#client-region-filter').val ());
			$('#client-table').dataTable().fnReloadAjax();
		});
		
		/** ________________________________________Filtering via region ends_____________________________________________ **/
		
		
		/** ________________________________________Filtering via type starts_______________________________________________________ **/
		
		$.ajax ({
			url : 'type',
			type : 'POST',
			contentType : "application/json",
			data: JSON.stringify ({ draw: 0, start: 0, length: 10})
		}).done (function(data) {
			var typeSelects = $('.type-selects');
			$.each (data.types, function (i, type) {
				$.each (typeSelects, function (i, select) {
					$ (select).append ($ ('<option data-display = "' + type.typeName + '" value="' + type.typeId + '">' + type.typeName + '</li>'));
				});
			});
		});
		
		
		$('#client-type-filter').change (function() {
			console.log ("typeFilter");
			$('#client-table').dataTable().fnReloadAjax();
		});
		
		/** ________________________________________Filtering via type ends_______________________________________________________ **/
	    
		
		/** _____________________ Disable and Enable Buttons__________________ **/
		
		
		sddms.dataTable2.on ('select', function () {
			$ ('#lead-open-delete-modal-btn').prop ('disabled', false);
			$ ('#lead-edit-modal-btn').prop ('disabled', false);
			$ ('#set-reminder-for-lead-button').prop ('disabled', false);
			$ ('#view-products-button').prop ('disabled', false);
			$ ('#lead-add-modal-btn').prop ('disabled', true);
		});
		
		sddms.dataTable2.on ('deselect', function () {
			$ ('#lead-open-delete-modal-btn').prop ('disabled', true);
			$ ('#lead-edit-modal-btn').prop ('disabled', true);
			$ ('#set-reminder-for-lead-button').prop ('disabled', true);
			$ ('#view-products-button').prop ('disabled', true);
			$ ('#lead-add-modal-btn').prop ('disabled', false);
		});
		
		
		sddms.dataTable.on ('select', function () {
			$ ('#recommend-product-modal-btn').prop ('disabled', false);
			$ ('#client-open-delete-modal-btn').prop ('disabled', false);
			$ ('#client-edit-modal-btn').prop ('disabled', false);
			$ ('#client-referral-modal-btn').prop ('disabled', false);
			$ ('#importantNote-modal-btn').prop ('disabled', false);
			$ ('#importantNote-modal-btn').prop ('disabled', false);
			$ ('#view-products-button2').prop ('disabled', false);
			$ ('#client-add-modal-btn').prop ('disabled', true);
	    });
		
		sddms.dataTable.on ('deselect', function () {
			$ ('#recommend-product-modal-btn').prop ('disabled', true);
			$ ('#client-open-delete-modal-btn').prop ('disabled', true);
			$ ('#client-edit-modal-btn').prop ('disabled', true);
			$ ('#client-referral-modal-btn').prop ('disabled', true);
			$ ('#importantNote-modal-btn').prop ('disabled', true);
			$ ('#view-products-button2').prop ('disabled', true);
			$ ('#client-add-modal-btn').prop ('disabled', false);
	    });
		
		
		sddms.dataTable3.on ('select', function () {
			$ ('#set-reminder-for-disqualified-client-btn').prop ('disabled', false);
			$ ('#disqualified-lead-open-delete-modal-btn').prop ('disabled', false);
		});
		
		sddms.dataTable3.on ('deselect', function () {
			$ ('#set-reminder-for-disqualified-client-btn').prop ('disabled', true);
			$ ('#disqualified-lead-open-delete-modal-btn').prop ('disabled', true);
		});
		
		
		 $('#set-reminder-for-disqualified-client-modal').on('shown.bs.modal',function(){
	    	$('#calendar5').fullCalendar('render');
	     });
		

		/** ___________________________________________________________________ **/
		
		$('#confirm-reminder-for-lead-btn-7').click (confirmReminderForLead);
		 
		$('#lead-open-delete-modal-btn').click (sddms.deleteLead);
		$('#client-add-button').click (sddms.addClient);
		$('#lead-add-button').click (sddms.addLead);
		$('#client-edit-button').click (sddms.editClient);
		$('#client-delete-button').click (sddms.deleteClient);
		$('#importantNote-button').click (sddms.importantNote);
		$('#go-recommend-product-button').click (sddms.goRecommendProduct);
		$('#lead-edit-button-confirm').click (sddms.editLead);
		$('#disqualified-lead-open-delete-modal-btn').click (sddms.deleteDisq);
		
		// Note: Hash part is mapped to the id in the html page.
 		$ ('#client-edit-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
            console.log (selectedData);
            $('#clientId_1').prop ('readonly',true)
            $('#client-add-modal #myModalLabel').data ().mode = 'update';
            $('#client-add-modal #myModalLabel').html ('<span class="glyphicon glyphicon-edit"></span> Modify client');
            $('#clientId_1').val (selectedData.clientId);
            $('#name_1').val (selectedData.name);
            $('#email_1').val (selectedData.email);
            $('#address_1').val (selectedData.address);
            $('#contact_1').val (selectedData.contact);
            $('#next-order_1').val (selectedData.nextOrderDate);
        });
 		
 		
 		$ ('#lead-edit-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable2.row ('.selected').data ();
            console.log (selectedData);
            $('#leadId2').prop ('readonly',true)
            $('#leadId2').val (selectedData.clientId);
            $('#leadName2').val (selectedData.leadName);
            $('#leadEmail2').val (selectedData.leadEmail);
            $('#leadAddress2').val (selectedData.leadAddress);
            $('#leadContact2').val (selectedData.leadContact);
            $('#leadType2').val (selectedData.leadType);
            $('#leadTransport2').val (selectedData.leadTransport);
            $('#leadRegion2').val (selectedData.leadRegion);
        });
 		
 		
 	// Note: Hash part is mapped to the id in the html page.
 		$ ('#recommend-product-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
            console.log (selectedData);
            $('#clientId_2').prop ('readonly',true)
            $('#clientName').prop ('readonly',true)
            $('#clientType').prop ('readonly',true)
            $('#clientRegion').prop ('readonly',true)
            $('#clientId_2').val (selectedData.clientId);
            $('#clientName').val (selectedData.name);
            $('#clientType').val (selectedData.type);
            $('#clientRegion').val (selectedData.region);
        });
 		
 		
 		
 		$ ('#importantNote-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
//            console.log (selectedData);
            $('#clid').val (selectedData.clientId);
            $('#clid').prop ('readonly',true);
        });
		
		
        //reset data when modal hides
		
        $('#client-add-modal').on ('hidden.bs.modal', function() {
        	$('#id').prop ('readonly',false);
            $('#client-add-modal #myModalLabel').data ().mode = 'add';
            $('#client-add-modal #myModalLabel').html ('Add new client');
            $('#client-add-form')[0].reset ();
        });
		
	};
	initPage();
});

var selectedDataForLead;
sddms.fillLeadDetails = function (evt) {
	selectedDataForLead = sddms.dataTable2.row ('.selected').data ();
	console.log (selectedDataForLead);
	// Fill up the details (For Lead Info Panel & Setting the Remainder Panel)
	$("#displayLeadId_7").html(selectedDataForLead.clientId);
	$("#displayLeadName_7").html(selectedDataForLead.leadName);
	$("#displayLeadType_7").html(selectedDataForLead.leadType);
	$("#displayLeadPhone_7").html(selectedDataForLead.leadContact);
	$("#displayLeadEmail_7").html(selectedDataForLead.leadEmail);
	$("#displayLeadAddress_7").html(selectedDataForLead.leadAddress);
	
	// Store Lead Id for view products
//	leadId = selectedDataForLead.clientId;
	
	// Hide Current Modal
//	$('#lead-from-manager-modal').modal('hide');
	
	// Show Next Modal (Lead Details) Shown from html
	
	
	// Send the selected data for setting the reminder
};


$(document).ready(
    function () {
        $("#transportOptions").select2();
    }
);


$(function() {
    $( "#client-referral-modal" ).draggable();
    $( "#importantNote-modal" ).draggable();
    $( "#client-add-modal" ).draggable();
    $( "#client-edit-modal" ).draggable();
    $( "#client-delete-modal" ).draggable();
    $( "#recommend-product-modal" ).draggable();
});

var clientIdList = new Array ();
var clientNameList = new Array ();

$.ajax ({
	url : 'clientmanagement/getClientDetailsList',
	type : 'POST',
	contentType : "application/json",
}).done (function(data) {
	for(i = 0; i < data.length; ++i) {
        clientIdList.push (data [i].clientId);
        clientNameList.push (data [i].clientName);
    }
	$("#name").shieldAutoComplete({
        dataSource: {
            data: clientNameList
        },
        minLength: 1
    });
	$("#clientId").shieldAutoComplete({
        dataSource: {
            data: clientIdList
        },
        minLength: 1
    });
});


function getTransportsInStringForm (d) {
	var ret = "";
	for (i = 0; i < d.length; ++i) {
		ret = ret + d [i] + " ";
	}
	return ret;
}




function confirmReminderForLead () {
	console.log ("Setting Reminder For Lead!");
	
	var reminderDate = $('#leadDealingDate').val ();
	console.log ("reminderDate : ", reminderDate);
	
	var sendReminderForLead = {
		title: "Contact Lead " + selectedDataForLead.clientId,
		start: reminderDate,
		clientId: selectedDataForLead.clientId,
		productId: "N/A",
		orderId: -1
	};

	console.log ("Set the reminder for : ", sendReminderForLead);
	
	// Ajax
	
	// 1. Insert event in calendarEvents
	// 2. Set ReminderSet as "true" in lead table
	
	$.ajax ({
		url : "reminder/setReminderForLead",
		data : JSON.stringify (sendReminderForLead),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function () {
		// Swal
		swal ("Reminder Confirmed!", "You will be notified about the reminder!", "success");
		
		// Reload Datatable
		$('#lead-table').dataTable().fnReloadAjax();
		
		// Re-fetch Events for Calendar
		$('#calendar7').fullCalendar ('refetchEvents');
		
	});
}





/** ______________________________________ADD CLIENT________________________________________________________**/

sddms.addClient = function (evt) {
	console.log ("\nInside add client");
	var formData = $ ('#client-add-form').serializeObject();
	formData.transport = getTransportsInStringForm (formData.transportOptions);
	console.log ("Form Data : " , formData);
	var url = 'clientmanagement/addClientDetails';
	$.ajax({
		url: url,
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#client-add-modal').modal('hide');
		$('#client-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The client is successfully added!", "success");
	});
};

/** ______________________________________ADD CLIENT ENDS________________________________________________________**/



/** ______________________________________ADD LEAD________________________________________________________**/

sddms.addLead = function (evt) {
	console.log ("\nInside add lead");
	var formData = $ ('#lead-add-form').serializeObject();
	formData.clientId = formData.leadId;
	delete (formData.leadId);
	console.log ("Form Data : " , formData);
	$.ajax({
		url: 'clientmanagement/addLeadDetails',
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#lead-add-modal').modal('hide');
		$('#lead-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The lead is successfully added!", "success");
	});
};

/** ______________________________________ADD LEAD ENDS________________________________________________________**/






/** ______________________________________EDIT CLIENT________________________________________________________**/

sddms.editClient = function (evt) {
	console.log ("\nInside edit client");
	var formData = $ ('#client-edit-form').serializeObject();
	console.log ("Form Data : " , formData);
	var url = 'clientmanagement/modifyClientDetails';
	$.ajax({
		url: url,
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#client-edit-modal').modal('hide');
		$('#client-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The client is successfully modified!", "success");
	});
};

/** ______________________________________EDIT CLIENT ENDS________________________________________________________**/





/** ______________________________________EDIT LEAD________________________________________________________**/

sddms.editLead = function (evt) {
	var formData = $ ('#lead-edit-form').serializeObject();
	formData.clientId = formData.leadId;
	delete (formData.leadId);
	console.log ("Form Data : " , formData);
	$.ajax({
		url: 'clientmanagement/modifyLead',
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#lead-edit-modal').modal('hide');
		$('#lead-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The lead is successfully modified!", "success");
	});
};

/** ______________________________________EDIT LEAD ENDS________________________________________________________**/





/** ______________________________________RECOMMEND CLIENT STARTS________________________________________________________**/

sddms.goRecommendProduct = function (evt) {
	console.log ("\nInside goRecommendProduct");
	var formData = $ ('#product-recommend-form').serializeObject();
	console.log ("formData: ", formData);
	$.ajax({
		url : "clientmanagement/recommendProduct",
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function(data) {
		console.log (data);
		$('#product-recommend-modal').modal('hide');
	});
};

/** ______________________________________RECOMMEND CLIENT ENDS________________________________________________________**/







/** ______________________________________DELETE CLIENT________________________________________________________**/


sddms.deleteClient = function(evt) {
	console.log ("Inside Delete");
	var selectedId = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].clientId;
	console.log (selectedId);
	$.ajax ({
		url : "clientmanagement/deleteClientDetails?id=" + selectedId,
		type : 'DELETE',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function () {
		$('#client-delete-modal').modal('hide');
		$('#client-table').dataTable ().fnReloadAjax ();
		swal ("Deleted!", "The selected client has been deleted.", "success");
	});
};

/** ______________________________________DELETE CLIENT ENDS________________________________________________________**/





/** ______________________________________DISQUALIFY LEAD________________________________________________________**/

sddms.deleteLead = function(evt) {
	swal ({	title: "Are you sure?",
		text: "The Lead will be moved to the disqualified list!",
		type: "warning",   showCancelButton: true,   
		confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, delete it!",   
		closeOnConfirm: false 
	 },
	 
	 function() { 
		 var selectedData = sddms.dataTable2.data () [sddms.dataTable2.row ('.selected') [0]];
		console.log (selectedData);
		var selectedId = selectedData.clientId;
		console.log (selectedId);
		$.ajax ({
			url : "clientmanagement/deleteLead?clientId=" + selectedId,
			type : 'DELETE',
			xhrFields: {
		      withCredentials: true
		   }
		}).done (function () {
			$('#lead-table').dataTable ().fnReloadAjax ();
			$('#disqualified_client_table').dataTable ().fnReloadAjax ();
			swal ("Deleted!", "The selected client has been deleted.", "success");
		});
	 }
	 );
};

/** ______________________________________DISQUALIFY LEAD ENDS________________________________________________________**/





/** ______________________________________DELETE DISQ________________________________________________________**/

sddms.deleteDisq = function(evt) {
	swal ({	title: "Are you sure?",
		text: "The client will be delete forever!",
		type: "warning",   showCancelButton: true,   
		confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, delete it!",   
		closeOnConfirm: false 
	 },
	 
	 function() { 
		 var selectedData = sddms.dataTable3.data () [sddms.dataTable3.row ('.selected') [0]];
		console.log (selectedData);
		var selectedId = selectedData.clientId;
		console.log (selectedId);
		$.ajax ({
			url : "clientmanagement/deleteDisq?clientId=" + selectedId,
			type : 'DELETE',
			xhrFields: {
		      withCredentials: true
		   }
		}).done (function () {
			$('#disqualified_client_table').dataTable ().fnReloadAjax ();
			swal ("Deleted!", "The selected client has been deleted forever.", "success");
		});
	 }
	 );
};

/** ______________________________________DELETE DISQ ENDS________________________________________________________**/





/** ______________________________________Important Note________________________________________________________**/

sddms.importantNote = function(evt) {
	console.log ("Inside Note");
	var formData = $ ('#importantNote-form').serializeObject();
	console.log ("FORM DATA : " , formData);
	$.ajax ({
		url : "clientmanagement/updateImportantNote",
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
			withCredentials: true
		}
	}).done (function () {
		$('#importantNote-modal').modal ('hide');
		$('#client-table').dataTable ().fnReloadAjax ();
		swal ("Good job!", "The important note for the client is successfully added!", "success");
	});
};

/** ______________________________________Important Note Ends________________________________________________________**/




