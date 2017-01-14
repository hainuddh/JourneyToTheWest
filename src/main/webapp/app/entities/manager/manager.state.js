(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('manager', {
            parent: 'entity',
            url: '/manager?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.manager.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/manager/managers.html',
                    controller: 'ManagerController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('manager');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('manager-detail', {
            parent: 'entity',
            url: '/manager/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.manager.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/manager/manager-detail.html',
                    controller: 'ManagerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('manager');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Manager', function($stateParams, Manager) {
                    return Manager.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'manager',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('manager-detail.edit', {
            parent: 'manager-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager/manager-dialog.html',
                    controller: 'ManagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Manager', function(Manager) {
                            return Manager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('manager.new', {
            parent: 'manager',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager/manager-dialog.html',
                    controller: 'ManagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                managerId: null,
                                managerName: null,
                                managerCardId: null,
                                managerCardType: null,
                                managerICCard: null,
                                managerSex: null,
                                managerFlag: null,
                                checkCount: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('manager', null, { reload: 'manager' });
                }, function() {
                    $state.go('manager');
                });
            }]
        })
        .state('manager.edit', {
            parent: 'manager',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager/manager-dialog.html',
                    controller: 'ManagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Manager', function(Manager) {
                            return Manager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('manager', null, { reload: 'manager' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('manager.delete', {
            parent: 'manager',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/manager/manager-delete-dialog.html',
                    controller: 'ManagerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Manager', function(Manager) {
                            return Manager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('manager', null, { reload: 'manager' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
