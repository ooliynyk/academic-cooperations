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

// Initialize the SVG layer
// map._initPathRoot();

// We pick up the SVG from the map object

function addpoint(lat, lon, text, radius) {
	var circle = L.circleMarker([ lat, lon ], {
		color : 'red',
		fillColor : '#f03',
		fillOpacity : 0.5,
		radius : 5
	}).addTo(map);

}

var DUMMY_LOCATION = [ 0, 0 ];

function networkLayer(network) {
	markersLayer.clearLayers();
	
	var locations = {};
	network.nodes.forEach(function(node) {
		console.log(node.label);

		geocoder.geocode(node.label, function(results) {
			if (results == null || results.length < 1) {
				locations[node.id] = DUMMY_LOCATION;
				console.log('Dummy location for ' + node.label);
				return;
			}

			var latLng = new L.LatLng(results[0].center.lat,
					results[0].center.lng);

			locations[node.id] = latLng;

			drawNode(node.label, latLng);

			if (Object.keys(locations).length == network.nodes.length) {
				drawEdges(network.edges, locations);
			}
		});
	});

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

function drawNode(label, location) {
	var marker = new L.CircleMarker(location);
	marker.bindTooltip(label, {
		permanent : false,
		offset : [ 0, 0 ]
	});
	//marker.addTo(map);
	markersLayer.addLayer(marker);
}

function drawEdge(edge, fromLocation, toLocation) {
	var polyline = L.polyline([ fromLocation, toLocation ], {
		color : 'red',
		opacity : 0.5,
		fillOpacity : 0.8,
		weight : edge.value
	})/*.addTo(map)*/;
	markersLayer.addLayer(polyline);
}
