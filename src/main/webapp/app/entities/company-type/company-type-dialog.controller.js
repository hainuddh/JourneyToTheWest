(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('CompanyTypeDialogController', CompanyTypeDialogController);

    CompanyTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyType', 'Company'];

    function CompanyTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyType, Company) {
        var vm = this;

        vm.companyType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyType.id !== null) {
                CompanyType.update(vm.companyType, onSaveSuccess, onSaveError);
            } else {
                CompanyType.save(vm.companyType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:companyTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
