function formatDate(date) {
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	  var ampm = hours >= 12 ? 'pm' : 'am';
	  hours = hours % 12;
	  hours = hours ? hours : 12; // the hour '0' should be '12'
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var strTime = hours + ':' + minutes + ' ' + ampm;
	  return date.getDate() + "/" + (date.getMonth()+1) + "/" + date.getFullYear() + " " + strTime;
//	  return date.getMonth()+1 + "/" + date.getDate() + "/" + date.getFullYear() + " " + strTime;
}

$(document).ready(function() {
	var initPage = function() {
		
		
		$('#datetimepicker1').datetimepicker({
//			sideBySide: true,
			format: 'yyyy-mm-dd HH:mm:ss',
		});
        $('#datetimepicker2').datetimepicker({
//        	sideBySide: true,
			format: 'yyyy-mm-dd HH:mm:ss',
            useCurrent: false //Important! See issue #1075
        });
        $("#datetimepicker1").on("dp.change", function (e) {
            $('#datetimepicker2').data("DateTimePicker").minDate(e.date);
        });
        $("#datetimepicker2").on("dp.change", function (e) {
            $('#datetimepicker1').data("DateTimePicker").maxDate(e.date);
        });
        
        $("#attendee").select2({
    		placeholder: 'Attendee list'
    	});
	}
	initPage();
	
	
	var cal = function() {
		


	       /* initialize the calendar
	        -----------------------------------------------------------------*/
	       
	       $('#calendar').fullCalendar({
	           header: {
	               left: 'prev,next today',
	               center: 'title',
	               right: 'month,agendaWeek,agendaDay'
	           },
//	           defaultView: 'agendaWeek',
//	           editable: true,
//	           droppable: true, // this allows things to be dropped onto the calendar
	           drop: function() {
	               // is the "remove after drop" checkbox checked?
	               if ($('#drop-remove').is(':checked')) {
	                   // if so, remove the element from the "Draggable Events" list
	                   $(this).remove();
	               }
	           },
	           ignoreTimezone: false,
	           events: function(start, end, timezone, callback) {
	               $.ajax({
	            	    url : './getEvents?uname=0', // send from controller
		           		type : 'POST',
		           		contentType : "application/json",
	                    success: function(doc) {
	                    	//console.log(doc);
	                    	
	               
	                    	var events = [];
	                    	$.each(doc.list, function(i,l) {
	                    		
	                    		
	                    		var row = l;
		                    	var color;
		                    	
		                    	var dt1 = new Date(row.dt1);
		  		        		var curr = new Date();
		  		        		if(curr < dt1) color="#f0ad4e";
		  		        		else color = "#989898 ";
//		                    	if(row.adt1 == null) { // not started
//		  	        			  var dt1 = new Date(row.dt1);
//		  		        		  var curr = new Date();
//		  		        		  //console.log("curr is: "+curr+" dt1 is: "+dt1);
//		  		        		
//		  		        		  if(curr < dt1) color = "#428bca"; //'<span class="label label-success">UPCOMING</span>';
//		  		        		  else color = "#d9534f"; //'<span class="label label-danger">DELAYED</span>';
//		  	        		  }
//		  	        		  else {
//		  	        			  if(row.adt2 == null) color = "#f0ad4e"; //return '<span class="label label-warning">ON GOING</span>';
//		  	        			  else color = "#5cb85c"; //'<span class="label label-primary">COMPLETED</span>';
//		  	        		  }
		                     	
		                    	//------------------------------------------------------
		                    	
	                    		events.push({
	                    			id: l.id,
	                    			title: l.title,
	                    			type: l.type,
	                    			location: l.location,
	                    			byl: l.byl,
	                    			start: new Date(l.dt1),
	                    			end: new Date(l.dt2),
	                    			dt1: l.dt1,
	                    			dt2: l.dt2,
	                    			backgroundColor: color, 
	                    	        borderColor: color,
	                    	        eid: l.eid
	                    		})
	                    	})
	                       
	                       callback(events);
	                   }
	               });
	           },
	           eventClick: function(calEvent, jsEvent, view) {
	        	   
	        	   $("#dtitle").html("<h4>"+calEvent.title+"</h4>");
//	        	    $("#byl").html("<h4>"+calEvent.byl+"</h4>");
	        	    $("#ddt1").html("<h4>"+formatDate(new Date(calEvent.dt1))+"</h4>");
	        	    $("#ddt2").html("<h4>"+formatDate(new Date(calEvent.dt2))+"</h4>");
	        	    $("#dlocation").html("<h4>"+calEvent.location+"</h4>");
	        	    
	        	    console.log(calEvent.eid);
	        	    
	        	    if($.fn.DataTable.isDataTable("#table1")) {
		    			$('#table1').DataTable().clear().destroy();
		    		}
	        	    
	        	    $.ajax({
	      			  url: './eventAList?id='+calEvent.eid,
	      		      type : 'POST',
	      		      contentType : "application/json"
	      		      }).done(function (d) {
	      		    	  console.log(d);
	  	        	    table1 = $('#table1').DataTable({
		        			data: d.list,
		        			columns: [
		        	          { data: 'name' },
		        	          { data: 'role' }
//		        	          { data: 'sname' }
		        	          ],
		        	          paginate: false,
		        	          info: false,
		        	          filter: false,
		        	          sort: false
		        		});
	        	    
		        	    $('#tdetail-modal').modal('show');
	//	        	    $("#dpanel").show();
	      		      });


	           }
	       });
	      
		}
		
		cal();
});

$('.schedule').on('click', function() {	
	var title = $("#title").val();
	var type = $("#type").val();
	var dt1 = $("#dt1").val();
	var dt2 = $("#dt2").val();
	var attendee = $("#attendee").val();
	var location = $("#loc").val();
	
	console.log(title+" "+type+" "+dt1+" "+dt2+" "+attendee+" "+location);
	
	// get event id+1
	var eid = $.ajax({
		  url: './getEID',
	      type : 'POST',
	      contentType : "application/json",
	      async:false
	      }).responseText;
	
	console.log(eid);
	
	for(var i=0; i<attendee.length; i++) {
		console.log(attendee[i]);
		
		//update
		  $.ajax({
			  url: './addEvent',
		      type : 'POST',
		      contentType : "application/json",
		      data: JSON.stringify({uid: attendee[i], title: title, type: type, dt1: dt1, dt2: dt2, location: location, byl: "SALES MANAGER" , eid: eid}),
		      async:false
		      }).done(function (d) {
		    	  console.log("EVENT ADDED successfully");
//		    	  cevent(d);
		      });
	}
	
	//update
	  $.ajax({
		  url: './addEvent',
	      type : 'POST',
	      contentType : "application/json",
	      data: JSON.stringify({uid: 'manager', title: title, type: type, dt1: dt1, dt2: dt2, location: location, byl: "SALES MANAGER" ,eid: eid}),
	      async:false
	      }).done(function (d) {
	    	  console.log("EVENT ADDED successfully");
//	    	  cevent(d);
	      });
	
	$("#title").val('');
	$("#type").val('');
	$("#dt1").val('');
	$("#dt2").val('');
	$("#attendee").select2("val", "");
	$("#loc").val('');
	
  swal("Done!", "Event scheduled successfully!", "success")
  $('#calendar').fullCalendar( 'refetchEvents' )
	
});