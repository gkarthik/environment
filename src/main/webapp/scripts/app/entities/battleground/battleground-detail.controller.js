'use strict';

angular.module('environmentApp')
    .controller('BattlegroundDetailController', function ($scope, $stateParams, Battleground, Clan) {
        $scope.battleground = {};
        $scope.load = function (id) {
            Battleground.get({id: id}, function(result) {
              $scope.battleground = result;
            });
        };
        $scope.load($stateParams.id);
    });
