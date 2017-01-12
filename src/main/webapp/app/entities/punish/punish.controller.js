(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('PunishController', PunishController);

    PunishController.$inject = ['$scope', '$state', 'Punish', 'PunishSearch'];

    function PunishController ($scope, $state, Punish, PunishSearch) {
        var vm = this;

        vm.punishes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Punish.query(function(result) {
                vm.punishes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PunishSearch.query({query: vm.searchQuery}, function(result) {
                vm.punishes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
