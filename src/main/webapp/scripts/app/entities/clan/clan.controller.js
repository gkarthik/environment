'use strict';

angular.module('environmentApp')
    .controller('ClanController', function ($scope, Clan, User, ParseLinks) {
        $scope.clans = [];
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Clan.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.clans = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Clan.update($scope.clan,
                function () {
                    $scope.loadAll();
                    $('#saveClanModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Clan.get({id: id}, function(result) {
                $scope.clan = result;
                $('#saveClanModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Clan.get({id: id}, function(result) {
                $scope.clan = result;
                $('#deleteClanConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Clan.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteClanConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.clan = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
