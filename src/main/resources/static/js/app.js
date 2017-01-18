var app = angular.module('academicPublicationsGraphApp', []);

var PROJECT_URL = '/academic-publications'

app.controller('PublicationsGraphController',
		function PublicationsGraphController($scope, $http) {
			$scope.search = function(university) {
				$http.get(
						PROJECT_URL + '/cooperation-network/by-coauthors?university='
								+ university).then(function(response) {
					network = $scope.network(response.data);
					networkLayer(network);
				});
			};

			$scope.network = function(cooperationNetwork) {
				var nodes = [];
				var edges = [];
				var network = null;

				var rootId = cooperationNetwork.rootOrganizationId;
				console.log(rootId);

				var orgs = cooperationNetwork.organizations;

				for ( var id in orgs) {
					nodes.push({
						id : id,
						value : 10,
						label : orgs[id].name
					});
					if (id != rootId) {
						edges.push({
							from : rootId,
							to : orgs[id].id,
							value : orgs[id].cooperationValue
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
