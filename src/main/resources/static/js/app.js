var app = angular.module('academicPublicationsGraphApp', []);

var PROJECT_URL = '/academic-publications'

app.controller('PublicationsGraphController',
		function PublicationsGraphController($scope, $http) {
			$scope.search = function(university) {
				$http
						.get(
								PROJECT_URL + '/publication-network/search?university='
										+ university).then(function(response) {
							network = $scope.network(response.data);
							networkLayer(network);
						});
			};

			$scope.network = function(publicationNetwork) {
				var nodes = [];
				var edges = [];
				var network = null;

				var rootId = publicationNetwork.rootOrganizationId;
				console.log(rootId);
				
				var orgs = publicationNetwork.organizations;
				
				for (var id in orgs) {
					nodes.push({
						id : id,
						value : 10,
						label : orgs[id].name
					});
					if (id != rootId) {
						edges.push({
							from : rootId,
							to : orgs[id].id,
							value : orgs[id].authors.length
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

app.filter('coauthors', function() {
	return function(value) {
		if (!angular.isArray(value))
			return '';
		return value.map(function(author) {
			return author.name + " | " + author.organization;
		});
	};
});
