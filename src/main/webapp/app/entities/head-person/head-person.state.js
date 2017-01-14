(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('head-person', {
            parent: 'entity',
            url: '/head-person',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.headPerson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/head-person/head-people.html',
                    controller: 'HeadPersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('headPerson');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('head-person-detail', {
            parent: 'entity',
            url: '/head-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.headPerson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/head-person/head-person-detail.html',
                    controller: 'HeadPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('headPerson');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HeadPerson', function($stateParams, HeadPerson) {
                    return HeadPerson.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'head-person',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('head-person-detail.edit', {
            parent: 'head-person-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/head-person/head-person-dialog.html',
                    controller: 'HeadPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HeadPerson', function(HeadPerson) {
                            return HeadPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('head-person.new', {
            parent: 'head-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/head-person/head-person-dialog.html',
                    controller: 'HeadPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                job: null,
                                email: null,
                                phone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('head-person', null, { reload: 'head-person' });
                }, function() {
                    $state.go('head-person');
                });
            }]
        })
        .state('head-person.edit', {
            parent: 'head-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/head-person/head-person-dialog.html',
                    controller: 'HeadPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HeadPerson', function(HeadPerson) {
                            return HeadPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('head-person', null, { reload: 'head-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('head-person.delete', {
            parent: 'head-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/head-person/head-person-delete-dialog.html',
                    controller: 'HeadPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HeadPerson', function(HeadPerson) {
                            return HeadPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('head-person', null, { reload: 'head-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
