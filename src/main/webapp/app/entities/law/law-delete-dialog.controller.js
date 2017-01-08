(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawDeleteController',LawDeleteController);

    LawDeleteController.$inject = ['$uibModalInstance', 'entity', 'Law'];

    function LawDeleteController($uibModalInstance, entity, Law) {
        var vm = this;

        vm.law = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Law.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
