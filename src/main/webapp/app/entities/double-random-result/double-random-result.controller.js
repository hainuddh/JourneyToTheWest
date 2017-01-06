(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomResultController', DoubleRandomResultController);

    DoubleRandomResultController.$inject = ['$scope', '$state', 'DoubleRandomResult', 'DoubleRandomResultSearch'];

    function DoubleRandomResultController ($scope, $state, DoubleRandomResult, DoubleRandomResultSearch) {
        var vm = this;

        vm.doubleRandomResults = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            DoubleRandomResult.query(function(result) {
                vm.doubleRandomResults = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DoubleRandomResultSearch.query({query: vm.searchQuery}, function(result) {
                vm.doubleRandomResults = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
