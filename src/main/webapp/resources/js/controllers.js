var eveuserControllers = angular.module('eveuserControllers', []);

eveuserControllers.controller('UserListCtrl', ['$scope', '$log', 'User', function($scope, $log, User) {
	  $scope.users = User.query();
	  $scope.doSubmit = function() { 
		  //$scope.users = User.saveData({}, $scope.users);
		  var newusers = User.saveData({}, $scope.users, function() {
			  $scope.users = newusers;
		  });
		  
		  
	  };
	}]);
eveuserControllers.controller('DefaultCtrl', ['$scope', '$log', function($scope, $log) {

	}]);

eveuserControllers.controller('HephaestusCtrl', ['$scope', '$log', function($scope, $log) {

}]);

eveuserControllers.controller('OrdersCtrl', ['$scope', '$log', function($scope, $log) {

}]);

eveuserControllers.controller("SecurityCtrl", function($http, $location) {
    var self = this;
    self.authenticated = false;
    self.hasWSTRole = false;
    self.hasAdminRole = false;
    self.user = "";
    $http.get("/rest/security/user").success(function(data) {
      self.user = data.name;
      if(!!data.name && data.name.length > 0)
    	  self.authenticated = true;
      	  self.hasWSTRole = (data.userAuthentication.details.authorities.indexOf("ROLE_WST_USER") > -1);
      	  self.hasAdminRole = (data.userAuthentication.details.authorities.indexOf("ROLE_ADMIN") > -1);
      	  
    }).error(function() {
      self.user = "N/A";
      self.authenticated = false;
      self.hasWSTRole = false;
      self.hasAdminRole = false;
    });
    self.logout = function() {
        $http.post('/logout', {}).success(function() {
          self.authenticated = false;
          $location.path("/");
        }).error(function(data) {
          console.log("Logout failed")
          self.authenticated = false;
        });
    };
  });