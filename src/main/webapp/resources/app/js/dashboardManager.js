function format_details (d) {
	var note = " ";
	if (d.noteToTheManager) {
		note = d.noteToTheManager;
	}
	return '<table><thead><tr><th><span class="label label-info"> NOTE </span></th></tr>'  +
			'<tr><th><br><span class="label label-default"> Nearest Date of Delivery:</span></th><th> ' + '<br>' + d.nearestDateOfDelivery +
			'</th></tr><tr><th><br><span class="label label-default"> Client Affected:</span></th><th> ' + '<br>' + d.clientAffected +
			'</th></tr><tr><th><br><span class="label label-default"> Note from the representative:</span></th><th> ' + '<br>' + note +
			'</th></tr></thead></table>';
}



document.getElementById ("jump-to-products-8").onclick = function () {
	window.open(
		'productmanagement',
		'_blank'
	);
};


setInterval(function refetchNumberOfProductsRequested () {
	$.ajax ({
		url : "dashboardManager/getCountFromProductRequestTable",
		type : 'POST',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function(data) {
		$("#displayNumberOfProductsRequested").html(data);
	});
}, 1000);


setInterval(function refetchNumberOfProductsShortage () {
	$.ajax ({
		url : "dashboardManager/getCountFromProductShortageTable",
		type : 'POST',
		xhrFields: {
	      withCredentials: true
	   }
	}).done (function(data) {
		$("#displayNumberOfProductShortage").html(data);
	});
}, 1000);


var productIdList = new Array ();

function autoCompleteProductId2 () {
	$.ajax ({
		url: 'productmanagement/getProductNameAndId',
		type: 'POST',
		contentType: "application/json",
	}).done (function(data) {
		for (i = 0; i < data.length; ++i) {
			productIdList.push (data[i].productId);
		}
		$("#productId2").shieldAutoComplete({
	        dataSource: {
	            data: productIdList
	        },
	        minLength: 1
	    });
	});
}


//

var lineOptions = {
        scaleShowGridLines: true,
        scaleGridLineColor: "rgba(0,0,0,.05)",
        scaleGridLineWidth: 1,
        bezierCurve: true,
        bezierCurveTension: 0.4,
        pointDot: true,
        pointDotRadius: 4,
        pointDotStrokeWidth: 1,
        pointHitDetectionRadius: 20,
        datasetStroke: true,
        datasetStrokeWidth: 2,
        datasetFill: true,
        responsive: true,
        multiTooltipTemplate: "<%= datasetLabel %> - $<%= value %>"
    };

/*var barOptions = {
    scaleBeginAtZero: true,
    scaleShowGridLines: true,
    scaleGridLineColor: "rgba(0,0,0,.05)",
    scaleGridLineWidth: 1,
    barShowStroke: true,
    barStrokeWidth: 2,
    barValueSpacing: 5,
    barDatasetSpacing: 1,
    responsive: true,
    multiTooltipTemplate: "<%= datasetLabel %> - $<%= value %>"
};*/

var target = [65, 59, 80, 75, 56, 55, 40, 64, 58, 55, 52]; // last is 53
var sales = [28, 48, 51, 53, 75, 52, 68, 64, 54, 52, 58];

var Data = {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        datasets: [
            {
                label: "Target",
                fillColor: "rgba(220,220,220,0.5)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)"
                //data: [65, 59, 80, 75, 56, 55, 40, 64, 58, 55, 52, 53]
            },
            {
                label: "Achieved",
                fillColor: "rgba(26,179,148,0.5)",
                strokeColor: "rgba(26,179,148,0.7)",
                pointColor: "rgba(26,179,148,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(26,179,148,1)"
                //data: [28, 48, 51, 53, 75, 52, 68, 64, 54, 50, 45]
            }
        ]
    };



//

function update() {	
	gnt = $("#nt").val();
	console.log(gnt);
	
	$.ajax({
	      url: "./updateTarget?target="+gnt,
	      type : 'POST',
	      contentType : "application/json",
	      }).done(function () {
	    	  console.log("updated succesfuly");
	    	  $('#cat-add-modal').modal('hide');
	    	  swal("Done!", "Sales revenue target updated successfully!", "success")
	    	  dChart();
	      });
}

function update2() {
	var v1 = $("#v1").val();
	var v2 = $("#v2").val();
	var v3 = $("#v3").val();
	$.ajax({
	      url: "./updateSRTarget?v1="+v1+"&v2="+v2+"&v3="+v3,
	      type : 'POST',
	      contentType : "application/json",
	      }).done(function () {
	    	  console.log("updated succesfuly");
	    	  $('#cat-add-modal2').modal('hide');
	    	  swal("Done!", "Sales revenue target distributed successfully!", "success")
	    	  $('#dist').prop('disabled', true);
//	    	  dChart();
	      });
}

var dtarget;
function dChart() {
	
	$("#cdiv").html('');
	$("#cdiv").html('<canvas id="lineChart" height="60"></canvas>');
	
	Data.datasets[0].data = target;
	Data.datasets[1].data = sales;
	
	$.ajax({
	      url: "./getTarget",
	      type : 'POST',
	      contentType : "application/json",
	      }).done(function (d) {
	    	  if(d!=0) {
	    		  console.log("hanji");
	    		  Data.datasets[0].data.push(d);
	    		  $('.update').prop('disabled', true);
	    		  
	    		  //   target work modal
	    		  dtarget = d*1000;
	    		  $("#target").val("$ "+d*1000);
	    		  $("#p1").val(40);
	    		  $("#p2").val(35);
	    		  $("#p3").val(25);
	    		  $("#v1").val(0.4*d*1000);
	    		  $("#v2").val(0.35*d*1000);
	    		  $("#v3").val(0.25*d*1000);
	    	  }
	    	  var ctx = document.getElementById("lineChart").getContext("2d");
	    	  var myNewChart = new Chart(ctx).Line(Data, lineOptions);
	    	  
	      });
}

//------------------------------sales rep performance data-------------------------
var Data0 = {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November"],
        datasets: [
            {
                label: "Example dataset",
                fillColor: "rgba(220,220,220,0.5)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: [65, 59, 80, 81, 56, 55, 40, 67, 75, 34, 69]
            }
        ]
    };

var Data1 = {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November"],
        datasets: [
            {
                label: "Example dataset",
                fillColor: "rgba(220,220,220,0.5)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: [65, 59, 80, 56 ,78,45, 81, 56, 55, 40, 67]
            }
        ]
    };

var Data2 = {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November"],
        datasets: [
            {
                label: "Example dataset",
                fillColor: "rgba(220,220,220,0.5)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: [65, 59, 80, 81, 56, 56, 42, 79, 89, 55, 40]
            }
        ]
    };

//----------------------end data------------------------
var sridx;
function spChart() {
	var spChart = new FusionCharts({
        type: 'doughnut2d',
        renderAt: 'chart-container2',
        width: '450',
        height: '450',
        dataFormat: 'json',
        dataSource: {
            "chart": {
                "caption": "<br> Split of Revenue by Sales Representatives",
                "subCaption": "<br />Overall",
                "numberPrefix": "$",
                "paletteColors": "#0075c2,#1aaf5d,#f2c500",
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
                "defaultCenterLabel": "Total revenue: $110K",
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
                    "label": "Sale Rep 1",
                    "value": 44
                },
                {
                    "label": "Sale Rep 2",
                    "value": 38.5
                },
                {
                    "label": "Sale Rep 3",
                    "value": 27.5
                }
            ]
        },
        "events": {

        /**
         * @description
         * This event is triggered when a pie or a doughnut slice begins slicing out/slicing in.
         *
         * @param {Object} eventObj: An object containing all the details related to this event like eventId, sender, etc.
         * @param {Object} dataObj: An object containing all the details related to chart data, such as the data index of the slice being sliced out.
         */

            "slicingStart": function (eventObj, dataObj) {
            //console.log(dataObj.dataIndex);            
            	sridx = dataObj.dataIndex;
            	$('#cat-add-modal3').modal('show');
        }
    }
    }).render();
}

