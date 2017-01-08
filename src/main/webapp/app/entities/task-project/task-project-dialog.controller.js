(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('TaskProjectDialogController', TaskProjectDialogController);

    TaskProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TaskProject', 'Task'];

    function TaskProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TaskProject, Task) {
        var vm = this;

        vm.taskProject = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tasks = Task.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.taskProject.id !== null) {
                TaskProject.update(vm.taskProject, onSaveSuccess, onSaveError);
            } else {
                TaskProject.save(vm.taskProject, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:taskProjectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
