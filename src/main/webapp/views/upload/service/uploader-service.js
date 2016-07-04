(function() {
	'use strict'
	
	var uploadService_fn = function($http) {
		
		
		var uploadFile = function(files) {
			
			var url = 'http://'+window.location.host+'/emplonet/';
			
			var result = {
					success    : "",
					data       : "",
			};
			
			var formData = new FormData();
			var promise = null;
			
			for(var i=0; i<files.length; i++) {
				
				console.log("Appending file to formData:");
				console.log(files[i]);
				
				formData.append('file', files[i]);
				
				console.log("sending......");
				
			}
			
			promise = $http(
					{
						method : "POST",
						url    : url,
						data   : formData,
						headers: {'Content-Type': undefined	},
						transformRequest: angular.identity,
					}
			);
			
			promise.then(
					function(response) {
						result.data    = response.data;
						result.success = true;
					},
					function(error) {
						result.data    = ""+error.status+": "+error.statusText;
						result.success = false;
					}
			);
			
			return result;
		};
		
		
		
		
		return {
			uploadFile : uploadFile,
		};
		
	};
	
	angular.module('app-emplonet').service('uploadSvc', ['$http', uploadService_fn]);
	
})()