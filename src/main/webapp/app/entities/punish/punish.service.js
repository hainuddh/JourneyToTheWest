(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('Punish', Punish);

    Punish.$inject = ['$resource'];

    function Punish ($resource) {
        var resourceUrl =  'api/punishes/:id';

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
