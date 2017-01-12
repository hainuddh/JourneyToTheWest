(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforcementDetailController', LawenforcementDetailController);

    LawenforcementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lawenforcement', 'Punish'];

    function LawenforcementDetailController($scope, $rootScope, $stateParams, previousState, entity, Lawenforcement, Punish) {
        var vm = this;

        vm.lawenforcement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:lawenforcementUpdate', function(event, result) {
            vm.lawenforcement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