function sperfChart() {
	console.log("hanji in chart");
	
	$("#cdiv1").html('');
	$("#cdiv1").html('<canvas id="lineChart1" height="60"></canvas>');
	var ctx = document.getElementById("lineChart1").getContext("2d");
//	/var myNewChart = new Chart(ctx).Line("Data"+sridx, lineOptions);
	if(sridx == 0) {
		$("#myModalLabel1").html("Sales Rep 1 Performance - Current Year");
		var myNewChart = new Chart(ctx).Line(Data0, lineOptions);
	}
	else if(sridx == 1) {
		$("#myModalLabel1").html("Sales Rep 2 Performance - Current Year");
		var myNewChart = new Chart(ctx).Line(Data1, lineOptions);
	}
	else if(sridx == 2) {
		$("#myModalLabel1").html("Sales Rep 3 Performance - Current Year");
		var myNewChart = new Chart(ctx).Line(Data2, lineOptions);
	}
}

function slider () {
	$( "#slider-range" ).slider({
		range: true,
		min: 0,
		max: 100,
		values: [ 40, 75 ],
		slide: function( event, ui ) {
			$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
			$("#p1").val(ui.values[ 0 ]);
			$("#v1").val(dtarget*0.01*$("#p1").val());
			$("#p2").val(ui.values[ 1 ] - ui.values[ 0 ]);
			$("#v2").val(dtarget*0.01*$("#p2").val());
			$("#p3").val(100 - ui.values[ 1 ]);
			$("#v3").val(dtarget*0.01*$("#p3").val());
		}
	});
}
$(document).ready(function() {


	//
	dChart();
	spChart();
	$('#cat-add-modal3').on('shown.bs.modal', function () {
		sperfChart();
	});

    $("#rtarget").val('52');
    $('#cr').val('58');
    $('#rtargetp').val('November');
    $("#gr").val('0.12');
    $("#nt").val('64.69');

    $('#cat-add-button').click(update);
    $('#cat-add-button2').click(update2);
    slider();
	//


    $("#toggleManagerNotifs").click();

    $("#toggleManagerNotifs").click(function(){
	    if($(this).html() == "View Notifications"){
	        $(this).html("Hide Notifications");
	    }
	    else{
	        $(this).html("View Notifications");
	    }
	});

	$('#product-requested-btn').click(function () {
		$('#productRequest-table').dataTable().fnReloadAjax();
	});

	$('#product-shortage-btn').click(function () {
		$('#productShortage-table').dataTable().fnReloadAjax();
	});

    var initPage = function() {
        switchActiveTab('nav-home');
        var detailRows = [];

        /* __________________________________________PRODUCT SHORTAGE TABLE______________________________________________________________________________ */

        sddms.dataTable = $('#productShortage-table').DataTable ({
        	bInfo: false,
        	bLengthChange: false,
        	autoFill: true,
        	iDisplayLength: 7,
        	'ajax' : {
        		url: 'indexM/fetchProductShortage',
        		type: 'POST',
        		contentType: "application/json",
        		data: function (d) {
        			delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
        		},
        		dataSrc: "productShortageEntityForManager",
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
  				{ data: 'productName' },
  				{ data: 'shortageQuantity' },
  				{ data: 'status' }
  			],
  			select: "single",
  			filter: true
        });


        /* _____________________________________________________SEARCH FOR COLUMN STARTS___________________________________________________ */

	    // Setup - add a text input to each footer cell
	    $('#productShortage-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder = "'+title+'"/>' );
	        }
	    } );

	    // DataTable
	    var table = $('#productShortage-table').DataTable();

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

	    $('#productShortage-table tbody').on( 'click', 'tr td.details-control', function () {
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

	    /* _____________________________________________________ROW DETAIL ENDS___________________________________________________ */





        sddms.dataTable.on ('deselect', function () {
			$ ('#product-add-modal-btn').prop ('disabled', true);
	    });

        sddms.dataTable.on ('select', function () {
			$ ('#product-add-modal-btn').prop ('disabled', false);
	    });


        /* __________________________________________PRODUCT SHORTAGE TABLE (ENDS)______________________________________________________________________________ */








        /* __________________________________________PRODUCT SHORTAGE TABLE______________________________________________________________________________ */

        sddms.dataTable2 = $('#productRequest-table').DataTable ({
        	bInfo: false,
        	bLengthChange: false,
        	autoFill: true,
        	iDisplayLength: 7,
        	'ajax' : {
        		url: 'indexM/fetchProductRequest',
        		type: 'POST',
        		contentType: "application/json",
        		data: function (d) {
        			delete (d.columns);
					delete (d.order);
					delete (d.search);
					return JSON.stringify (d);
        		},
        		dataSrc: "productRequestEntity",
        		xhrFields: {
					withCredentials: true
				}
        	},
        	columns: [
  				{ data: 'productName' },
  				{ data: 'productCount' }
  			],
  			select: "single",
  			filter: true
        });



/* _____________________________________________________SEARCH FOR COLUMN STARTS___________________________________________________ */

	    // Setup - add a text input to each footer cell
	    $('#productRequest-table tfoot th').each( function () {
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder = "'+title+'"/>' );
	        }
	    } );

	    // DataTable
	    var table = $('#productRequest-table').DataTable();

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





        sddms.dataTable2.on ('deselect', function () {
			$ ('#product-add-modal-btn2').prop ('disabled', true);
	    });

        sddms.dataTable2.on ('select', function () {
			$ ('#product-add-modal-btn2').prop ('disabled', false);
	    });


        $ ('#product-add-modal-btn2').on ('click', function () {
            var selectedData = sddms.dataTable2.row ('.selected').data ();
            $('#productName2').prop ('readonly',true)
            $('#productQuantityRequested2').prop ('readonly',true)

            $('#productName2').val (selectedData.productName);
            $('#productQuantityRequested2').val (selectedData.productCount);
            autoCompleteProductId2 ();
        });




        /* __________________________________________PRODUCT REQUEST TABLE (ENDS)______________________________________________________________________________ */





        $('#product-add-button').click (sddms.addProductQuantity);
        $('#product-add-button2').click (sddms.addRequestedProduct);



        $ ('#cancel-product-add').on ('click', function () {
        	$('#product-out-of-stock-modal').show();
        });

        $ ('#product-add-modal-btn').on ('click', function () {
//        	$('#product-out-of-stock-modal').hide();
            var selectedData = sddms.dataTable.row ('.selected').data ();
            console.log ("Add : ", selectedData);
            $('#productId').prop ('readonly',true)
            $('#productName').prop ('readonly',true)
            $('#productId').val (selectedData.productId);
            $('#productName').val (selectedData.productName);
        });
    }




	/*_________________________________________________CHART (STARTS)  ________________________________________________________________________________*/

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



    initPage();
});


