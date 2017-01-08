(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('TaskProjectController', TaskProjectController);

    TaskProjectController.$inject = ['$scope', '$state', 'TaskProject', 'TaskProjectSearch'];

    function TaskProjectController ($scope, $state, TaskProject, TaskProjectSearch) {
        var vm = this;

        vm.taskProjects = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TaskProject.query(function(result) {
                vm.taskProjects = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TaskProjectSearch.query({query: vm.searchQuery}, function(result) {
                vm.taskProjects = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
