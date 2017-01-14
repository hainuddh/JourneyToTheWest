(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('OfficeDetailController', OfficeDetailController);

    OfficeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Office', 'HeadPerson'];

    function OfficeDetailController($scope, $rootScope, $stateParams, previousState, entity, Office, HeadPerson) {
        var vm = this;

        vm.office = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:officeUpdate', function(event, result) {
            vm.office = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
