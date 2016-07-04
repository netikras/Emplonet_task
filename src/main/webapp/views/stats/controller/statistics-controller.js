(function() {
	'use strict'
	
	
	var statsController_fn = function($scope, statsSvc, mainSvc) {

		$scope.categories = mainSvc.getCategories();
		
		$scope.results = {};
		$scope.queryType = '';
		
		$scope.query = {
				qType : "",
				qWhat : "",
		};
		
		$scope.setType = function(type) {
			$scope.query.qWhat = "";
			$scope.query.qType = type;
			$scope.results = {};
		};
		
		$scope.setQuery = function(qry) {
			$scope.query.qWhat = qry;
		};
		
		$scope.getResults = function() {
			var queryType  = $scope.query.qType;
			var queryValue = $scope.query.qWhat;
			
			switch(queryType) {
				
				case "By word":
					$scope.results = statsSvc.getWordCount(queryValue);
					break;
				case "By category":
					$scope.results = statsSvc.getCategoryCount(queryValue);
					break;
				default:
					$scope.results = statsSvc.getAllCounts();
					break;
			};
		};
		
		
	};
	
	
	angular.module('app-emplonet').controller("statsController", ['$scope', 'statsSvc', 'mainSvc', statsController_fn]);
	
})()