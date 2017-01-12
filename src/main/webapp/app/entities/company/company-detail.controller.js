(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('CompanyDetailController', CompanyDetailController);

    CompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Company', 'DoubleRandomResult', 'Punish', 'CompanyType', 'IndustryType', 'LawenforceDepartment'];

    function CompanyDetailController($scope, $rootScope, $stateParams, previousState, entity, Company, DoubleRandomResult, Punish, CompanyType, IndustryType, LawenforceDepartment) {
        var vm = this;

        vm.company = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('journeyToTheWestApp:companyUpdate', function(event, result) {
            vm.company = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
