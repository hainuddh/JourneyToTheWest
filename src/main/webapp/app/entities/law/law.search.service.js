(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('LawSearch', LawSearch);

    LawSearch.$inject = ['$resource'];

    function LawSearch($resource) {
        var resourceUrl =  'api/_search/laws/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
