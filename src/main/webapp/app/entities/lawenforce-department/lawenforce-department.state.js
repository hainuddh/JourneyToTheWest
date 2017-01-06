(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lawenforce-department', {
            parent: 'entity',
            url: '/lawenforce-department',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.lawenforceDepartment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lawenforce-department/lawenforce-departments.html',
                    controller: 'LawenforceDepartmentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lawenforceDepartment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lawenforce-department-detail', {
            parent: 'entity',
            url: '/lawenforce-department/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.lawenforceDepartment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lawenforce-department/lawenforce-department-detail.html',
                    controller: 'LawenforceDepartmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lawenforceDepartment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LawenforceDepartment', function($stateParams, LawenforceDepartment) {
                    return LawenforceDepartment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lawenforce-department',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lawenforce-department-detail.edit', {
            parent: 'lawenforce-department-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-department/lawenforce-department-dialog.html',
                    controller: 'LawenforceDepartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LawenforceDepartment', function(LawenforceDepartment) {
                            return LawenforceDepartment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lawenforce-department.new', {
            parent: 'lawenforce-department',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-department/lawenforce-department-dialog.html',
                    controller: 'LawenforceDepartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                departmentName: null,
                                departmentAddress: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lawenforce-department', null, { reload: 'lawenforce-department' });
                }, function() {
                    $state.go('lawenforce-department');
                });
            }]
        })
        .state('lawenforce-department.edit', {
            parent: 'lawenforce-department',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-department/lawenforce-department-dialog.html',
                    controller: 'LawenforceDepartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LawenforceDepartment', function(LawenforceDepartment) {
                            return LawenforceDepartment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lawenforce-department', null, { reload: 'lawenforce-department' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lawenforce-department.delete', {
            parent: 'lawenforce-department',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lawenforce-department/lawenforce-department-delete-dialog.html',
                    controller: 'LawenforceDepartmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LawenforceDepartment', function(LawenforceDepartment) {
                            return LawenforceDepartment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lawenforce-department', null, { reload: 'lawenforce-department' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
