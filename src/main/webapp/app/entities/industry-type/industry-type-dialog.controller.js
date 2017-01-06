(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('IndustryTypeDialogController', IndustryTypeDialogController);

    IndustryTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'IndustryType', 'Company'];

    function IndustryTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, IndustryType, Company) {
        var vm = this;

        vm.industryType = entity;
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
            if (vm.industryType.id !== null) {
                IndustryType.update(vm.industryType, onSaveSuccess, onSaveError);
            } else {
                IndustryType.save(vm.industryType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:industryTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
