(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('LawenforceDepartmentSearch', LawenforceDepartmentSearch);

    LawenforceDepartmentSearch.$inject = ['$resource'];

    function LawenforceDepartmentSearch($resource) {
        var resourceUrl =  'api/_search/lawenforce-departments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
