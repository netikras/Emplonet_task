(function() {
	'use strict'
	
	
	var directive_fn = function ($parse) {
	    return {
	        restrict: 'A',
	        link: function (scope, element, attrs) {
	            var model = $parse(attrs.fileModel);
	            var isMultiple = attrs.multiple;
	            var modelSetter = model.assign;
	            element.bind('change', function () {
	                scope.$apply(function () {
	                    if (isMultiple) {
	                    	modelSetter(scope, element[0].files)
	                    } else {
	                    	modelSetter(scope, element[0].files)
	                    }
	                });
	            });
	        }
	    };
	}
	
	angular.module('app-emplonet').directive('fileModel', directive_fn);
	
})();

