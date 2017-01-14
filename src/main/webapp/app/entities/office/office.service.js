(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('Office', Office);

    Office.$inject = ['$resource'];

    function Office ($resource) {
        var resourceUrl =  'api/offices/:id';

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
