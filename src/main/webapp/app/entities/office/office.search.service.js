(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('OfficeSearch', OfficeSearch);

    OfficeSearch.$inject = ['$resource'];

    function OfficeSearch($resource) {
        var resourceUrl =  'api/_search/offices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
