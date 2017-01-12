(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('SignController', SignController);

    SignController.$inject = ['$scope', '$state', 'Sign', 'SignSearch'];

    function SignController ($scope, $state, Sign, SignSearch) {
        var vm = this;

        vm.signs = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Sign.query(function(result) {
                vm.signs = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SignSearch.query({query: vm.searchQuery}, function(result) {
                vm.signs = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
