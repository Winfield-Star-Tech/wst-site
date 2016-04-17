/**
 * 
 */
var wstSiteApp = angular.module('wstSiteApp', 
		['ngRoute', 'eveuserControllers','eveuserFilters', 'eveuserServices']);

wstSiteApp.config(['$routeProvider',
                    function($routeProvider) {
                      $routeProvider.
                        when('/', {
                          templateUrl: '/angular/partial/main',
                          controller: 'DefaultCtrl'
                        }).
                        when('/useradmin', {
                          templateUrl: '/angular/partial/useradmin',
                          controller: 'UserListCtrl'
                        }).
                        when('/hephaestus', {
                            templateUrl: '/angular/partial/hephaestus',
                            controller: 'HephaestusCtrl'
                          }).
	                      when('/shiporders', {
	                          templateUrl: '/angular/partial/shiporders',
	                          controller: 'OrdersCtrl'
	                        }).
	                      when('/unauthorised', {
	                          templateUrl: '/angular/partial/unauthorised',
	                          controller: 'DefaultCtrl'
	                        }).
                        otherwise({
                          redirectTo: '/'
                        });
                    }]);