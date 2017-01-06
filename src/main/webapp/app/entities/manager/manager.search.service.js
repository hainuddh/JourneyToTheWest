(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('ManagerSearch', ManagerSearch);

    ManagerSearch.$inject = ['$resource'];

    function ManagerSearch($resource) {
        var resourceUrl =  'api/_search/managers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
