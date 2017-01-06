(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-type', {
            parent: 'entity',
            url: '/company-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.companyType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-type/company-types.html',
                    controller: 'CompanyTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-type-detail', {
            parent: 'entity',
            url: '/company-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.companyType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-type/company-type-detail.html',
                    controller: 'CompanyTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanyType', function($stateParams, CompanyType) {
                    return CompanyType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-type-detail.edit', {
            parent: 'company-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-type/company-type-dialog.html',
                    controller: 'CompanyTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyType', function(CompanyType) {
                            return CompanyType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-type.new', {
            parent: 'company-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-type/company-type-dialog.html',
                    controller: 'CompanyTypeDialogController',
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
                    $state.go('company-type', null, { reload: 'company-type' });
                }, function() {
                    $state.go('company-type');
                });
            }]
        })
        .state('company-type.edit', {
            parent: 'company-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-type/company-type-dialog.html',
                    controller: 'CompanyTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyType', function(CompanyType) {
                            return CompanyType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-type', null, { reload: 'company-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-type.delete', {
            parent: 'company-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-type/company-type-delete-dialog.html',
                    controller: 'CompanyTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyType', function(CompanyType) {
                            return CompanyType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-type', null, { reload: 'company-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
