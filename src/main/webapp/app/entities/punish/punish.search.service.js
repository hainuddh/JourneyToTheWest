(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('PunishSearch', PunishSearch);

    PunishSearch.$inject = ['$resource'];

    function PunishSearch($resource) {
        var resourceUrl =  'api/_search/punishes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
