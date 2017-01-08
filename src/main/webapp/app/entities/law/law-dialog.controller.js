(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawDialogController', LawDialogController);

    LawDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Law', 'Task'];

    function LawDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Law, Task) {
        var vm = this;

        vm.law = entity;
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
            if (vm.law.id !== null) {
                Law.update(vm.law, onSaveSuccess, onSaveError);
            } else {
                Law.save(vm.law, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:lawUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
