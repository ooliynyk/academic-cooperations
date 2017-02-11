var map = L.map('map').setView([ 51.505, -0.09 ], 3);

L
		.tileLayer(
				'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
				{
					maxZoom : 18,
					id : 'mapbox.streets',
					accessToken : 'pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw'
				}).addTo(map);

var markersLayer = new L.LayerGroup();
markersLayer.addTo(map);

var geocoder = new L.Control.Geocoder.Nominatim();

var NODE_SIZE_MAX = 30;

var DUMMY_LOCATION = [ 0, 0 ];

function networkLayer(network) {
	markersLayer.clearLayers();

	var nodeSizeCoef = calculateNodeSizeCoef(network.nodes);

	var geoNetwork = new Set();
	
	var processingCounter = 0;

	network.nodes.forEach(function(node) {
		console.log(node.label);

		geocoder.geocode(node.label, function(results) {
			processingCounter++;
			
			if (results == null || results.length == 0)
				return;
			
			var latLng = new L.LatLng(results[0].center.lat,
					results[0].center.lng);

			var country = results[0].properties.address.country;

			geoNetwork.add({
				id : node.id,
				value : node.value,
				country : country
			});

			var nodeSize = node.value * nodeSizeCoef;
			drawNode(node.label, Math.max(nodeSize, 5), latLng);
			
			if (processingCounter == network.nodes.length) {
				drawCountriesLayer(geoNetwork);
			}
		});
	});
}

function drawCountriesLayer(geoNetwork) {
	console.log(geoNetwork);
	
}

function drawEdges(edges, locations) {
	console.log('Draw edges');

	for (var i = 0; i < edges.length; i++) {
		var edge = edges[i];

		console.log(edge);
		if (locations[edge.from] == DUMMY_LOCATION
				|| locations[edge.to] == DUMMY_LOCATION) {
			console.log('Dummy location');
			continue;
		}

		drawEdge(edge, locations[edge.from], locations[edge.to])

		// zoom the map to the polyline
		// map.fitBounds(polyline.getBounds());
	}
}

function drawNode(label, size, location) {
	var marker = new L.CircleMarker(location, {
		radius : size,
	});
	marker.bindTooltip(label, {
		permanent : false,
		offset : [ 0, 0 ]
	});
	// marker.addTo(map);
	markersLayer.addLayer(marker);
}

function drawEdge(edge, fromLocation, toLocation) {
	var polyline = L.polyline([ fromLocation, toLocation ], {
		color : 'red',
		opacity : 0.5,
		fillOpacity : 0.8,
		weight : edge.value
	})/* .addTo(map) */;
	markersLayer.addLayer(polyline);
}

function calculateNodeSizeCoef(nodes) {
	var nodeValueMax = 0;
	for (var i = 0; i < nodes.length; i++) {
		if (nodes[i].value > nodeValueMax)
			nodeValueMax = nodes[i].value;
	}
	return NODE_SIZE_MAX / nodeValueMax;
}
