(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('DoubleRandomResultSearch', DoubleRandomResultSearch);

    DoubleRandomResultSearch.$inject = ['$resource'];

    function DoubleRandomResultSearch($resource) {
        var resourceUrl =  'api/_search/double-random-results/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
