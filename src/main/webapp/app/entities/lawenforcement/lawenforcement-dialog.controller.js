(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforcementDialogController', LawenforcementDialogController);

    LawenforcementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Lawenforcement', 'Punish'];

    function LawenforcementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Lawenforcement, Punish) {
        var vm = this;

        vm.lawenforcement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.punishes = Punish.query({filter: 'lawenforcement-is-null'});
        $q.all([vm.lawenforcement.$promise, vm.punishes.$promise]).then(function() {
            if (!vm.lawenforcement.punish || !vm.lawenforcement.punish.id) {
                return $q.reject();
            }
            return Punish.get({id : vm.lawenforcement.punish.id}).$promise;
        }).then(function(punish) {
            vm.punishes.push(punish);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lawenforcement.id !== null) {
                Lawenforcement.update(vm.lawenforcement, onSaveSuccess, onSaveError);
            } else {
                Lawenforcement.save(vm.lawenforcement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('journeyToTheWestApp:lawenforcementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
