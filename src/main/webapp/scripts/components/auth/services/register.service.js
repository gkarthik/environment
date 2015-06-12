'use strict';

angular.module('environmentApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


