'use strict';

angular.module('environmentApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('clan', {
                parent: 'entity',
                url: '/clan',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'environmentApp.clan.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/clan/clans.html',
                        controller: 'ClanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('clan');
                        return $translate.refresh();
                    }]
                }
            })
            .state('clanDetail', {
                parent: 'entity',
                url: '/clan/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'environmentApp.clan.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/clan/clan-detail.html',
                        controller: 'ClanDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('clan');
                        return $translate.refresh();
                    }]
                }
            });
    });
