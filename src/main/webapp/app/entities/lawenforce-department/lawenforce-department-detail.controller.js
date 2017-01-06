(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('LawenforceDepartmentDetailController', LawenforceDepartmentDetailController);

    LawenforceDepartmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LawenforceDepartment', 'Company', 'Manager'];

    function LawenforceDepartmentDetailController($scope, $rootScope, $stateParams, previousState, entity, LawenforceDepartment, Company, Manager) {
        var vm = this;

        vm.lawenforceDepartment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:lawenforceDepartmentUpdate', function(event, result) {
            vm.lawenforceDepartment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
