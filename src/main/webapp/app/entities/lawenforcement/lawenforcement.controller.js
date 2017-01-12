(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforcementController', LawenforcementController);

    LawenforcementController.$inject = ['$scope', '$state', 'Lawenforcement', 'LawenforcementSearch'];

    function LawenforcementController ($scope, $state, Lawenforcement, LawenforcementSearch) {
        var vm = this;

        vm.lawenforcements = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Lawenforcement.query(function(result) {
                vm.lawenforcements = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LawenforcementSearch.query({query: vm.searchQuery}, function(result) {
                vm.lawenforcements = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
