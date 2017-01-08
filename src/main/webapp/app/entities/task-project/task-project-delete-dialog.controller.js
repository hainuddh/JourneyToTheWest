(function() {
    'use strict';

    angular
        .module('journeyToTheWestApp')
        .controller('TaskProjectDeleteController',TaskProjectDeleteController);

    TaskProjectDeleteController.$inject = ['$uibModalInstance', 'entity', 'TaskProject'];

    function TaskProjectDeleteController($uibModalInstance, entity, TaskProject) {
        var vm = this;

        vm.taskProject = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TaskProject.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
