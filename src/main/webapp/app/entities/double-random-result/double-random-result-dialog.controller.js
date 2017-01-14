(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomResultDialogController', DoubleRandomResultDialogController);

    DoubleRandomResultDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'DoubleRandomResult', 'Lawenforcement', 'Manager', 'Sign', 'Company', 'DoubleRandom'];

    function DoubleRandomResultDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, DoubleRandomResult, Lawenforcement, Manager, Sign, Company, DoubleRandom) {
        var vm = this;

        vm.doubleRandomResult = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lawenforcements = Lawenforcement.query({filter: 'doublerandomresult-is-null'});
        $q.all([vm.doubleRandomResult.$promise, vm.lawenforcements.$promise]).then(function() {
            if (!vm.doubleRandomResult.lawenforcement || !vm.doubleRandomResult.lawenforcement.id) {
                return $q.reject();
            }
            return Lawenforcement.get({id : vm.doubleRandomResult.lawenforcement.id}).$promise;
        }).then(function(lawenforcement) {
            vm.lawenforcements.push(lawenforcement);
        });
        vm.managers = Manager.query();
        vm.signs = Sign.query();
        vm.companies = Company.query();
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
