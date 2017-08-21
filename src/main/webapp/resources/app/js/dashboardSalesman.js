var leadId = "AAAA";
var leadName = "BBBB";

document.getElementById ("view-products-button").onclick = function () {

    //the sessionStorage.setItem(); is the predefined function in javascript
    //which will support for every browser that will store the sessions.
    sessionStorage.setItem ("leadInfo", leadName + " (" + leadId + ")"); 
    window.open("productmanagement","_blank");
};


$(function () {
	$('#leadDealingDate').datetimepicker({
		dateFormat: 'yy-mm-dd H:MM'
	});
});



document.getElementById ("jump-to-product-button2").onclick = function () {
	var selectedData = sddms.dataTable2.row ('.selected').data ();
	var clientId = selectedData.clientId;
	$.ajax ({
		url : "clientmanagement/getClientForClientId?clientId="+clientId,
		type : 'POST',
		xhrFields: {
			withCredentials: true
		}
	}).done (function(data) {
		var clientName = data.name;
		sessionStorage.setItem ("leadInfo", clientName + " (" + clientId + ")");
		var selectedData = sddms.dataTable2.row ('.selected').data ();
		var productIdSend2 = selectedData.productId;
		sessionStorage.setItem ("productIdSent2", productIdSend2);
		window.open(
			'productmanagement',
			'_blank'
		);
	});
}


document.getElementById ("jump-to-product-button3").onclick = function () {
	window.open(
		'productmanagement',
		'_blank' // <- This is what makes it open in a new window.
	);
};


var productIdSend;
document.getElementById ("jump-to-orders-button3").onclick = function () {
	var selectedData = sddms.dataTable3.row ('.selected').data ();
	var productIdSend = selectedData.productId;
	sessionStorage.setItem ("productIdSent", productIdSend); 
	window.open(
		'orders',
		'_blank'
	);
};

document.getElementById ("jump-to-leads-button").onclick = function () {
	window.open(
		'clientmanagement',
		'_blank'
	);
};


