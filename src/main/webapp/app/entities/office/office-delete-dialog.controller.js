(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('OfficeDeleteController',OfficeDeleteController);

    OfficeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Office'];

    function OfficeDeleteController($uibModalInstance, entity, Office) {
        var vm = this;

        vm.office = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Office.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
