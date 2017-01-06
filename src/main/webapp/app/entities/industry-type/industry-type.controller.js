(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('IndustryTypeController', IndustryTypeController);

    IndustryTypeController.$inject = ['$scope', '$state', 'IndustryType', 'IndustryTypeSearch'];

    function IndustryTypeController ($scope, $state, IndustryType, IndustryTypeSearch) {
        var vm = this;

        vm.industryTypes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            IndustryType.query(function(result) {
                vm.industryTypes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            IndustryTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.industryTypes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
