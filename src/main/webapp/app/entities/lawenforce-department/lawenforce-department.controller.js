(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceDepartmentController', LawenforceDepartmentController);

    LawenforceDepartmentController.$inject = ['$scope', '$state', 'LawenforceDepartment', 'LawenforceDepartmentSearch'];

    function LawenforceDepartmentController ($scope, $state, LawenforceDepartment, LawenforceDepartmentSearch) {
        var vm = this;

        vm.lawenforceDepartments = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LawenforceDepartment.query(function(result) {
                vm.lawenforceDepartments = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LawenforceDepartmentSearch.query({query: vm.searchQuery}, function(result) {
                vm.lawenforceDepartments = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
