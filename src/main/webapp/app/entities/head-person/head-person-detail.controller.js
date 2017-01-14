(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('HeadPersonDetailController', HeadPersonDetailController);

    HeadPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HeadPerson', 'Office'];

    function HeadPersonDetailController($scope, $rootScope, $stateParams, previousState, entity, HeadPerson, Office) {
        var vm = this;

        vm.headPerson = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:headPersonUpdate', function(event, result) {
            vm.headPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
