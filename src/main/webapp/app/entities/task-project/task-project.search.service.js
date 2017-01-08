(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .factory('TaskProjectSearch', TaskProjectSearch);

    TaskProjectSearch.$inject = ['$resource'];

    function TaskProjectSearch($resource) {
        var resourceUrl =  'api/_search/task-projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
