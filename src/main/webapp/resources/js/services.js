var phonecatServices = angular.module('eveuserServices', ['ngResource']);

phonecatServices.factory('User', ['$resource',
  function($resource){
    return $resource('rest/users/:characterId', {}, {
      query: {method:'GET', params:{characterId:''}, isArray:true},
      saveData: {method:'POST',  isArray: true}
    });
  }]);
