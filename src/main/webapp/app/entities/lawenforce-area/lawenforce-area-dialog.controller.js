(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceAreaDialogController', LawenforceAreaDialogController);

    LawenforceAreaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LawenforceArea', 'Company', 'Manager'];

    function LawenforceAreaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LawenforceArea, Company, Manager) {
        var vm = this;

        vm.lawenforceArea = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.managers = Manager.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lawenforceArea.id !== null) {
                LawenforceArea.update(vm.lawenforceArea, onSaveSuccess, onSaveError);
            } else {
                LawenforceArea.save(vm.lawenforceArea, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:lawenforceAreaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
