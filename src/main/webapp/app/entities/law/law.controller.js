(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawController', LawController);

    LawController.$inject = ['$scope', '$state', 'Law', 'LawSearch'];

    function LawController ($scope, $state, Law, LawSearch) {
        var vm = this;

        vm.laws = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Law.query(function(result) {
                vm.laws = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LawSearch.query({query: vm.searchQuery}, function(result) {
                vm.laws = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