setInterval(function refetchDisplayLeadCount () {
	$.ajax ({
		url : "clientmanagement/getLeadCountFromLeadTable",
		type : 'POST',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function(data) {
		$("#displayLeadCount").html(data);
	});
}, 1000);



setInterval(function refetchDisplayProdAvail () {
	$.ajax ({
		url : "indexM/getProdAvailCount",
		type : 'POST',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function(data) {
		$("#displayAvailableProducts").html(data);
	});
}, 1000);


setInterval (function refetchDisplayInStock () {
	$.ajax ({
		url : "indexM/getInStockCount",
		type : 'POST',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function(data) {
		$("#displayInStockProducts").html(data);
	});
}, 1000);


setInterval (function refetchDisplayNewProductsAdded () {
	$.ajax ({
		url : "indexM/getProdNewCount",
		type : 'POST',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function(data) {
		$("#displayNewProductsAdded").html(data);
	});
}, 1000);



$(document).ready(function() {
	$("#toggleCalHeading").click();
	
	$('#lead-from-manager-btn').click(function () {
		$('#lead-from-manager-table').dataTable().fnReloadAjax();
	});
	
	$('#prod-avail-btn').click(function () {
		$('#prod-avail-table').dataTable().fnReloadAjax();
	});
	
	$('#prod-instock-btn').click(function () {
		$('#prod-instock-table').dataTable().fnReloadAjax();
	});
	
	$('#prod-new-added-btn').click(function () {
		$('#prod-new-added-table').dataTable().fnReloadAjax();
	});
	
	
	$("#toggleCalHeading").click(function(){
	    if($(this).html() == "Click to View Calendar"){
	        $(this).html("Click to Hide Calendar");
	    }
	    else{
	        $(this).html("Click to View Calendar");
	    }
	});
	
	var initPage = function() {
		switchActiveTab('nav-home');
		var detailRows = [];
		var dTable_recentActivity = $('#recentActivity-table').DataTable ({bFilter: false, bSort: true, select : "single", sScrollY: "330px", type: "checkbox", ordering: true, bPaginate: false, bInfo: false});
		
		$.ajax({
			url: "indexS/recentActivity",
			type: 'POST',
			contentType: "application/json",
			xhrFields: {
				withCredentials: true
			}
		}).done(function(data) {
			dTable_recentActivity.clear();
			for (i= 0; i < data.length; ++i) {
				dTable_recentActivity.row.add ([
				    data [i].status + " " + data [i].details,
				    data [i].instance,
				]).draw (false);
			}
		});
		
		
		
		
		/* ________________________________________LEADS_____________________________________________________________*/
		
		sddms.dataTable = $('#lead-from-manager-table').DataTable ({
			'serverSide' : true,
			"bSortable": false,
			"bInfo" : false,
			"paging":   false,
		    "ordering": false,
		    "bFilter": false,
			'ajax' : {
				url: 'clientmanagement/fetchLeadClients',
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
				{ data: 'leadName' },
//				{ data: 'leadContact' },
//				{ data: 'leadEmail' },
				{ data: 'leadType' },
				{ data: 'leadRegion'}
			],
			select: "single",
		});
		
		/* _____________________________________________________________________________________________________*/
		
		
		
		
		
		/* ________________________________________PROD AVAIL_____________________________________________________________*/
		
		sddms.dataTable2 = $('#prod-avail-table').DataTable ({
			'serverSide' : true,
			"bSortable": false,
			"bInfo" : false,
			"paging":   false,
		    "ordering": false,
		    "bFilter": false,
			'ajax' : {
				url: 'indexM/getProdAvail',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
			    },
			    dataSrc: "prodAvailEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			columns: [
				{ data: 'productId' },
				{ data: 'productName' },
				{ data: 'clientId'}
			],
			select: "single",
		});
		
		
		sddms.dataTable2.on ('select', function () {
			$ ('#mark-as-done-2').prop ('disabled', false);
	    });
		
		sddms.dataTable2.on ('deselect', function () {
			$ ('#mark-as-done-2').prop ('disabled', true);
	    });
		
		
		$('#mark-as-done-2').click (sddms.deleteFromProdAvail);
		
		/* _____________________________________________________________________________________________________*/
		
		
		
		
		
		
		
		/* ________________________________________PROD IN STOCK_____________________________________________________________*/
		
		
		sddms.dataTable3 = $('#prod-instock-table').DataTable ({
			'serverSide' : true,
			"bSortable": false,
			"bInfo" : false,
			"paging":   false,
		    "ordering": false,
		    "bFilter": false,
			'ajax' : {
				url: 'indexM/getInStock',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
			    },
			    dataSrc: "inStockEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			columns: [
				{ data: 'productId' },
				{ data: 'productName' },
				{ data: 'productType'}
			],
			select: "single",
		});
		
		
		sddms.dataTable3.on ('select', function () {
			$ ('#jump-to-orders-button3').prop ('disabled', false);
			$ ('#mark-as-done-4').prop ('disabled', false);
	    });
		
		sddms.dataTable3.on ('deselect', function () {
			$ ('#mark-as-done-4').prop ('disabled', true);
			$ ('#jump-to-orders-button3').prop ('disabled', true);
	    });
		
		
		$('#mark-as-done-4').click (sddms.deleteFromInStock);
		
		/* _____________________________________________________________________________________________________*/
		
		
		
		
		/* ________________________________________PROD IN STOCK_____________________________________________________________*/
		
		
		sddms.dataTable4 = $('#prod-new-added-table').DataTable ({
			'serverSide' : true,
			"bSortable": false,
			"bInfo" : false,
			"paging":   false,
		    "ordering": false,
		    "bFilter": false,
			'ajax' : {
				url: 'indexM/getProdNew',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
			    },
			    dataSrc: "inStockEntities", // Same Entity as InStock
				xhrFields: {
					withCredentials: true
				}
			},
			columns: [
				{ data: 'productId' },
				{ data: 'productName' },
				{ data: 'productType'}
			],
			select: "single",
		});
		
		
		sddms.dataTable4.on ('select', function () {
			$ ('#mark-as-done-6').prop ('disabled', false);
	    });
		
		sddms.dataTable4.on ('deselect', function () {
			$ ('#mark-as-done-6').prop ('disabled', true);
	    });
		
		
		$('#mark-as-done-6').click (sddms.deleteFromNewProductsAdded);
		
		/* _____________________________________________________________________________________________________*/
		
		
		
		
		
		
		
		
		/** _____________________ Disable and Enable Buttons__________________ **/
		
		sddms.dataTable.on ('select', function () {
			$ ('#lead-select-button').prop ('disabled', false);
	    });
		
		sddms.dataTable.on ('deselect', function () {
			$ ('#lead-select-button').prop ('disabled', true);
	    });
		
		
		$('#lead-select-button').click (sddms.leadDetailsButton);
		$('#backFromLeadDetails-btn').click (backFromLeadDetails);
		$('#backFromSetReminderForLead-btn').click (backFromSetReminderForLead);
		$('#set-reminder-for-lead-button').click (setReminderForLead);
		$('#confirm-reminder-for-lead-btn').click (confirmReminderForLead);
		
		
		$('#disqualify-lead-button').click (sddms.disqualifyLead);
		
//		$('#prod-instock-btn').click (sddms.disqualifyLead);
		
		
		
		/** ___________________________________________________________________ **/
		
		
	};
	initPage ();
});



sddms.deleteFromProdAvail = function(evt) {
	var productId = sddms.dataTable2.data () [sddms.dataTable2.row ('.selected') [0]].productId;
	console.log (productId);
	swal ({	title: "Are you done ?",
			text: "",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, I'm Done!",   
			closeOnConfirm: false 
	 	}, 
		function() {
	 		$.ajax ({
				url : "deleteFromProdAvail?productId=" + productId,
				type : 'DELETE',
				xhrFields: {
			      withCredentials: true
				}
	 		}).done(function() {
	 			swal ("Great !", "", "success");
				$('#prod-avail-modal').modal('hide');
				$('#prod-avail-table').dataTable().fnReloadAjax();
//				refetchDisplayProdAvail ();
			});
	 	}
	);
}

sddms.deleteFromInStock = function(evt) {
	var productId = sddms.dataTable3.data () [sddms.dataTable3.row ('.selected') [0]].productId;
	console.log (productId);
	swal ({	title: "Are you done ?",
			text: "",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, I'm Done!",   
			closeOnConfirm: false 
	 	}, 
		function() {
	 		$.ajax ({
				url : "deleteFromInStock?productId=" + productId,
				type : 'DELETE',
				xhrFields: {
			      withCredentials: true
				}
	 		}).done(function() {
				$('#prod-instock-modal').modal('hide');
				$('#prod-instock-table').dataTable().fnReloadAjax();
//				refetchDisplayInStock();
				swal ("Great !", "", "success");
			});
	 	}
	);
}




sddms.deleteFromNewProductsAdded = function(evt) {
	var productId = sddms.dataTable4.data () [sddms.dataTable4.row ('.selected') [0]].productId;
	console.log (productId);
	swal ({	title: "Are you done ?",
			text: "",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, I'm Done!",   
			closeOnConfirm: false 
	 	}, 
		function() {
	 		$.ajax ({
				url : "deleteFromNewProd?productId=" + productId,
				type : 'DELETE',
				xhrFields: {
			      withCredentials: true
				}
	 		}).done(function() {
//				$('#prod-new-added-modal').modal('hide');
				$('#prod-new-added-table').dataTable().fnReloadAjax();
//				refetchDisplayNewProductsAdded();
				swal ("Great !", "", "success");
			});
	 	}
	);
}





sddms.disqualifyLead = function(evt) {
	var clientId = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].clientId;
	swal ({	title: "Are you sure?",
			text: "The lead will be added to special attention list!",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, Disqualify Lead!",   
			closeOnConfirm: false 
	 	}, 
		function() {
	 		$.ajax ({
				url : "disqualifyLeadAndDeleteFromCal?clientId=" + clientId,
				type : 'POST',
				xhrFields: {
			      withCredentials: true
				}
	 		}).done(function() {
	 			swal ("Lead Disqualified !", "The selected Lead has been moved to special attention list.", "success");
				$('#lead-details-modal').modal('hide');
				$('#lead-from-manager-table').dataTable().fnReloadAjax();
				$('#lead-from-manager-modal').modal('show');
//				refetchDisplayLeadCount ();
			});
	 	}
	);
};



