(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceDepartmentDeleteController',LawenforceDepartmentDeleteController);

    LawenforceDepartmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'LawenforceDepartment'];

    function LawenforceDepartmentDeleteController($uibModalInstance, entity, LawenforceDepartment) {
        var vm = this;

        vm.lawenforceDepartment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LawenforceDepartment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
