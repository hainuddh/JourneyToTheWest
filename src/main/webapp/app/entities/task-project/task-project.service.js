(function() {
    'use strict';
    angular
        .module('journeyToTheWestApp')
        .factory('TaskProject', TaskProject);

    TaskProject.$inject = ['$resource'];

    function TaskProject ($resource) {
        var resourceUrl =  'api/task-projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
