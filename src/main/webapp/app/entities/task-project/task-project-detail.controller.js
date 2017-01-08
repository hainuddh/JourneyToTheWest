(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('TaskProjectDetailController', TaskProjectDetailController);

    TaskProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TaskProject', 'Task'];

    function TaskProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, TaskProject, Task) {
        var vm = this;

        vm.taskProject = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:taskProjectUpdate', function(event, result) {
            vm.taskProject = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
