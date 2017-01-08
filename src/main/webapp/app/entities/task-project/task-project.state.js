(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task-project', {
            parent: 'entity',
            url: '/task-project',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.taskProject.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-project/task-projects.html',
                    controller: 'TaskProjectController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskProject');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-project-detail', {
            parent: 'entity',
            url: '/task-project/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'journeyToTheWestApp.taskProject.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-project/task-project-detail.html',
                    controller: 'TaskProjectDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskProject');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TaskProject', function($stateParams, TaskProject) {
                    return TaskProject.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'task-project',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('task-project-detail.edit', {
            parent: 'task-project-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-project/task-project-dialog.html',
                    controller: 'TaskProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskProject', function(TaskProject) {
                            return TaskProject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-project.new', {
            parent: 'task-project',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-project/task-project-dialog.html',
                    controller: 'TaskProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                taskProjectName: null,
                                taskProjectCheckDepartment: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task-project', null, { reload: 'task-project' });
                }, function() {
                    $state.go('task-project');
                });
            }]
        })
        .state('task-project.edit', {
            parent: 'task-project',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-project/task-project-dialog.html',
                    controller: 'TaskProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskProject', function(TaskProject) {
                            return TaskProject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-project', null, { reload: 'task-project' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-project.delete', {
            parent: 'task-project',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-project/task-project-delete-dialog.html',
                    controller: 'TaskProjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TaskProject', function(TaskProject) {
                            return TaskProject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-project', null, { reload: 'task-project' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
