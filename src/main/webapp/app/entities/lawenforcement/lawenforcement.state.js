(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lawenforcement', {
            parent: 'entity',
            url: '/lawenforcement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.lawenforcement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lawenforcement/lawenforcements.html',
                    controller: 'LawenforcementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lawenforcement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lawenforcement-detail', {
            parent: 'entity',
            url: '/lawenforcement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.lawenforcement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lawenforcement/lawenforcement-detail.html',
                    controller: 'LawenforcementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lawenforcement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lawenforcement', function($stateParams, Lawenforcement) {
                    return Lawenforcement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lawenforcement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lawenforcement-detail.edit', {
            parent: 'lawenforcement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforcement/lawenforcement-dialog.html',
                    controller: 'LawenforcementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lawenforcement', function(Lawenforcement) {
                            return Lawenforcement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lawenforcement.new', {
            parent: 'lawenforcement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforcement/lawenforcement-dialog.html',
                    controller: 'LawenforcementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                enforcementName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lawenforcement', null, { reload: 'lawenforcement' });
                }, function() {
                    $state.go('lawenforcement');
                });
            }]
        })
        .state('lawenforcement.edit', {
            parent: 'lawenforcement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforcement/lawenforcement-dialog.html',
                    controller: 'LawenforcementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lawenforcement', function(Lawenforcement) {
                            return Lawenforcement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lawenforcement', null, { reload: 'lawenforcement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lawenforcement.delete', {
            parent: 'lawenforcement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforcement/lawenforcement-delete-dialog.html',
                    controller: 'LawenforcementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lawenforcement', function(Lawenforcement) {
                            return Lawenforcement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lawenforcement', null, { reload: 'lawenforcement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
