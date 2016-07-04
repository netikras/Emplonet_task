(function() {
	'use strict'
	
	
	var uploaderController_fn = function($scope, uploadSvc, mainSvc) {
		
		$scope.results = {};
		
		$scope.uploadFile = function() {
			var file = $scope.filesToUpload;
			
			console.log("uploading a file:");
			console.log(file);
			
			$scope.results = uploadSvc.uploadFile(file);
		};
		
		
		$scope.saveChanges = mainSvc.saveChanges;
		
		
	};
	
	angular.module('app-emplonet').controller('uploadController', ['$scope', 'uploadSvc', 'mainSvc', uploaderController_fn]);
	
})()