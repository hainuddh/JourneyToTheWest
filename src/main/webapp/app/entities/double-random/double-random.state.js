(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('double-random', {
            parent: 'entity',
            url: '/double-random?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.doubleRandom.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/double-random/double-randoms.html',
                    controller: 'DoubleRandomController',
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
                    $translatePartialLoader.addPart('doubleRandom');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('double-random-detail', {
            parent: 'entity',
            url: '/double-random/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.doubleRandom.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/double-random/double-random-detail.html',
                    controller: 'DoubleRandomDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doubleRandom');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DoubleRandom', function($stateParams, DoubleRandom) {
                    return DoubleRandom.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'double-random',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('double-random-detail.edit', {
            parent: 'double-random-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random/double-random-dialog.html',
                    controller: 'DoubleRandomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DoubleRandom', function(DoubleRandom) {
                            return DoubleRandom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('double-random.new', {
            parent: 'double-random',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random/double-random-dialog.html',
                    controller: 'DoubleRandomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                doubleRandomName: null,
                                doubleRandomDate: null,
                                doubleRandomNotary: null,
                                doubleRandomCompanyName: null,
                                doubleRandomCompanyArea: null,
                                doubleRandomCompanySupervisory: null,
                                doubleRandomCompanyType: null,
                                doubleRandomCompanyIndustryType: null,
                                doubleRandomCompanyRatio: null,
                                doubleRandomManagerName: null,
                                doubleRandomManagerNumber: null,
                                doubleRandomManagerDepartment: null,
                                doubleRandomManagerRatio: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('double-random', null, { reload: 'double-random' });
                }, function() {
                    $state.go('double-random');
                });
            }]
        })
        .state('double-random.edit', {
            parent: 'double-random',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random/double-random-dialog.html',
                    controller: 'DoubleRandomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DoubleRandom', function(DoubleRandom) {
                            return DoubleRandom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('double-random', null, { reload: 'double-random' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('double-random.delete', {
            parent: 'double-random',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random/double-random-delete-dialog.html',
                    controller: 'DoubleRandomDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DoubleRandom', function(DoubleRandom) {
                            return DoubleRandom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('double-random', null, { reload: 'double-random' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
