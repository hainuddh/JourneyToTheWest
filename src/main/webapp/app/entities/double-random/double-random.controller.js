(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomController', DoubleRandomController);

    DoubleRandomController.$inject = ['$scope', '$state', 'DoubleRandom', 'DoubleRandomSearch'];

    function DoubleRandomController ($scope, $state, DoubleRandom, DoubleRandomSearch) {
        var vm = this;

        vm.doubleRandoms = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            DoubleRandom.query(function(result) {
                vm.doubleRandoms = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DoubleRandomSearch.query({query: vm.searchQuery}, function(result) {
                vm.doubleRandoms = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
