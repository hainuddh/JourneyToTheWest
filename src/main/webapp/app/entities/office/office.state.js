(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('office', {
            parent: 'entity',
            url: '/office',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.office.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/office/offices.html',
                    controller: 'OfficeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('office');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('office-detail', {
            parent: 'entity',
            url: '/office/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.office.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/office/office-detail.html',
                    controller: 'OfficeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('office');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Office', function($stateParams, Office) {
                    return Office.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'office',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('office-detail.edit', {
            parent: 'office-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office/office-dialog.html',
                    controller: 'OfficeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Office', function(Office) {
                            return Office.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('office.new', {
            parent: 'office',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office/office-dialog.html',
                    controller: 'OfficeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                officeName: null,
                                officeDuty: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('office', null, { reload: 'office' });
                }, function() {
                    $state.go('office');
                });
            }]
        })
        .state('office.edit', {
            parent: 'office',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office/office-dialog.html',
                    controller: 'OfficeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Office', function(Office) {
                            return Office.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('office', null, { reload: 'office' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('office.delete', {
            parent: 'office',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office/office-delete-dialog.html',
                    controller: 'OfficeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Office', function(Office) {
                            return Office.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('office', null, { reload: 'office' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
