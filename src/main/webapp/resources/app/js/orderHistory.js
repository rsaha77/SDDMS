function format (d) {
	if (d.delayed ==  ("N / A")) {
		return '<span class="label label-warning"> REASON FOR CANCELLATION </span> <br>' + ' > ' + d.orderCancellationReason + '<br>';
	} else {
		return '<span class="label label-info"> NOTE </span> <br>';
	}
}

$(document).ready (function () {
	var initPage = function () {
		switchActiveTab ('nav-orderHistory');
		var detailRows = [];
		sddms.dataTable = $('#order-history-table').DataTable({
//			'serverSide' : true,
			'ajax' : {
				url: 'order/fetchOrderHistoryTable',
				type: 'POST',
				contentType: "application/json",
				data: function (d) {
					// send only data required by backend API
					delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
				},
				dataSrc: "orderHistoryEntities",
				xhrFields: {
					withCredentials: true
				}
			},
			columns: [					 // Same as orderHistoryDetailsEntity
				{
				    "class":          "details-control",
				    "orderable":      false,
				    "data":           null,
				    "defaultContent": ""
				},
				{ data: 'status' },
				{ data: 'orderId' },
				{ data: 'clientId' },
				{ data: 'clientName' },
				{ data: 'clientContact' },
				{ data: 'clientAddress' },
				{ data: 'productId' },
				{ data: 'productName'},
				{ data: 'orderQuantity' },
				{ data: 'deliveryDate' },
				{ data: 'deliveryDate2' },
				{ data: 'delayed' }
			],
			select: "single"
		});
		
		
		sddms.dataTable.on ('select', function () {
			$ ('#order-history-delete-modal-btn').prop ('disabled', false);
	    });
		
		sddms.dataTable.on ('deselect', function () {
			$ ('#order-history-delete-modal-btn').prop ('disabled', true);
	    });
		
		$('#order-history-delete-modal-btn').click (sddms.deleteOrderHistory);
		
/* _____________________________________________________SEARCH FOR COLUMN STARTS___________________________________________________ */
		
	    // Setup - add a text input to each footer cell
	    $('#order-history-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder = "'+title+'"/>' );
	        }
	    } );
	 
	    // DataTable
	    var table = $('#order-history-table').DataTable();
	 
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
		
	    $('#order-history-table tbody').on( 'click', 'tr td.details-control', function () {
	        var tr = $(this).closest ('tr');
	        var row = sddms.dataTable.row (tr);
	        var idx = $.inArray (tr.attr('id'), detailRows);
	 
	        if (!row.child.isShown ()) {
	            tr.addClass ('details');
	            row.child (format (row.data())).show ();

	            // Add to the 'open' array
	            if (idx === -1) {
	                detailRows.push (tr.attr('id'));
	            }
	        }
	        else {
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

	};
	initPage ();
});


sddms.deleteOrderHistory = function(evt) {
	swal ({	title: "Are you sure?",
			text: "You will not be able to recover this order history!",
			type: "warning",   showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   confirmButtonText: "Yes, delete it!",   
			closeOnConfirm: false 
		 }, 
			
		function() { 
			console.log ("Inside Delete History");
			var orderId = sddms.dataTable.data () [sddms.dataTable.row ('.selected') [0]].orderId;
			console.log (orderId);
			$.ajax({
				url : "orderHistory/deleteOrderHistory?orderId=" + orderId,
				type : 'DELETE',
				xhrFields: {
			      withCredentials: true
			   }
			}).done(function() {
				$('#order-history-delete-modal').modal('hide');
				$('#order-history-table').dataTable().fnReloadAjax();
				swal ("Deleted!", "The selected Order History has been deleted.", "success");
			});
		}
	);
};






