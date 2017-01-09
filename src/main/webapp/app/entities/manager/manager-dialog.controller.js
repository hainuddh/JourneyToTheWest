(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('ManagerDialogController', ManagerDialogController);

    ManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Manager', 'User', 'LawenforceDepartment', 'LawenforceArea', 'DoubleRandomResult'];

    function ManagerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Manager, User, LawenforceDepartment, LawenforceArea, DoubleRandomResult) {
        var vm = this;

        vm.manager = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.lawenforcedepartments = LawenforceDepartment.query();
        vm.lawenforceareas = LawenforceArea.query();
        vm.doublerandomresults = DoubleRandomResult.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.manager.id !== null) {
                Manager.update(vm.manager, onSaveSuccess, onSaveError);
            } else {
                Manager.save(vm.manager, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:managerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
