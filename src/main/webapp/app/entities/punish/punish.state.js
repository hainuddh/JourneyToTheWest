(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('punish', {
            parent: 'entity',
            url: '/punish',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.punish.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/punish/punishes.html',
                    controller: 'PunishController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('punish');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('punish-detail', {
            parent: 'entity',
            url: '/punish/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.punish.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/punish/punish-detail.html',
                    controller: 'PunishDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('punish');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Punish', function($stateParams, Punish) {
                    return Punish.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'punish',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('punish-detail.edit', {
            parent: 'punish-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/punish/punish-dialog.html',
                    controller: 'PunishDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Punish', function(Punish) {
                            return Punish.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('punish.new', {
            parent: 'punish',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/punish/punish-dialog.html',
                    controller: 'PunishDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                punishId: null,
                                personName: null,
                                personRegisterId: null,
                                unitName: null,
                                unitRegisterId: null,
                                unitOwner: null,
                                breakLaw: null,
                                punishContent: null,
                                punishDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('punish', null, { reload: 'punish' });
                }, function() {
                    $state.go('punish');
                });
            }]
        })
        .state('punish.edit', {
            parent: 'punish',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/punish/punish-dialog.html',
                    controller: 'PunishDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Punish', function(Punish) {
                            return Punish.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('punish', null, { reload: 'punish' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('punish.delete', {
            parent: 'punish',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/punish/punish-delete-dialog.html',
                    controller: 'PunishDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Punish', function(Punish) {
                            return Punish.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('punish', null, { reload: 'punish' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
