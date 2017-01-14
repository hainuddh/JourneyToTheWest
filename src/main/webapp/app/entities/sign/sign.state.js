(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sign', {
            parent: 'entity',
            url: '/sign',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.sign.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sign/signs.html',
                    controller: 'SignController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sign');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sign-detail', {
            parent: 'entity',
            url: '/sign/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.sign.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sign/sign-detail.html',
                    controller: 'SignDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sign');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sign', function($stateParams, Sign) {
                    return Sign.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sign',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sign-detail.edit', {
            parent: 'sign-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign/sign-dialog.html',
                    controller: 'SignDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sign', function(Sign) {
                            return Sign.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sign.new', {
            parent: 'sign',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign/sign-dialog.html',
                    controller: 'SignDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                signName: null,
                                signConfig: null,
                                signCss: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sign', null, { reload: 'sign' });
                }, function() {
                    $state.go('sign');
                });
            }]
        })
        .state('sign.edit', {
            parent: 'sign',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign/sign-dialog.html',
                    controller: 'SignDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sign', function(Sign) {
                            return Sign.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sign', null, { reload: 'sign' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sign.delete', {
            parent: 'sign',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign/sign-delete-dialog.html',
                    controller: 'SignDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sign', function(Sign) {
                            return Sign.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sign', null, { reload: 'sign' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
