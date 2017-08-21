function format (d) {
//	return "Hello";
	return 'Name : ' + d.name + '<br> Available : ' + d.available;
//    return 'Product Name: '+d.name+' +'<br>'+
//        'The child row can contain any data you wish, including links, images, inner tables etc.';
}

function CheckNewType (val) {
	var element=document.getElementById ('type');
	if (val == 'others') {
		element.style.display='block';
	} else {
		element.style.display='none';
	}
}


//document.getElementById (").onclick = function () {
$("#disqualify-lead-button5").click(function() {
	swal ({	title: "Are you sure?",
			text: "The lead will be added to special attention list!",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, Disqualify Lead!",   
			closeOnConfirm: false 
	 	}, 
		
		function() {
	 		var arr = $('#recommend-client-or-lead').val ();
			var n = arr.length;
			var clientId = arr [n - 5] + arr [n - 4] + arr [n - 3] + arr [n - 2];
	 		$.ajax ({
				url : "disqualifyLeadAndDeleteFromCal?clientId=" + clientId,
				type : 'POST',
				xhrFields: {
			      withCredentials: true
				}
	 		}).done(function() {
	 			swal ("Lead Disqualified !", "The selected Lead has been moved to special attention list.", "success");
	 			window.open("productmanagement","_self")
			});
	 	}
	);
});

function trial (val) {
	var element = document.getElementById ('type');
	console.log (val, " ", val.length);
	if (val == 'others') {
		element.style.display='block';
	} else {
		element.style.display='none';
	}
}

$(function () {
	$('#orderDate').datetimepicker({
		dateFormat: 'yy-mm-dd H:MM'
	});
	$('#orderDate3').datetimepicker({
		dateFormat: 'yy-mm-dd H:MM'
	});
	$('#dateOfDelivery').datetimepicker({
		dateFormat: 'yy-mm-dd H:MM'
	});
});



var clientAndLeadNameAndIdList = new Array ();

$.ajax ({
	url : 'clientmanagement/getClientAndLeadDetailsList', // Also get disqualified client list
	type : 'POST',
	contentType : "application/json",
}).done (function(data) {
	for(i = 0; i < data.length; ++i) {
        clientAndLeadNameAndIdList.push (data [i].clientLeadName + " (" + data [i].clientLeadId + ")");
    }
//	console.log ("Ajax client: ", clientAndLeadNameAndIdList);
	$("#recommend-client-or-lead").shieldAutoComplete({
	    dataSource: {
	        data: clientAndLeadNameAndIdList
	    },
	    minLength: 1
	});
	$("#type2-reco").shieldAutoComplete({
	    dataSource: {
	        data: clientAndLeadNameAndIdList
	    },
	    minLength: 1
	});
});


