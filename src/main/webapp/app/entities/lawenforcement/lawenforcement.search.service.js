(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('LawenforcementSearch', LawenforcementSearch);

    LawenforcementSearch.$inject = ['$resource'];

    function LawenforcementSearch($resource) {
        var resourceUrl =  'api/_search/lawenforcements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
