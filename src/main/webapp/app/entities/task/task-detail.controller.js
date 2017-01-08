(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Task', 'Law', 'TaskProject', 'DoubleRandom'];

    function TaskDetailController($scope, $rootScope, $stateParams, previousState, entity, Task, Law, TaskProject, DoubleRandom) {
        var vm = this;

        vm.task = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
