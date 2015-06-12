'use strict';

angular.module('environmentApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('battleground', {
                parent: 'entity',
                url: '/battleground',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'environmentApp.battleground.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/battleground/battlegrounds.html',
                        controller: 'BattlegroundController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('battleground');
                        return $translate.refresh();
                    }]
                }
            })
            .state('battlegroundDetail', {
                parent: 'entity',
                url: '/battleground/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'environmentApp.battleground.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/battleground/battleground-detail.html',
                        controller: 'BattlegroundDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('battleground');
                        return $translate.refresh();
                    }]
                }
            });
    });
