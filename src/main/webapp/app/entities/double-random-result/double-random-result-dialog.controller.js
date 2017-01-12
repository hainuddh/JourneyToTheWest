(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomResultDialogController', DoubleRandomResultDialogController);

    DoubleRandomResultDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'DoubleRandomResult', 'Company', 'Sign', 'Task', 'Manager', 'DoubleRandom'];

    function DoubleRandomResultDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, DoubleRandomResult, Company, Sign, Task, Manager, DoubleRandom) {
        var vm = this;

        vm.doubleRandomResult = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query({filter: 'doublerandomresult-is-null'});
        $q.all([vm.doubleRandomResult.$promise, vm.companies.$promise]).then(function() {
            if (!vm.doubleRandomResult.company || !vm.doubleRandomResult.company.id) {
                return $q.reject();
            }
            return Company.get({id : vm.doubleRandomResult.company.id}).$promise;
        }).then(function(company) {
            vm.companies.push(company);
        });
        vm.signs = Sign.query({filter: 'doublerandomresult-is-null'});
        $q.all([vm.doubleRandomResult.$promise, vm.signs.$promise]).then(function() {
            if (!vm.doubleRandomResult.sign || !vm.doubleRandomResult.sign.id) {
                return $q.reject();
            }
            return Sign.get({id : vm.doubleRandomResult.sign.id}).$promise;
        }).then(function(sign) {
            vm.signs.push(sign);
        });
        vm.tasks = Task.query();
        vm.managers = Manager.query();
        vm.doublerandoms = DoubleRandom.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.doubleRandomResult.id !== null) {
                DoubleRandomResult.update(vm.doubleRandomResult, onSaveSuccess, onSaveError);
            } else {
                DoubleRandomResult.save(vm.doubleRandomResult, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:doubleRandomResultUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
