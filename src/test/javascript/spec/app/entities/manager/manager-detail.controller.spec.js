'use strict';

describe('Controller Tests', function() {

    describe('Manager Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockManager, MockUser, MockLawenforceDepartment, MockDoubleRandomResult;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockManager = jasmine.createSpy('MockManager');
            MockUser = jasmine.createSpy('MockUser');
            MockLawenforceDepartment = jasmine.createSpy('MockLawenforceDepartment');
            MockDoubleRandomResult = jasmine.createSpy('MockDoubleRandomResult');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Manager': MockManager,
                'User': MockUser,
                'LawenforceDepartment': MockLawenforceDepartment,
                'DoubleRandomResult': MockDoubleRandomResult
            };
            createController = function() {
                $injector.get('$controller')("ManagerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'journeyToTheWestApp:managerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
