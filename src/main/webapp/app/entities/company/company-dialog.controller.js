(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company', 'CompanyType', 'IndustryType', 'LawenforceArea', 'LawenforceDepartment', 'DoubleRandomResult'];

    function CompanyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Company, CompanyType, IndustryType, LawenforceArea, LawenforceDepartment, DoubleRandomResult) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companytypes = CompanyType.query();
        vm.industrytypes = IndustryType.query();
        vm.lawenforceareas = LawenforceArea.query();
        vm.lawenforcedepartments = LawenforceDepartment.query();
        vm.doublerandomresults = DoubleRandomResult.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onSaveSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:companyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
