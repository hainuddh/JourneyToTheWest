(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('ManagerController', ManagerController);

    ManagerController.$inject = ['$scope', '$state', 'Manager', 'ManagerSearch'];

    function ManagerController ($scope, $state, Manager, ManagerSearch) {
        var vm = this;

        vm.managers = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Manager.query(function(result) {
                vm.managers = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ManagerSearch.query({query: vm.searchQuery}, function(result) {
                vm.managers = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
