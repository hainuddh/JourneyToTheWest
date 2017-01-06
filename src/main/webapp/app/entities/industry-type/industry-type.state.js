(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('industry-type', {
            parent: 'entity',
            url: '/industry-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.industryType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/industry-type/industry-types.html',
                    controller: 'IndustryTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('industryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('industry-type-detail', {
            parent: 'entity',
            url: '/industry-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.industryType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/industry-type/industry-type-detail.html',
                    controller: 'IndustryTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('industryType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'IndustryType', function($stateParams, IndustryType) {
                    return IndustryType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'industry-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('industry-type-detail.edit', {
            parent: 'industry-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/industry-type/industry-type-dialog.html',
                    controller: 'IndustryTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IndustryType', function(IndustryType) {
                            return IndustryType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('industry-type.new', {
            parent: 'industry-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/industry-type/industry-type-dialog.html',
                    controller: 'IndustryTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typeName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('industry-type', null, { reload: 'industry-type' });
                }, function() {
                    $state.go('industry-type');
                });
            }]
        })
        .state('industry-type.edit', {
            parent: 'industry-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/industry-type/industry-type-dialog.html',
                    controller: 'IndustryTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IndustryType', function(IndustryType) {
                            return IndustryType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('industry-type', null, { reload: 'industry-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('industry-type.delete', {
            parent: 'industry-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/industry-type/industry-type-delete-dialog.html',
                    controller: 'IndustryTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['IndustryType', function(IndustryType) {
                            return IndustryType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('industry-type', null, { reload: 'industry-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
