(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforcementDeleteController',LawenforcementDeleteController);

    LawenforcementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lawenforcement'];

    function LawenforcementDeleteController($uibModalInstance, entity, Lawenforcement) {
        var vm = this;

        vm.lawenforcement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lawenforcement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
