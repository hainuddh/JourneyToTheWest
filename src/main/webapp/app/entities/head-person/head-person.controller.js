(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('HeadPersonController', HeadPersonController);

    HeadPersonController.$inject = ['$scope', '$state', 'HeadPerson', 'HeadPersonSearch'];

    function HeadPersonController ($scope, $state, HeadPerson, HeadPersonSearch) {
        var vm = this;

        vm.headPeople = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            HeadPerson.query(function(result) {
                vm.headPeople = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            HeadPersonSearch.query({query: vm.searchQuery}, function(result) {
                vm.headPeople = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
