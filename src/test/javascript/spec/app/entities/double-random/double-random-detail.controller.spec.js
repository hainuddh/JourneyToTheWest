'use strict';

describe('Controller Tests', function() {

    describe('DoubleRandom Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDoubleRandom, MockTask, MockDoubleRandomResult;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDoubleRandom = jasmine.createSpy('MockDoubleRandom');
            MockTask = jasmine.createSpy('MockTask');
            MockDoubleRandomResult = jasmine.createSpy('MockDoubleRandomResult');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DoubleRandom': MockDoubleRandom,
                'Task': MockTask,
                'DoubleRandomResult': MockDoubleRandomResult
            };
            createController = function() {
                $injector.get('$controller')("DoubleRandomDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'journeyToTheWestApp:doubleRandomUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
