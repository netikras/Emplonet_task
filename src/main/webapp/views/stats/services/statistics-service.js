(function() {
	
	'use strict'
	
	var statsService_fn = function($http) {
		
		
		var _retrieveData = function(url) {
			
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
		
		
		var getWordCount = function(word) {
			var url = 'http://'+window.location.host+'/emplonet/results/word/'+word;
			
			return _retrieveData(url);
		};
		
		var getCategoryCount = function(cat) {
			var url = 'http://'+window.location.host+'/emplonet/results/category/'+cat;
			
			return _retrieveData(url);
		};
		
		var getAllCounts = function() {
			var url = 'http://'+window.location.host+'/emplonet/results';
			
			return _retrieveData(url);
		};
		
		
		
		
		return {
			getWordCount    : getWordCount,
			getCategoryCount: getCategoryCount,
			getAllCounts    : getAllCounts,
		};
		
	};
	
	angular.module('app-emplonet').service('statsSvc', ['$http', statsService_fn]);
	
})()