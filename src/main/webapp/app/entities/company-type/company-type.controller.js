(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('CompanyTypeController', CompanyTypeController);

    CompanyTypeController.$inject = ['$scope', '$state', 'CompanyType', 'CompanyTypeSearch'];

    function CompanyTypeController ($scope, $state, CompanyType, CompanyTypeSearch) {
        var vm = this;

        vm.companyTypes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CompanyType.query(function(result) {
                vm.companyTypes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanyTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.companyTypes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
