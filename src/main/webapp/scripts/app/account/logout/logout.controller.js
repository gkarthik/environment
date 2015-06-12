'use strict';

angular.module('environmentApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
