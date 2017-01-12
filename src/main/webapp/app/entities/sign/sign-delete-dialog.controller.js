(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('SignDeleteController',SignDeleteController);

    SignDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sign'];

    function SignDeleteController($uibModalInstance, entity, Sign) {
        var vm = this;

        vm.sign = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sign.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
