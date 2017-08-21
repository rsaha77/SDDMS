$(function () {
//	$('#orderDate').datetimepicker({
//		dateFormat: 'yy-mm-dd HH:MM'
//	});
});

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

$(document).ready (function () {
	
	var receivedProductId = sessionStorage.getItem ("productIdSent");
	// Action carried out later
	
	switchActiveTab('nav-orders');
	var initPage = function () {
		sddms.dataTable = $('#order-table').DataTable({
//			'serverSide' : true,
			'ajax' : {
				url: 'order/fetchOrderTable',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					// send only data required by backend API
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					console.log ("Inside ajax ... Order.JS");
					console.log (JSON.stringify (d));
					return JSON.stringify (d);
				},
				dataSrc: "orderDetailsEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			columns: [
	          { data: 'orderId' },
	          { data: 'clientId' },
	          { data: 'clientName' },
	          { data: 'proId' },
	          { data: 'productName'}, // Same as orderDetailsEntity
	          { data: 'orderQuantity' },
	          { data: 'orderDate' },
	          { data: 'transport' },
	          { data: 'status' }
			],
			select: "single"
		});
		
		$('#order-modify-button').click (sddms.modifyOrderQuantity);
//		$('#reportOutOfStock-confirm-button').click (sddms.reportOutOfStock);
		$('#orderDetails-modal-btn').click (sddms.getOrderDetails);
		$('#order-process-button').click(sddms.orderProcess);
		
		sddms.dataTable.on ('select', function () {
			$ ('#order-delete-modal-btn').prop ('disabled', false);
			$ ('#order-modify-modal-btn').prop ('disabled', false);
			$ ('#order-order-modal-btn').prop ('disabled', false);
			$ ('#check-product-modal-btn').prop ('disabled', false);
			$ ('#order-process-modal-btn').prop ('disabled', false);
//			$ ('#reportOutOfStock-modal-btn').prop ('disabled', false);
			$ ('#orderDetails-modal-btn').prop ('disabled', false);
	    });
		
		sddms.dataTable.on ('deselect', function () {
			$ ('#order-delete-modal-btn').prop ('disabled', true);
			$ ('#order-modify-modal-btn').prop ('disabled', true);
			$ ('#order-order-modal-btn').prop ('disabled', true);
			$ ('#check-product-modal-btn').prop ('disabled', true);
			$ ('#order-process-modal-btn').prop ('disabled', true);
//			$ ('#reportOutOfStock-modal-btn').prop ('disabled', true);
			$ ('#orderDetails-modal-btn').prop ('disabled', true);
	    });
		
		$ ('#order-process-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
//            console.log("SelectedData", selectedData);
            $('#orderId').prop ('readonly',true)
            $('#clientId').prop ('readonly',true)
            $('#clientName').prop ('readonly',true)
            $('#proId').prop ('readonly',true)
            $('#productName').prop ('readonly',true)
            $('#productCount').prop ('readonly',true)
            $('#orderQuantity').prop ('readonly',true)
            $('#orderDate').prop ('readonly',true)
            $('#orderId').val (selectedData.orderId);
            $('#clientId').val (selectedData.clientId);
            $('#clientName').val (selectedData.clientName);
            $('#proId').val (selectedData.proId);
            $('#productName').val (selectedData.productName);
            $('#orderQuantity').val (selectedData.orderQuantity);
            $('#orderDate').val (getCurrentDateAndTime ());
        });
		
		$ ('#order-delete-modal-btn').on ('click', function () {
            var selectedData = sddms.dataTable.row ('.selected').data ();
            $('#orderId_2').prop ('readonly',true)
            $('#orderId_2').val (selectedData.orderId);
        });
		$('#order-delete-button').click (sddms.deleteOrder);
		
		
		$ ('#order-modify-modal-btn').on ('click', function () {
			var selectedData = sddms.dataTable.row ('.selected').data ();
			console.log("SelectedData", selectedData);
			$('#orderId_1').prop ('readonly',true)
			$('#orderId_1').val (selectedData.orderId);
			$('#orderQCurrent').prop ('readonly',true)
			$('#orderQCurrent').val (selectedData.orderQuantity);
		});
		
		
//		$ ('#reportOutOfStock-modal-btn').on ('click', function () {
//			var selectedData = sddms.dataTable.row ('.selected').data ();
//            console.log (selectedData);
//            $('#proId_2').prop ('readonly',true)
//            $('#productName_2').prop ('readonly',true)
//            $('#dateOfDelivery').prop ('readonly',true)
//            $('#dateOfDelivery').prop ('readonly',true)
//            $('#clientAffected').prop ('readonly',true)
//            $('#proId_2').val (selectedData.proId);
//            $('#productName_2').val (selectedData.productName);
//            $('#dateOfDelivery').val (selectedData.orderDate);
//            $('#clientAffected').val (selectedData.clientId);
//        });
		
//		$("#reportOutOfStock-modal-btn").draggable({handle:".modal-header"});
		
		/* _____________________________________________________SEARCH FOR COLUMN STARTS___________________________________________________ */
		
	    // Setup - add a text input to each footer cell
	    $('#order-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	var myId = title + "forOrders";
	        	myId = myId.replace (" ", "");
	        	$(this).html( '<input type="text" id = "'+myId+'" placeholder = "'+title+'"/>' );
	        }
	    } );
	 
	    
	    // DataTable
	    var table = $('#order-table').DataTable();
	 
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
	    
	    if (receivedProductId != null || receivedProductId != "") {
			console.log ("Hello => ", receivedProductId);
			$('#ProductIdforOrders').val(receivedProductId);
			$('#ProductIdforOrders').keyup();
		}
	    
	    /* _____________________________________________________SEARCH FOR COLUMN ENDS___________________________________________________ */
	    
	};
	initPage ();
});

