(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('IndustryType', IndustryType);

    IndustryType.$inject = ['$resource'];

    function IndustryType ($resource) {
        var resourceUrl =  'api/industry-types/:id';

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