function requestProduct (productName, productCount, clientNameAndId) {
	var arr = clientNameAndId;
	var n = arr.length;
	var clientId = arr [n - 5] + arr [n - 4] + arr [n - 3] + arr [n - 2];
	var sendData = {
		productName: productName,
		productCount: productCount,
		clientId: clientId
	};
	console.log (sendData);
	$.ajax ({
		url : 'productmanagement/requestForProduct', // Also get disqualified client list
		data : JSON.stringify (sendData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		if (data == true) {
			swal ("Product Request Sent!", "You will be notified when the product is availaible!", "success");
			$('#target-request-product').hide();
		} else {
			swal("Product Already Requested!");
		}
	});
}


var dTable;
var clientIdForRecommendation = "ZZZZ";

var clientOrders = new Array ();

$(document).ready (function () {
	
	/*_________________________________________________CHART (STARTS)  ________________________________________________________________________________*/
	
	$ ('#analyse-btn').on ('click', function () {
		receivedLeadInfo = null;
		$('recommend-client-or-lead').val("");
		$('#target-lead-profile').hide ();
		$('#target-client-profile').hide ();
		clientIdForRecommendation = "ZZZZ";
		$('#product-table').dataTable().fnReloadAjax();
		$('#analyse-chart').toggle ();
	});
	
	
	$.ajax ({
		url: 'productmanagement/getQuantitySoldForTypes',
		type: 'POST',
		contentType: "application/json",
	}).done (function(data) {
		var revenueChart = new FusionCharts({
	        type: 'doughnut2d',
	        renderAt: 'chart-container',
	        width: '450',
	        height: '450',
	        dataFormat: 'json',
	        dataSource: {
	            "chart": {
	                "caption": "<br> Split of Product Sold by Product Categories",
	                "subCaption": "<br />Overall",
	                "numberPrefix": "$",
	                "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000,#9900FF",
	                "bgColor": "#ECEDEE",
	                "showBorder": "0",
	                "use3DLighting": "0",
	                "showShadow": "0",
	                "enableSmartLabels": "0",
	                "startingAngle": "310",
	                "showLabels": "0",
	                "showPercentValues": "1",
	                "showLegend": "1",
	                "legendShadow": "0",
	                "legendBorderAlpha": "0",
	                "defaultCenterLabel": "Total revenue: $64.08K",
	                "centerLabel": "Revenue from $label: $value",
	                "centerLabelBold": "1",
	                "showTooltip": "0",
	                "decimals": "0",
	                "captionFontSize": "14",
	                "subcaptionFontSize": "14",
	                "subcaptionFontBold": "0"
	            },
	            "data": [
	                {
	                    "label": "Fast Food Restaurant",
	                    "value": data.fast
	                }, 
	                {
	                    "label": "Health Stores",
	                    "value": data.health
	                }, 
	                {
	                    "label": "Workplace",
	                    "value": data.work
	                },
	                {
	                	"label": "Sports Centre",
	                	"value": data.sports
	                },
	                {
	                    "label": "Movie Theatres",
	                    "value": data.movie
	                }
	            ]
	        }
	    }).render();
	});
	
	
    
    
    /*____________________________________________________CHART (ENDS)_____________________________________________________________________________*/
	
	var receivedLeadInfo = sessionStorage.getItem ("leadInfo");
	if (receivedLeadInfo != null) {
		$('#recommend-client-or-lead').val(receivedLeadInfo);
	}
	
	
	var receivedProductId2 = sessionStorage.getItem ("productIdSent2");
	// Action carried out later
	
	
	$ ('#request-product-for-client-button').on ('click', function () {
		$('#target-next-order').hide();
		$('#target-request-product').toggle ();
	});
	
	$ ('#request-product-for-lead-button').on ('click', function () {
		$('#target-next-order').hide();
		$('#target-request-product').toggle ();
	});
	
	$ ('#confirm-request-product-button').on ('click', function () {
		var productRequestedName = $('#productName5').val ();
		var productRequestedCount = $('#productCount5').val ();
		var clientNameAndId = $('#recommend-client-or-lead').val ();
		requestProduct (productRequestedName, productRequestedCount, clientNameAndId);
	});
	
	
	$ ('#next-order-date-for-lead-button').on ('click', function () {
		$('#target-request-product').hide();
		$('#target-next-order').toggle();
	});
	
	
	$ ('#next-order-date-for-client-button').on ('click', function () {
		$('#target-request-product').hide();
		$('#target-next-order').toggle();
	});
	
	
	$ ('#cancel-request-product-button').on ('click', function () {
		$('#target-request-product').hide();
	});
	
	
	$ ('#cancel-next-order').on ('click', function () {
		$('#target-next-order').hide();
	});
	
	
	$ ('#all-result-button').on ('click', function () {
		clientIdForRecommendation = "ZZZZ";
		$('#product-table').dataTable().fnReloadAjax();
	});
	
	
	$ ('#confirm-next-order-date-button').on ('click', function () {
		var arr = $('#recommend-client-or-lead').val ();
		var n = arr.length;
		var clientId = arr [n - 5] + arr [n - 4] + arr [n - 3] + arr [n - 2];
		var title = "Contact Client " + clientId;
		var nextOrderDate = $('#orderDate3').val ();
		var sendData = {
			title: title,
			clientId: clientId,
			start: nextOrderDate,
			productId: "N/A",
			orderId: -1
		}
		console.log ("sendData: ", sendData);
		$.ajax ({
			url : 'reminder/setReminderForClient', // Also get disqualified client list // Also insert it into client table
			data : JSON.stringify (sendData),
			type : 'POST',
			contentType : "application/json",
			xhrFields: {
				withCredentials: true
			}
		}).done (function(data) {
			$('#calendar4').fullCalendar ('refetchEvents');
			swal ("Reminder Set!", "You will be notified on the day of reminder!", "success");
		});
	});
	
	
	
	/* ____________________________________________ CALENDAR 3 (STARTS)_____________________________________________________*/
	
	
	$('#calendar3').fullCalendar({
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
            $('#calendar3').fullCalendar('gotoDate', date);
            $('#calendar3').fullCalendar('changeView', 'agendaDay');
            $('#orderDate').val (date.format ('YYYY-MM-DD H:mm'));
        },
	    
	    color: 'yellow',   // an option!
	    textColor: 'black', // an option!
    })
	
	
	$('#product-place-order-modal').on('shown.bs.modal',function(){
    	$('#calendar3').fullCalendar('render');
    })
    
    /* ________________________________________________________________CALENDAR 3 (ENDS)________________________________________________________________________________________ */
    
    
    
    
    
    
    /* ______________________________________________________________ CALENDAR 4 (STARTS)_________________________________________________________________________________*/
    
    
    
    
    $('#calendar4').fullCalendar({
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
            $('#calendar4').fullCalendar('gotoDate', date);
            $('#calendar4').fullCalendar('changeView', 'agendaDay');
            $('#orderDate3').val (date.format ('YYYY-MM-DD H:mm'));
        },
	    
	    color: 'yellow',   // an option!
	    textColor: 'black', // an option!
    })
    
    
    $('#next-order-date-for-lead-button').on('shown.bs.modal',function(){
    	$('#calendar4').fullCalendar('render');
    })
    
    $('#next-order-date-for-client-button').on('shown.bs.modal',function(){
    	$('#calendar4').fullCalendar('render');
    })
    
    /* __________________________________________________________________CALENDAR 4 (ENDS)______________________________________________________________________*/
	
	
	
	var initPage = function () {
		dTable = $('#product-emergency-replacement-table').DataTable ({bPaginate: false, bResetDisplay : false, select : "single",  sScrollX:false, bInfo:false});
		
		switchActiveTab('nav-product');
		var detailRows = [];
		
		
		sddms.dataTable = $('#product-table').DataTable ({
			'ajax' : {
				url: 'productmanagement/fetchProductDetails',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					// send only data required by backend API
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					d.clientIdForRecommendation = clientIdForRecommendation;
					console.log ("Inside ajax ... Product.JS");
					console.log (JSON.stringify (d));
					return JSON.stringify (d);
				},
				dataSrc: "productDetailsEntities",
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
				{ data: 'productId' },
				{ data: 'name' },
				{ data: 'price' },
				{ data: 'type' },
				{ data: 'season' },
				{ data: 'isNew' },
				{ data: 'available' },
				{ data: 'ordered' },
				{ data: 'remaining' }
//				{ data: 'potentialClient' }
			],
			select: "multiple"
		});
		

		/* _____________________________________________________SEARCH FOR COLUMN STARTS___________________________________________________ */
		
	    // Setup - add a text input to each footer cell
	    $('#product-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	var myId = title + "forProduct";
	        	myId = myId.replace (" ", "");
	        	$(this).html( '<input type="text" id = "'+myId+'" placeholder = "'+title+'"/>' );
	        }
	    } );
	 
	    // DataTable
	    var table = $('#product-table').DataTable();
	 
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
	    });
	    
	    if (receivedProductId2 != "" || receivedProductId2 != null) {
//			console.log ("Hello => ", receivedProductId2);
			$('#ProductIdforProduct').val(receivedProductId2);
			$('#ProductIdforProduct').keyup();
		}
		
	    /* _____________________________________________________SEARCH FOR COLUMN ENDS___________________________________________________ */
	    
		
	    
		/* _____________________________________________________ROW DETAIL STARTS___________________________________________________ */
		
	    $('#product-table tbody').on( 'click', 'tr td.details-control', function () {
	        var tr = $(this).closest ('tr');
	        var row = sddms.dataTable.row (tr);
	        var idx = $.inArray (tr.attr('id'), detailRows);
	 
	        if (row.child.isShown ()) {
	            tr.removeClass ('details');
	            row.child.hide ();
	 
	            // Remove from the 'open' array
	            detailRows.splice ( idx, 1 );
	        }
	        else {
	            tr.addClass ('details');
	            row.child (format (row.data())).show ();
	 
	            // Add to the 'open' array
	            if (idx === -1) {
	                detailRows.push (tr.attr('id'));
	            }
	        }
	    } );
	 
	    // On each draw, loop over the `detailRows` array and show any child rows
