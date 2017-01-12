(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('Sign', Sign);

    Sign.$inject = ['$resource'];

    function Sign ($resource) {
        var resourceUrl =  'api/signs/:id';

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
