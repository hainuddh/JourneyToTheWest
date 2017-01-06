(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('CompanyTypeSearch', CompanyTypeSearch);

    CompanyTypeSearch.$inject = ['$resource'];

    function CompanyTypeSearch($resource) {
        var resourceUrl =  'api/_search/company-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
