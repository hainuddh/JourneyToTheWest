(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceDepartmentDialogController', LawenforceDepartmentDialogController);

    LawenforceDepartmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LawenforceDepartment', 'Company', 'Manager'];

    function LawenforceDepartmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LawenforceDepartment, Company, Manager) {
        var vm = this;

        vm.lawenforceDepartment = entity;
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
            if (vm.lawenforceDepartment.id !== null) {
                LawenforceDepartment.update(vm.lawenforceDepartment, onSaveSuccess, onSaveError);
            } else {
                LawenforceDepartment.save(vm.lawenforceDepartment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:lawenforceDepartmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
