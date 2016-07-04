(function(){

	'use strict'
	
	var router_fn  = function ($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise("/");
		
		$stateProvider.state('home',
				{
					url: '/',
					views: {
						'middle': {
							templateUrl: 'views/home/view/homepage.html',
							//controller : 'homeController'
						},
						
					}
				}
			);
		
		
		$stateProvider.state('stats',
				{
					url: '/',
					views: {
						'middle': {
							templateUrl: 'views/stats/view/statistics.html',
							controller : 'statsController'
						},
						
					}
				}
			);
		
		
		$stateProvider.state('upload',
				{
					url: '/',
					views: {
						'middle': {
							templateUrl: 'views/upload/view/uploader.html',
							controller : 'uploadController'
						},
						
					}
				}
			);
		
	};
	
	
	
	
	//angular.module('app-filer',[]).service('glbl', function(){});
	angular.module('app-emplonet').config(['$stateProvider', '$urlRouterProvider', router_fn]);
	
	
})();