//	    $('#product-table').DataTable.on ('draw', function () {
//	        $.each (detailRows, function (i, id) {
//	            $('#'+id+' td.details-control').trigger ('click');
//	        } );
//	    } );

	    /* _____________________________________________________ROW DETAIL ENDS___________________________________________________ */

		
		$('#product-add-save-button').click (sddms.addProduct);
		$('#product-modify-save-button').click (sddms.modifyProduct); // Make a different form .. Else it'll go to sddms.addProduct
		$('#product-delete-confirm-button').click (sddms.deleteProduct);
		$('#product-place-order-confirm-button').click (sddms.placeOrder);
		$('#product-emergency-replacement-confirm-button').click (sddms.emergencyReplacement);
		$('#report-product-shortage-confirm-button').click (sddms.reportProductShortage);
		
		sddms.dataTable.on ('select', function () { 
			$ ('#product-add-modal-btn').prop ('disabled', true);
			$ ('#product-open-delete-modal-btn').prop ('disabled', false);
			$ ('#product-edit-modal-btn').prop ('disabled', false);
			$ ('#product-place-order-modal-btn').prop ('disabled', false);
			$ ('#check-new-order-modal-btn').prop ('disabled', false);
			$ ('#product-offers-modal-btn').prop ('disabled', false);
			$ ('#product-emergency-replacement-modal-btn').prop ('disabled', false);
			$ ('#product-shortage-modal-btn').prop ('disabled', false);
	    });
		
		sddms.dataTable.on ('deselect', function () {
			$ ('#product-add-modal-btn').prop ('disabled', false);
			$ ('#product-open-delete-modal-btn').prop ('disabled', true);
			$ ('#product-edit-modal-btn').prop ('disabled', true);
			$ ('#product-place-order-modal-btn').prop ('disabled', true);
			$ ('#check-new-order-modal-btn').prop ('disabled', true);
			$ ('#product-offers-modal-btn').prop ('disabled', true);
			$ ('#product-emergency-replacement-modal-btn').prop ('disabled', true);
			$ ('#product-shortage-modal-btn').prop ('disabled', true);
	    });
		
		$ ('#product-add-modal-btn').on ('click', function () {
			$('#product-add-modal #myModalLabel').data().mode = 'add';
		});
		
		$ ('#product-edit-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
            console.log ("Edit : ", selectedData);
            $('#productId_1').prop ('readonly',true)
            $('#potentialClient_1').prop ('readonly',true)
            $('#productId_1').val (selectedData.productId);
            $('#name_1').val (selectedData.name);
            $('#price_1').val (selectedData.price);
            $('#available_1').val (selectedData.available);
            $('#type_1').val (selectedData.type);
        });
		
		function getRandomInt(min, max) {
		    return Math.floor(Math.random() * (max - min + 1)) + min;
		}
		
		
		$ ('#product-place-order-modal-btn').on ('click', function () {
			$('#product-emergency-replacement-result-modal').modal('hide');
            $('#clientNameFromRecommend').prop ('readonly',true)
			var product = sddms.dataTable.rows('.selected').data();
			$("#products").empty();
			clientOrders = [];
			for(var i=0;i<product.length;i++) {
				var randomCount = getRandomInt (500, 5400);
				$("#products").append ('<div class="form-group col-lg-12"> <div class="col-lg-5"><label for="productName">  <span class="label label-info"> <font size="2"> ' + product[i].name + '</font> </span> </label></div>' + '<div class="col-lg-3"><label for="productPrice">  <span class="label label-warning"> <font size="2">' + product[i].price + '</font> </span> </label></div>' + '<div class="col-lg-4"><input name="productQuantity" type="text" class="form-control" id="productQuantity' + i + '"/></div></div>');
				var productId = product[i].productId;
				clientOrders.push ([productId]);
			}
        });
		
		
		$ ('#product-emergency-replacement-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
            console.log (selectedData);
            $('#proId_1').prop ('readonly',true)
            $('#productName_1').prop ('readonly',true)
            $('#clientForEmergencyReplacement').prop ('readonly',true)
            $('#proId_1').val (selectedData.productId);
            $('#productName_1').val (selectedData.name);
        });
		
		
		$ ('#product-shortage-modal-btn').on ('click', function () {
			var selectedData = sddms.dataTable.row ('.selected').data ();
            console.log (selectedData);
            var availMinusOrder = selectedData.ordered - selectedData.available;
            $('#proId_2').prop ('readonly',true)
            $('#productName_2').prop ('readonly',true)
            $('#underflowQuantity').prop ('readonly',true)
            $('#clientAffectedDueToShortage').prop ('readonly',true)
            $('#proId_2').val (selectedData.productId);
            $('#productName_2').val (selectedData.name);
            $('#underflowQuantity').val (availMinusOrder);
        });
		
		
	    
	    
	    
		/** ________________________________________CLIENT NAME AND ID AUTO STARTS_______________________________________________________ **/
		
		$.ajax ({
			url : 'clientmanagement/getClientDetailsList',
			type : 'POST',
			contentType : "application/json",
		}).done (function(data) {
//			for(var i = 0; i < data.length; ++i){
//		        console.log ("Client Id : ", data [i].clientId, " -------   Client Name = ", data [i].clientName);
//		    }
			var typeSelects = $('.clientNameList-selects');
			$.each (data, function (i, da) {
				$.each (typeSelects, function (i, select) {
					$ (select).append ($ ('<option data-display = "' + da.clientName + '" value="' + da.clientName + '_' +  da.clientId + '">' + da.clientName + ' (' + da.clientId + ') ' + '</li>'));
				});
			});
		});
		/** ________________________________________CLIENT NAME AND ID AUTO ENDS_______________________________________________________ **/
		
		$('#confirm-recommend-client-or-lead-button').click (confirmRecommendationForClientOrLead);
	};
	initPage ();
});



