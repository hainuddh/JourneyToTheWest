(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('DoubleRandomSearch', DoubleRandomSearch);

    DoubleRandomSearch.$inject = ['$resource'];

    function DoubleRandomSearch($resource) {
        var resourceUrl =  'api/_search/double-randoms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
