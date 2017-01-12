(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('SignDialogController', SignDialogController);

    SignDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sign'];

    function SignDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sign) {
        var vm = this;

        vm.sign = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sign.id !== null) {
                Sign.update(vm.sign, onSaveSuccess, onSaveError);
            } else {
                Sign.save(vm.sign, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:signUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