function fillAndDisplayLeadOrClientPanel (clientIdForRecommendation) {
	$.ajax ({
		url : "isLead?id="+ clientIdForRecommendation,
		type : 'POST',
		contentType : "application/json",
	}).done (function(data) {
		if (data == true) {
			// If the selected Id is leads Id
		    $('#target-client-profile').hide();
		    $('#target-lead-profile').show();
		    
		    $.ajax ({
				url : "getLeadForLeadId?clientId="+ clientIdForRecommendation,
				type : 'POST',
				contentType : "application/json",
		    }).done (function(data) {
		    	$("#displayLeadId").html(data.clientId);
		    	$("#displayLeadName").html(data.leadName);
		    	$("#displayLeadType").html(data.leadType);
		    	$("#displayLeadRegion").html(data.leadRegion);
		    	$("#displayLeadPhone").html(data.leadContact);
		    	$("#displayLeadEmail").html(data.leadEmail);
		    	$("#displayLeadAddress").html(data.leadAddress);
		    });
		    
		} else {
			// If the selected Id is clients Id
		    $('#target-lead-profile').hide();
		    $('#target-client-profile').show();
		    
		    $.ajax ({
				url : "clientmanagement/getClientForClientId?clientId="+ clientIdForRecommendation,
				type : 'POST',
				contentType : "application/json",
		    }).done (function(data) {
		    	$("#displayClientId").html(data.clientId);
		    	$("#displayClientName").html(data.name);
		    	$("#displayClientType").html(data.type);
		    	$("#displayClientRegion").html(data.region);
		    	$("#displayClientPhone").html(data.contact);
		    	$("#displayClientEmail").html(data.email);
		    	$("#displayClientAddress").html(data.address);
		    });
		}
	});
}


