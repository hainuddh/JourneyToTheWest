(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('LawenforceArea', LawenforceArea);

    LawenforceArea.$inject = ['$resource'];

    function LawenforceArea ($resource) {
        var resourceUrl =  'api/lawenforce-areas/:id';

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
