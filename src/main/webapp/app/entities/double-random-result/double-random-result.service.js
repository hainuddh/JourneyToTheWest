(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('DoubleRandomResult', DoubleRandomResult);

    DoubleRandomResult.$inject = ['$resource'];

    function DoubleRandomResult ($resource) {
        var resourceUrl =  'api/double-random-results/:id';

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
