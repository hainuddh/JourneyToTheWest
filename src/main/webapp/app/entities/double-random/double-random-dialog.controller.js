(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomDialogController', DoubleRandomDialogController);

    DoubleRandomDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DoubleRandom', 'DoubleRandomResult', 'Task'];

    function DoubleRandomDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DoubleRandom, DoubleRandomResult, Task) {
        var vm = this;

        vm.doubleRandom = entity;
        vm.clear = clear;
        vm.save = save;
        vm.doublerandomresults = DoubleRandomResult.query();
        vm.tasks = Task.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.doubleRandom.id !== null) {
                DoubleRandom.update(vm.doubleRandom, onSaveSuccess, onSaveError);
            } else {
                DoubleRandom.save(vm.doubleRandom, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:doubleRandomUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
