(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('Lawenforcement', Lawenforcement);

    Lawenforcement.$inject = ['$resource'];

    function Lawenforcement ($resource) {
        var resourceUrl =  'api/lawenforcements/:id';

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
