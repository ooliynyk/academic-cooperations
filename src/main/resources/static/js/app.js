var app = angular.module('academicPublicationsGraphApp', []);

var PROJECT_URL = '/itcgs';

SearchByEnum = {
    CO_AUTHORS: 0,
    PUBLICATIONS: 1
};

app.controller('PublicationsGraphController',
    function PublicationsGraphController($scope, $http) {
        $scope.search = function (university) {
            var searchBy = getSearchBy();

            var isCoAuthorsVerification = getIsCoAuthorsVerification();
            var url = null;
            if (searchBy == SearchByEnum.PUBLICATIONS) {
                var fromYear = $scope.fromYear;
                var toYear = $scope.toYear;

                url = PROJECT_URL + '/network/by-publications?university=' + university;
                if (fromYear !== undefined) {
                    url += '&fromYear=' + fromYear;
                }
                if (toYear !== undefined) {
                    url += '&toYear=' + toYear;
                }
            } else {
                url = PROJECT_URL + '/network/by-coauthors?university=' + university;
            }
            url += "&coAuthorsVerification=" + isCoAuthorsVerification;

            processingStart();
            $http({
                method: 'GET',
                url: url
            }).success(function (data, status) {
                if (status === 200) {
                    var network = $scope.mapToNetwork(data);
                    processingFinish(network);
                } else {
                    processingError("Response status " + status)
                }
            }).error(function (data) {
                processingError(data.message);
            });

        };

        $scope.mapToNetwork = function (cooperationNetwork) {
            var network = [];

            var rootId = cooperationNetwork.rootOrganizationId;

            var orgs = cooperationNetwork.organizations;

            for (var id in orgs) {
                if (orgs.hasOwnProperty(id)) {
                    if (id !== rootId) {
                        network.push({
                            id: id,
                            value: orgs[id].cooperationValue,
                            label: orgs[id].name
                        });
                    }
                }
            }

            return network;
        };
    });

function getSearchBy() {
    var searchByElement = document.getElementById("searchBy");

    return searchByElement.options[searchByElement.selectedIndex].value;
}

function getIsCoAuthorsVerification() {
    return $("[name='co-a-verif-checkbox']").bootstrapSwitch('state');
}
