(function() {
	'use strict'
	
	var controller_fn = function($scope, mainSvc) {
		
		//$scope.fileToUpload = null;
		
		$scope.categories = mainSvc.getCategories();
		
		
	};
	
	angular.module('app-emplonet').controller('mainController', ['$scope', 'mainSvc', controller_fn]);
	
	
})()