var app = angular.module('academicPublicationsGraphApp', []);

var PROJECT_URL = '/academic-cooperations';

SearchByEnum = {
	CO_AUTHORS : 0,
	PUBLICATIONS : 1
};

var EDGE_MAX_VALUE = 8.0;

app.controller('PublicationsGraphController',
		function PublicationsGraphController($scope, $http) {
			$scope.search = function(university) {
				var searchBy = getSearchBy();

				var url = null;
				if (searchBy == SearchByEnum.CO_AUTHORS) {
					url = PROJECT_URL + '/network/by-coauthors?university='
							+ university;
				} else if (searchBy == SearchByEnum.PUBLICATIONS) {
					var fromYear = $scope.fromYear;
					var toYear = $scope.toYear;

					url = PROJECT_URL + '/network/by-publications?university='
							+ university;
					if (fromYear != undefined) {
						url += '&fromYear=' + fromYear;
					}
					if (toYear != undefined) {
						url += '&toYear=' + toYear;
					}
				}

				if (url != null) {
					$http.get(url).then(function(response) {
						network = $scope.network(response.data);
						networkLayer(network);
					});
				}

			};

			$scope.network = function(cooperationNetwork) {
				var nodes = [];
				var edges = [];
				var network = null;

				var rootId = cooperationNetwork.rootOrganizationId;
				console.log(rootId);

				var orgs = cooperationNetwork.organizations;
				
				// we don't interested of root organization real cooperation value
				orgs[rootId].cooperationValue = 1;

				var edgeValueCoef = calculateEdgeValueCoef(cooperationNetwork);
				for ( var id in orgs) {
					nodes.push({
						id : id,
						value : orgs[id].cooperationValue,
						label : orgs[id].name
					});
					if (id != rootId) {
						var cooperationValue = orgs[id].cooperationValue * edgeValueCoef;
						edges.push({
							from : rootId,
							to : orgs[id].id,
							value : Math.max(cooperationValue, 1)
						});
					}
				}

				var data = {
					nodes : nodes,
					edges : edges
				};
				return data;
			};

		});

function calculateEdgeValueCoef(cooperationNetwork) {
	var rootId = cooperationNetwork.rootOrganizationId;
	var orgs = cooperationNetwork.organizations;

	var maxCooperationValue = 0;
	for ( var id in orgs) {
		if (id != rootId) {
			if (orgs[id].cooperationValue > maxCooperationValue) {
				maxCooperationValue = orgs[id].cooperationValue;
			}
		}
	}

	return EDGE_MAX_VALUE / maxCooperationValue;
}

function getSearchBy() {
	var searchByElement = document.getElementById("searchBy");

	return searchByElement.options[searchByElement.selectedIndex].value;
}
