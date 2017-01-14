(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('OfficeController', OfficeController);

    OfficeController.$inject = ['$scope', '$state', 'Office', 'OfficeSearch'];

    function OfficeController ($scope, $state, Office, OfficeSearch) {
        var vm = this;

        vm.offices = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Office.query(function(result) {
                vm.offices = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OfficeSearch.query({query: vm.searchQuery}, function(result) {
                vm.offices = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
