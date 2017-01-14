(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('HeadPersonDialogController', HeadPersonDialogController);

    HeadPersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HeadPerson', 'Office'];

    function HeadPersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HeadPerson, Office) {
        var vm = this;

        vm.headPerson = entity;
        vm.clear = clear;
        vm.save = save;
        vm.offices = Office.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.headPerson.id !== null) {
                HeadPerson.update(vm.headPerson, onSaveSuccess, onSaveError);
            } else {
                HeadPerson.save(vm.headPerson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:headPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
