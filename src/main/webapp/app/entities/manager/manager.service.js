(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('Manager', Manager);

    Manager.$inject = ['$resource'];

    function Manager ($resource) {
        var resourceUrl =  'api/managers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
