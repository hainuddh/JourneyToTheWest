'use strict';

describe('Controller Tests', function() {

    describe('Company Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCompany, MockCompanyType, MockIndustryType, MockLawenforceArea, MockLawenforceDepartment, MockDoubleRandomResult;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCompany = jasmine.createSpy('MockCompany');
            MockCompanyType = jasmine.createSpy('MockCompanyType');
            MockIndustryType = jasmine.createSpy('MockIndustryType');
            MockLawenforceArea = jasmine.createSpy('MockLawenforceArea');
            MockLawenforceDepartment = jasmine.createSpy('MockLawenforceDepartment');
            MockDoubleRandomResult = jasmine.createSpy('MockDoubleRandomResult');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Company': MockCompany,
                'CompanyType': MockCompanyType,
                'IndustryType': MockIndustryType,
                'LawenforceArea': MockLawenforceArea,
                'LawenforceDepartment': MockLawenforceDepartment,
                'DoubleRandomResult': MockDoubleRandomResult
            };
            createController = function() {
                $injector.get('$controller')("CompanyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'journeyToTheWestApp:companyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