$(function() {
    $( "#product-add-modal" ).draggable();
    $( "#message-to-representative-modal-btn" ).draggable();
});


/** ______________________________________ADD PRODUCT QUANTITY STARTS________________________________________________________**/

sddms.addProductQuantity = function(evt) {
	console.log ("\nInside add product quantity");
	var formData = $ ('#product-add-form').serializeObject();
	console.log ("Boom: ", formData);
	formData.quantity = formData.addProductQuantity;
	delete (formData.addProductQuantity);
	$.ajax({
		url : 'dashboardManager/addProductQuantity',
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
//		refetchNumberOfProductsShortage ();
		$('#productShortage-table').dataTable().fnReloadAjax();
		$('#product-add-modal').modal('hide');
		$('#product-out-of-stock-modal').modal('show');
		swal ("Good job!", "The product is successfully added, the out of stock status is updated and the information has been passed to the sales representative!", "success");
	});
};

/** ______________________________________ADD PRODUCT QUANTITY ENDS________________________________________________________**/





/** ______________________________________ADD PRODUCT STARTS________________________________________________________**/

sddms.addRequestedProduct = function(evt) {
	console.log ("\nInside add product");
	var formData = $ ('#product-add-form2').serializeObject();
	console.log ("formData ==> : ", formData);
	$.ajax({
		url : 'indexM/addProductRequested',
		data : JSON.stringify (formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
//		refetchNumberOfProductsRequested ();
		$('#product-add-modal2').modal('hide');
		$('#productRequest-table').dataTable().fnReloadAjax();
		$('#product-requested-modal').modal('show');
		swal ("Good job!", "The product is successfully added!", "success");
	});
};

/** ______________________________________ADD PRODUCT ENDS________________________________________________________**/
