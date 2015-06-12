'use strict';

angular.module('environmentApp')
    .controller('BattlegroundController', function ($scope, Battleground, Clan, ParseLinks) {
        $scope.battlegrounds = [];
        $scope.clans = Clan.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Battleground.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.battlegrounds = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Battleground.update($scope.battleground,
                function () {
                    $scope.loadAll();
                    $('#saveBattlegroundModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Battleground.get({id: id}, function(result) {
                $scope.battleground = result;
                $('#saveBattlegroundModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Battleground.get({id: id}, function(result) {
                $scope.battleground = result;
                $('#deleteBattlegroundConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Battleground.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBattlegroundConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.battleground = {name: null, location: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
