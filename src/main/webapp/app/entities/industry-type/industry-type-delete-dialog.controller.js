(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('IndustryTypeDeleteController',IndustryTypeDeleteController);

    IndustryTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'IndustryType'];

    function IndustryTypeDeleteController($uibModalInstance, entity, IndustryType) {
        var vm = this;

        vm.industryType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            IndustryType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