//$(function() {
//    $( "#order-process-modal" ).draggable();
//    $( "#reportOutOfStock-modal" ).draggable();
//    $( "#order-modify-modal" ).draggable();
//    $( "#order-delete-modal" ).draggable();
//});


//var e = document.getElementById ('order-modify-modal-btn');
//e.onmouseover = function() {
//  document.getElementById ('popup').style.display = 'block';
//}
//e.onmouseout = function() {
//  document.getElementById('popup').style.display = 'none';
//}

//$(".tiptext").mouseover(function() {
//    $(this).children(".description").show();
//}).mouseout(function() {
//    $(this).children(".description").hide();
//});

function CheckReasonForCancellation (val) {
	var element=document.getElementById ('reason');
	if(val == 'others') {
		element.style.display='block';
	} else {
		element.style.display='none';
	}
}


/** ______________________________________PROCESSING AN ORDER STARTS________________________________________________**/


sddms.orderProcess = function(evt) {
	console.log ("\nInside order process");
	var formData = $ ('#order-process-form').serializeObject();
	formData.productId = formData.proId;
	delete (formData.proId);
	console.log (formData);
	$.ajax({
		url : "order/orderProcess",
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#order-process-modal').modal('hide');
		$('#order-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The order is successfully processed!", "success");
	});
};

/** ______________________________________PROCESSING AN ORDER ENDS____________________________________________________**/




/** ______________________________________MODIFY ORDER QUANTITY________________________________________________**/


sddms.modifyOrderQuantity = function(evt) {
	console.log ("\nInside modify order");
	var tableSelectedData = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]];
	var formData = $ ('#order-modify-form').serializeObject();
	var newFormData = {
		orderId: tableSelectedData.orderId,
		productId: tableSelectedData.proId,
		orderQuantityOld: tableSelectedData.orderQuantity,
		orderQuantityNew: formData.orderQuantity,
	}
	console.log (newFormData);
	$.ajax({
		url : "order/modifyOrderQuantity",
		data : JSON.stringify (newFormData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#order-modify-modal').modal('hide');
		$('#order-table').dataTable().fnReloadAjax();
		swal ("Good job!", "The order quantity has been successfully modified!", "success");
	});
};

/** ______________________________________MODIFY ORDER QUANTITY ENDS____________________________________________________**/



/** ______________________________________REPORT SHORTAGE STARTS________________________________________________________**/


//sddms.reportOutOfStock = function (evt) { // This function will only update the NOTE TO THE MANAGER .. (URGENCY WILL BE NOTIFIED ON IT'S OWN) [Take Care of that while fetching the product table / (or better as soon as the product goes out of stock)]
//	console.log ("\nInside Report Out Of Stock");
//	var formData = $ ('#reportOutOfStock-form').serializeObject();
//	var arr = formData.clientAffected.split ('_');
//	formData.clientAffected = arr[0];
//	formData.clientAffectedId = arr[1];
//	formData.productId = formData.proId;
//	delete (formData.proId);
//	console.log ("Form Data", formData);
//	$.ajax({
//		url : "order/reportProductShortageInfo",
//		data : JSON.stringify (formData),
//		type : 'POST',
//		contentType : "application/json",
//		xhrFields: {
//	      withCredentials: true
//	   }
//	}).done(function() {
//		$('#reportOutOfStock-modal').modal('hide');
//		$('#product-table').dataTable().fnReloadAjax();
//		swal ("Good job!", "The out of stock information is successfully reported to the manager!", "success");
//	});
//};

/** ______________________________________REPORT SHORTAGE ENDS________________________________________________________**/




sddms.getOrderDetails = function (evt) {
	console.log ("To get order details");
}



/** ______________________________________DELETE PRODUCT	________________________________________________________**/

sddms.deleteOrder = function (evt) {
	console.log ("\nInside Delete");
	var tableSelectedData = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]];
	var formData = $ ('#order-delete-form').serializeObject();
	console.log ("tableSelectedData : ", tableSelectedData);
	console.log ("formData : ", formData);
	var newFormData = {
		orderId: tableSelectedData.orderId,
		productId: tableSelectedData.proId,
		productName: tableSelectedData.productName,
		orderQuantity: tableSelectedData.orderQuantity,
		orderDate: tableSelectedData.orderDate,
		clientId: tableSelectedData.clientId,
		reasonManual: formData.reasonManual,
		reasonOther: formData.reasonOther
	};
	console.log ("newFormData : ", newFormData);
	$.ajax({
		url : "order/deleteOrder",
		data : JSON.stringify (newFormData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#order-delete-modal').modal ('hide');
		$('#order-table').dataTable().fnReloadAjax();
	});
	swal ("Deleted!", "The selected Order has been cancelled and the cancellation reason along with the details has been updated at the order history !", "success");
};

/** ______________________________________DELETE PRODUCT ENDS________________________________________________________**/