function confirmRecommendationForClientOrLead () {
	$('#analyse-chart').hide();
	$('#target-request-product').hide();
	$('#target-next-order').hide();
	var clientNameAndId = $('#recommend-client-or-lead').val ();
	var n = clientNameAndId.length;
	clientIdForRecommendation = clientNameAndId [n - 5] + clientNameAndId [n - 4] + clientNameAndId [n - 3] + clientNameAndId [n - 2];
//	console.log("Hello : " + clientIdForRecommendation);
	$('#product-table').dataTable().fnReloadAjax();
	$('#clientNameFromRecommend').val (clientNameAndId);
	$('#clientForEmergencyReplacement').val (clientNameAndId);
	$('#clientAffectedDueToShortage').val (clientNameAndId);
	
	// Fill and Display Pannel
	fillAndDisplayLeadOrClientPanel (clientIdForRecommendation);
}

//$(function() {
//    $("#product-place-order-modal").draggable();
//    $("#product-offers-modal").draggable();
//    $("#product-emergency-replacement-modal" ).draggable();
//    $("#product-shortage-modal").draggable();
//    $("#product-add-modal").draggable();
//    $("#product-modify-modal").draggable();
//    $("#product-delete-modal").draggable();
//    $("#product-emergency-replacement-result-modal").draggable();
//});


