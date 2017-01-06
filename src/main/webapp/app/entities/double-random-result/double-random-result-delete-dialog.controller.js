(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomResultDeleteController',DoubleRandomResultDeleteController);

    DoubleRandomResultDeleteController.$inject = ['$uibModalInstance', 'entity', 'DoubleRandomResult'];

    function DoubleRandomResultDeleteController($uibModalInstance, entity, DoubleRandomResult) {
        var vm = this;

        vm.doubleRandomResult = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DoubleRandomResult.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