function backFromLeadDetails () {
	$('#lead-from-manager-modal').modal('show');
}

function backFromSetReminderForLead () {
	$('#lead-from-manager-modal').modal('show');
}


function setReminderForLead () {
	$('#lead-details-modal').modal('hide');
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
		
		// Refetch the displayLeadCount
//		refetchDisplayLeadCount ();
		
		// Reload Datatable
		$('#lead-from-manager-table').dataTable().fnReloadAjax();
		
		// Re-fetch Events for Calendar
		$('#calendar').fullCalendar ('refetchEvents');
		$('#calendar2').fullCalendar ('refetchEvents');
		
	});
}


var selectedDataForLead;
sddms.leadDetailsButton = function (evt) {
	selectedDataForLead = sddms.dataTable.row ('.selected').data ();
	console.log (selectedDataForLead);
	// Fill up the details (For Lead Info Panel & Setting the Remainder Panel)
	$("#displayLeadId").html(selectedDataForLead.clientId);
	$("#displayLeadId_7").html(selectedDataForLead.clientId);
	$("#displayLeadName").html(selectedDataForLead.leadName);
	$("#displayLeadName_7").html(selectedDataForLead.leadName);
	$("#displayLeadType").html(selectedDataForLead.leadType);
	$("#displayLeadType_7").html(selectedDataForLead.leadType);
	$("#displayLeadRegion").html(selectedDataForLead.leadRegion);
	$("#displayLeadPhone").html(selectedDataForLead.leadContact);
	$("#displayLeadPhone_7").html(selectedDataForLead.leadContact);
	$("#displayLeadEmail").html(selectedDataForLead.leadEmail);
	$("#displayLeadEmail_7").html(selectedDataForLead.leadEmail);
	$("#displayLeadAddress").html(selectedDataForLead.leadAddress);
	
	// Store Lead Id for view products via session
	leadId = selectedDataForLead.clientId;
	
	// Store Lead Name for view products via session
	leadName = selectedDataForLead.leadName;
	
	// Hide Current Modal
	$('#lead-from-manager-modal').modal('hide');
	
	// Show Next Modal (Lead Details) Shown from html
	
	
	// Send the selected data for setting the reminder
};



/** _________________________________________________________________________________________________________________________________________________________________________________________________ **/
