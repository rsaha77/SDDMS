$(document).ready(function() {
	var initPage = function() {
		switchActiveTab('nav-office');

		BookStore.dataTable = $('#office-table').DataTable({
			'serverSide' : true,
			'ajax' : {
				url : 'offices',
				type : 'POST',
				contentType : "application/json",
				data: function ( d ) {
					// send only data required by backend API
					delete(d.columns);
					delete(d.order);
					delete(d.search);
			      return JSON.stringify(d);
			    },
			    dataSrc: "offices"
			},
			columns: [
	          { data: 'id' },
	          { data: 'name' }
			],
			select: "single"
			
		});
		

		$('#office-add-button').click(BookStore.addOffice);
		$('#office-delete-button').click(BookStore.deleteOffice);
		
		// disable delete button if nothing selected
		BookStore.dataTable.on('select', function () {
			$('#office-open-delete-modal-btn').prop('disabled', false);
	    });
		
		BookStore.dataTable.on('deselect', function () {
			$('#office-open-delete-modal-btn').prop('disabled', true);
	    });
	};

	initPage();
});

BookStore.addOffice = function(evt) {
	var formData = $('#office-form').serializeObject();

	$.ajax({
		url : "office",
		data : JSON.stringify(formData),
		type : 'POST',
		contentType : "application/json",
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#office-add-modal').modal('hide');
		$('#office-table').dataTable().fnReloadAjax();
		
	});
};

BookStore.deleteOffice = function(evt) {
	var selectedId = BookStore.dataTable.data()[BookStore.dataTable.row('.selected')[0]].id;
	$.ajax({
		url : "office?id=" + selectedId,
		type : 'DELETE',
		xhrFields: {
	      withCredentials: true
	   }
	}).done(function() {
		$('#office-delete-modal').modal('hide');
		$('#office-table').dataTable().fnReloadAjax();
		
	});
};