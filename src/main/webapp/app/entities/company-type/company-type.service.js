(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('CompanyType', CompanyType);

    CompanyType.$inject = ['$resource'];

    function CompanyType ($resource) {
        var resourceUrl =  'api/company-types/:id';

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
