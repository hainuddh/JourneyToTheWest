(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('HeadPerson', HeadPerson);

    HeadPerson.$inject = ['$resource'];

    function HeadPerson ($resource) {
        var resourceUrl =  'api/head-people/:id';

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
