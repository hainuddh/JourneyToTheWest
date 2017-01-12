(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('DoubleRandomDetailController', DoubleRandomDetailController);

    DoubleRandomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DoubleRandom', 'DoubleRandomResult', 'Task'];

    function DoubleRandomDetailController($scope, $rootScope, $stateParams, previousState, entity, DoubleRandom, DoubleRandomResult, Task) {
        var vm = this;

        vm.doubleRandom = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:doubleRandomUpdate', function(event, result) {
            vm.doubleRandom = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
