(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('LawenforceDepartment', LawenforceDepartment);

    LawenforceDepartment.$inject = ['$resource'];

    function LawenforceDepartment ($resource) {
        var resourceUrl =  'api/lawenforce-departments/:id';

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
