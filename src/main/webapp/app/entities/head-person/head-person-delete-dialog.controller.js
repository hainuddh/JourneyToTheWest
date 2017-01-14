(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('HeadPersonDeleteController',HeadPersonDeleteController);

    HeadPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'HeadPerson'];

    function HeadPersonDeleteController($uibModalInstance, entity, HeadPerson) {
        var vm = this;

        vm.headPerson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HeadPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
