'use strict';

angular.module('environmentApp')
    .controller('ClanDetailController', function ($scope, $stateParams, Clan, User) {
        $scope.clan = {};
        $scope.load = function (id) {
            Clan.get({id: id}, function(result) {
              $scope.clan = result;
            });
        };
        $scope.load($stateParams.id);
    });
