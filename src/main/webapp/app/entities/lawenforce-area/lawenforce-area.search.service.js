(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('LawenforceAreaSearch', LawenforceAreaSearch);

    LawenforceAreaSearch.$inject = ['$resource'];

    function LawenforceAreaSearch($resource) {
        var resourceUrl =  'api/_search/lawenforce-areas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
