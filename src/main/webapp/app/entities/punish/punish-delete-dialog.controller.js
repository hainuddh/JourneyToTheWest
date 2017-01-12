(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('PunishDeleteController',PunishDeleteController);

    PunishDeleteController.$inject = ['$uibModalInstance', 'entity', 'Punish'];

    function PunishDeleteController($uibModalInstance, entity, Punish) {
        var vm = this;

        vm.punish = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Punish.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
