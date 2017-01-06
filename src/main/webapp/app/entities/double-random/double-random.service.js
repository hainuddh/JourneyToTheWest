(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('DoubleRandom', DoubleRandom);

    DoubleRandom.$inject = ['$resource'];

    function DoubleRandom ($resource) {
        var resourceUrl =  'api/double-randoms/:id';

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
