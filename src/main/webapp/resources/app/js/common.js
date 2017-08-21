var switchActiveTab = function (id) {
	$.each($('#book-store-navbar').children(), function(i, el) {
		if (el.id === id) {
			$(el).addClass('active');
		} else {
			$(el).removeClass('active');
		}
	});
};

var sddms = {};

jQuery.fn.serializeObject = function() {
	var arrayData, objectData;
	arrayData = this.serializeArray();
	objectData = {};

	$.each(arrayData, function() {
		var value;

		if (this.value != null) {
			value = this.value;
		} else {
			value = '';
		}

		if (objectData[this.name] != null) {
			if (!objectData[this.name].push) {
				objectData[this.name] = [ objectData[this.name] ];
			}

			objectData[this.name].push(value);
		} else {
			objectData[this.name] = value;
		}
	});

	return objectData;
};

$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
