(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('OfficeDialogController', OfficeDialogController);

    OfficeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Office', 'HeadPerson'];

    function OfficeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Office, HeadPerson) {
        var vm = this;

        vm.office = entity;
        vm.clear = clear;
        vm.save = save;
        vm.headpeople = HeadPerson.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.office.id !== null) {
                Office.update(vm.office, onSaveSuccess, onSaveError);
            } else {
                Office.save(vm.office, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:officeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
