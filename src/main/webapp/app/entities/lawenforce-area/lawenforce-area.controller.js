(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceAreaController', LawenforceAreaController);

    LawenforceAreaController.$inject = ['$scope', '$state', 'LawenforceArea', 'LawenforceAreaSearch'];

    function LawenforceAreaController ($scope, $state, LawenforceArea, LawenforceAreaSearch) {
        var vm = this;

        vm.lawenforceAreas = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LawenforceArea.query(function(result) {
                vm.lawenforceAreas = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LawenforceAreaSearch.query({query: vm.searchQuery}, function(result) {
                vm.lawenforceAreas = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
