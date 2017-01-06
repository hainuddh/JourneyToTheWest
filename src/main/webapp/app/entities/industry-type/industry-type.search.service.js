(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('IndustryTypeSearch', IndustryTypeSearch);

    IndustryTypeSearch.$inject = ['$resource'];

    function IndustryTypeSearch($resource) {
        var resourceUrl =  'api/_search/industry-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
