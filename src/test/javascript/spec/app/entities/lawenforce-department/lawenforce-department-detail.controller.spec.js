'use strict';

describe('Controller Tests', function() {

    describe('LawenforceDepartment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLawenforceDepartment, MockCompany, MockManager;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLawenforceDepartment = jasmine.createSpy('MockLawenforceDepartment');
            MockCompany = jasmine.createSpy('MockCompany');
            MockManager = jasmine.createSpy('MockManager');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LawenforceDepartment': MockLawenforceDepartment,
                'Company': MockCompany,
                'Manager': MockManager
            };
            createController = function() {
                $injector.get('$controller')("LawenforceDepartmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'journeyToTheWestApp:lawenforceDepartmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
