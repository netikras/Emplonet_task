(function() {
	
	'use strict'
	
	var mainService_fn = function($http) {
		
		
		var getCategories = function(url) {
			
			var url = 'http://'+window.location.host+'/emplonet/categories';
			
			var result = {
					success: null,
					data   : null,
			};
			
			var promise = $http(
					{
						method: "GET",
						url   : url,
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
		
		
		
		var saveChanges = function() {
			var url = 'http://'+window.location.host+'/emplonet/save';
			var result = {
					success    : null,
					data       : null,
			};
			
			var promise = $http(
					{
						method: "POST",
						url: url,
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
			getCategories   : getCategories,
			saveChanges     : saveChanges,
		};
		
	};
	
	angular.module('app-emplonet').service('mainSvc', ['$http', mainService_fn]);
	
})()