(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('SignSearch', SignSearch);

    SignSearch.$inject = ['$resource'];

    function SignSearch($resource) {
        var resourceUrl =  'api/_search/signs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
