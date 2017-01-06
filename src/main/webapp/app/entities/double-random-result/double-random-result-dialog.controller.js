(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomResultDialogController', DoubleRandomResultDialogController);

    DoubleRandomResultDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DoubleRandomResult', 'DoubleRandom', 'Manager'];

    function DoubleRandomResultDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DoubleRandomResult, DoubleRandom, Manager) {
        var vm = this;

        vm.doubleRandomResult = entity;
        vm.clear = clear;
        vm.save = save;
        vm.doublerandoms = DoubleRandom.query();
        vm.managers = Manager.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.doubleRandomResult.id !== null) {
                DoubleRandomResult.update(vm.doubleRandomResult, onSaveSuccess, onSaveError);
            } else {
                DoubleRandomResult.save(vm.doubleRandomResult, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:doubleRandomResultUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
