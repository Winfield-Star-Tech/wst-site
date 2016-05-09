var eveServices = angular.module('eveuserServices', ['ngResource']);

eveServices.factory('User', ['$resource',
  function($resource){
    return $resource('rest/users/:characterId', {}, {
      query: {method:'GET', params:{characterId:''}, isArray:true},
      saveData: {method:'POST',  isArray: true}
    });
  }]);

eveServices.factory('KbList', ['$resource',
                             function($resource){
                               return $resource('rest/hangar/user/killboard', {}, {
                                 query: {method:'GET', isArray:true}
                               });
                             }]);
