(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceAreaDeleteController',LawenforceAreaDeleteController);

    LawenforceAreaDeleteController.$inject = ['$uibModalInstance', 'entity', 'LawenforceArea'];

    function LawenforceAreaDeleteController($uibModalInstance, entity, LawenforceArea) {
        var vm = this;

        vm.lawenforceArea = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LawenforceArea.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
