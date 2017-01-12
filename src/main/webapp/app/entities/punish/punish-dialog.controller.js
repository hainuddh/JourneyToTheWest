(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('PunishDialogController', PunishDialogController);

    PunishDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Punish', 'LawenforceDepartment', 'Company'];

    function PunishDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Punish, LawenforceDepartment, Company) {
        var vm = this;

        vm.punish = entity;
        vm.clear = clear;
        vm.save = save;
        vm.departments = LawenforceDepartment.query({filter: 'punish-is-null'});
        $q.all([vm.punish.$promise, vm.departments.$promise]).then(function() {
            if (!vm.punish.department || !vm.punish.department.id) {
                return $q.reject();
            }
            return LawenforceDepartment.get({id : vm.punish.department.id}).$promise;
        }).then(function(department) {
            vm.departments.push(department);
        });
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.punish.id !== null) {
                Punish.update(vm.punish, onSaveSuccess, onSaveError);
            } else {
                Punish.save(vm.punish, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:punishUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
