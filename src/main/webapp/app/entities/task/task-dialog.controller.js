(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'Law', 'TaskProject', 'DoubleRandom'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task, Law, TaskProject, DoubleRandom) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.save = save;
        vm.laws = Law.query();
        vm.taskprojects = TaskProject.query();
        vm.doublerandoms = DoubleRandom.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:taskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