var productIdList = new Array ();
var productNameList = new Array ();


$.ajax ({
	url: 'productmanagement/getProductNameAndId',
	type: 'POST',
	contentType: "application/json",
}).done (function(data) {
	for (i = 0; i < data.length; ++i) {
		productIdList.push (data[i].productId);
		productNameList.push (data[i].productName);
	}
	$("#productId").shieldAutoComplete({
        dataSource: {
            data: productIdList
        },
        minLength: 1
    });
	$("#name").shieldAutoComplete({
        dataSource: {
            data: productNameList
        },
        minLength: 1
    });
	$("#productName5").shieldAutoComplete({
        dataSource: {
            data: productNameList
        },
        minLength: 1
    });
});



/** ______________________________________ADD PRODUCT STARTS________________________________________________________**/

sddms.addProduct = function(evt) {
	console.log ("\nInside add product");
	var formData = $ ('#product-add-form').serializeObject();
	formData.type = formData.typeManual;
	delete (formData.typeManual);
	console.log (formData);
	var url = 'productmanagement/addProductDetails';
	console.log (url);
		$.ajax({
		url : url,
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		console.log ("DONE!!");
		$('#product-add-modal').modal('hide');
		$('#product-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The product is successfully added!", "success");
	});
};

/** ______________________________________ADD PRODUCT ENDS________________________________________________________**/



/** ______________________________________MODIFY PRODUCT STARTS________________________________________________________**/

sddms.modifyProduct = function(evt) {
	console.log ("\nInside modify product");
	var formData = $ ('#product-modify-form').serializeObject();
	formData.type = formData.typeManual;
	delete (formData.typeManual);
	console.log (formData);
	$.ajax({
		url : 'productmanagement/modifyProductDetails',
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		console.log ("DONE!!");
		$('#product-modify-modal').modal('hide');
		$('#product-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The product is successfully modified!", "success");
	});
};

/** ______________________________________MODIFY PRODUCT ENDS________________________________________________________**/





/** ______________________________________PLACING AN ORDER STARTS________________________________________________________**/


sddms.placeOrder = function(evt) {
	console.log ("\nInside placeOrder");
	var items = new Array();
	
	/* ______ Client Name & Id ______ */
	var arr = $('#recommend-client-or-lead').val();;
	var n = arr.length;
	var clientId = arr [n - 5] + arr [n - 4] + arr [n - 3] + arr [n - 2];
	var arr2 = arr.split(' ');
	var clientName = "";
	for (i = 0; i < arr2.length; ++i) {
		if (arr2[i][0] == '(') {
			break;
		}
		if (i > 0) {
			clientName += " ";
		}
		clientName += arr2 [i];
	}
	/* ______________ */
	
	for (i=0; i<clientOrders.length; ++i) {
		var productQuantity = $('#productQuantity' + i).val();
		items.push ({productId : clientOrders[i][0], productQuantity : productQuantity });
	}
	
	var formData = {
		clientId: clientId,
		clientName: clientName,
		orderDate: $('#orderDate').val(),
		items: items
	};
	
	console.log ("formData: ", formData);
	

	$.ajax({
		url : "order/placeOrderMultiple",
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#product-table').dataTable().fnReloadAjax();
		swal ("Order Placed!", "The order is added to the reminder list!", "success");
//		$('#product-place-order-modal').modal('hide');
		$('#calendar3').fullCalendar ('refetchEvents');
	});
};

/** ______________________________________PLACING AN ORDER ENDS________________________________________________________**/



/** ______________________________________EMERGENCY REPLACEMENT STARTS________________________________________________________**/


sddms.emergencyReplacement = function (evt) {
	console.log ("\nInside emergencyReplacement");
	var formData = $ ('#product-emergency-replacement-form').serializeObject();
	
	var arr = formData.clientForEmergencyReplacement;
	var n = arr.length;
	var clientId = arr [n - 5] + arr [n - 4] + arr [n - 3] + arr [n - 2];
	
	var productId = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].productId;
	var productType = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].type;
	var productName = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].name;
