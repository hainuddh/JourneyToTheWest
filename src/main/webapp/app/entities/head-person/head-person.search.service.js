(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('HeadPersonSearch', HeadPersonSearch);

    HeadPersonSearch.$inject = ['$resource'];

    function HeadPersonSearch($resource) {
        var resourceUrl =  'api/_search/head-people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
