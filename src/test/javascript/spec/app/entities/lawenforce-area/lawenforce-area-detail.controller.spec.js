'use strict';

describe('Controller Tests', function() {

    describe('LawenforceArea Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLawenforceArea, MockCompany, MockManager;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLawenforceArea = jasmine.createSpy('MockLawenforceArea');
            MockCompany = jasmine.createSpy('MockCompany');
            MockManager = jasmine.createSpy('MockManager');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LawenforceArea': MockLawenforceArea,
                'Company': MockCompany,
                'Manager': MockManager
            };
            createController = function() {
                $injector.get('$controller')("LawenforceAreaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'journeyToTheWestApp:lawenforceAreaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