//	document.body.innerHTML = document.body.innerHTML.replace('REPLACEproductWhichIsShort', productName);
	var sendData = {
		productId: productId,
		productType: productType,
		clientId: clientId
	};
	console.log ("sendData: ", sendData);
	$.ajax({
		url : "productmanagement/emergencyReplacement",
		data : JSON.stringify (sendData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function(data) {
		$('#product-emergency-replacement-modal').modal('hide');
		dTable.clear();
		for (i= 0; i < data.length; ++i) {
			dTable.row.add ([
			    data [i].productId,
			    data [i].productName,
			    data [i].productPrice,
			]).draw (false);
		}
		dTable.on ('select', function () {
			$ ('#product-place-order-modal-btn_1').prop ('disabled', false);
		});
		
		dTable.on ('deselect', function () {
			$ ('#product-place-order-modal-btn_1').prop ('disabled', true);
		});
		
		$ ('#product-place-order-modal-btn_1').on ('click', function () {
			console.log ("Hiding");
			$('#product-emergency-replacement-result-modal').modal('hide');
			var selectedDataFromReplacementPlaceOrder = dTable.row ('.selected').data ();
			console.log ("selectedDataFromReplacementPlaceOrder: ", selectedDataFromReplacementPlaceOrder);
			$('#proId').prop ('readonly',true)
			$('#productName').prop ('readonly',true)
			$('#proId').val (selectedDataFromReplacementPlaceOrder [0]);
			$('#productName').val (selectedDataFromReplacementPlaceOrder [1]);
        });
	});
};

/** ______________________________________EMERGENCY REPLACEMENT ENDS________________________________________________________**/




/** ______________________________________REPORT SHORTAGE STARTS________________________________________________________**/


sddms.reportProductShortage = function (evt) {
	console.log ("\nInside Report Product Shortage");
	var formData = $ ('#report-product-shortage').serializeObject();
	
	var arr = formData.clientAffectedDueToShortage;
	var n = arr.length;
	
	formData.clientAffectedId = arr [n - 5] + arr [n - 4] + arr [n - 3] + arr [n - 2];
	
	var arr2 = arr.split(' ');
	
	formData.clientAffected = "";
	
	for (i = 0; i < arr2.length; ++i) {
		if (arr2[i][0] == '(') {
			break;
		}
		if (i > 0) {
			formData.clientAffected += " ";
		}
		formData.clientAffected += arr2 [i];
	}
	
	formData.productId = formData.proId;
	delete (formData.proId);
	console.log ("Form Data", formData);
	$.ajax({
		url : "productmanagement/reportProductShortage",
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#product-shortage-modal').modal('hide');
		$('#product-table').dataTable().fnReloadAjax();
		swal ("Reported to the manager!", "Notification will be sent to dashboard when manager adds the product!", "success");
	});
};

/** ______________________________________REPORT SHORTAGE ENDS________________________________________________________**/




/** ______________________________________DELETE PRODUCT	________________________________________________________**/


sddms.deleteProduct = function(evt) {
	console.log ("Inside Delete");
	var productId = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].productId;
	$.ajax({
		url : "productmanagement/deleteProductDetails?id=" + productId,
		type : 'DELETE',
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#product-delete-modal').modal('hide');
		$('#product-table').dataTable().fnReloadAjax();
		swal ("Deleted!", "The selected product has been deleted.", "success");
	});
};

/** ______________________________________DELETE PRODUCT ENDS________________________________________________________**/













