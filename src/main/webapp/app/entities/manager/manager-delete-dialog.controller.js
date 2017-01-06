(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('ManagerDeleteController',ManagerDeleteController);

    ManagerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Manager'];

    function ManagerDeleteController($uibModalInstance, entity, Manager) {
        var vm = this;

        vm.manager = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Manager.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
