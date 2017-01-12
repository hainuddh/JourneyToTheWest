(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('PunishDetailController', PunishDetailController);

    PunishDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Punish', 'LawenforceDepartment', 'Company'];

    function PunishDetailController($scope, $rootScope, $stateParams, previousState, entity, Punish, LawenforceDepartment, Company) {
        var vm = this;

        vm.punish = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:punishUpdate', function(event, result) {
            vm.punish = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
