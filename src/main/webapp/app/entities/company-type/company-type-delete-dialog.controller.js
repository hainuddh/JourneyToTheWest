(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('CompanyTypeDeleteController',CompanyTypeDeleteController);

    CompanyTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyType'];

    function CompanyTypeDeleteController($uibModalInstance, entity, CompanyType) {
        var vm = this;

        vm.companyType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
