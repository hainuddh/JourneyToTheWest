(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('double-random-result', {
            parent: 'entity',
            url: '/double-random-result',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.doubleRandomResult.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/double-random-result/double-random-results.html',
                    controller: 'DoubleRandomResultController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doubleRandomResult');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('double-random-result-detail', {
            parent: 'entity',
            url: '/double-random-result/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.doubleRandomResult.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/double-random-result/double-random-result-detail.html',
                    controller: 'DoubleRandomResultDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doubleRandomResult');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DoubleRandomResult', function($stateParams, DoubleRandomResult) {
                    return DoubleRandomResult.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'double-random-result',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('double-random-result-detail.edit', {
            parent: 'double-random-result-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random-result/double-random-result-dialog.html',
                    controller: 'DoubleRandomResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DoubleRandomResult', function(DoubleRandomResult) {
                            return DoubleRandomResult.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('double-random-result.new', {
            parent: 'double-random-result',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random-result/double-random-result-dialog.html',
                    controller: 'DoubleRandomResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                companyName: null,
                                companyRegisterId: null,
                                people: null,
                                task: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('double-random-result', null, { reload: 'double-random-result' });
                }, function() {
                    $state.go('double-random-result');
                });
            }]
        })
        .state('double-random-result.edit', {
            parent: 'double-random-result',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random-result/double-random-result-dialog.html',
                    controller: 'DoubleRandomResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DoubleRandomResult', function(DoubleRandomResult) {
                            return DoubleRandomResult.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('double-random-result', null, { reload: 'double-random-result' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('double-random-result.delete', {
            parent: 'double-random-result',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/double-random-result/double-random-result-delete-dialog.html',
                    controller: 'DoubleRandomResultDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DoubleRandomResult', function(DoubleRandomResult) {
                            return DoubleRandomResult.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('double-random-result', null, { reload: 'double-random-result' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
