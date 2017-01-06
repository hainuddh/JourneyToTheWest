(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lawenforce-area', {
            parent: 'entity',
            url: '/lawenforce-area',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.lawenforceArea.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lawenforce-area/lawenforce-areas.html',
                    controller: 'LawenforceAreaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lawenforceArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lawenforce-area-detail', {
            parent: 'entity',
            url: '/lawenforce-area/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.lawenforceArea.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lawenforce-area/lawenforce-area-detail.html',
                    controller: 'LawenforceAreaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lawenforceArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LawenforceArea', function($stateParams, LawenforceArea) {
                    return LawenforceArea.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lawenforce-area',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lawenforce-area-detail.edit', {
            parent: 'lawenforce-area-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-area/lawenforce-area-dialog.html',
                    controller: 'LawenforceAreaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LawenforceArea', function(LawenforceArea) {
                            return LawenforceArea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lawenforce-area.new', {
            parent: 'lawenforce-area',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-area/lawenforce-area-dialog.html',
                    controller: 'LawenforceAreaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                areaName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lawenforce-area', null, { reload: 'lawenforce-area' });
                }, function() {
                    $state.go('lawenforce-area');
                });
            }]
        })
        .state('lawenforce-area.edit', {
            parent: 'lawenforce-area',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-area/lawenforce-area-dialog.html',
                    controller: 'LawenforceAreaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LawenforceArea', function(LawenforceArea) {
                            return LawenforceArea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lawenforce-area', null, { reload: 'lawenforce-area' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lawenforce-area.delete', {
            parent: 'lawenforce-area',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-area/lawenforce-area-delete-dialog.html',
                    controller: 'LawenforceAreaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LawenforceArea', function(LawenforceArea) {
                            return LawenforceArea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lawenforce-area', null, { reload: 'lawenforce-area' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
