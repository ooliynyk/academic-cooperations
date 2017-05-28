var NODE_SIZE_MAX = 15;

var DUMMY_LOCATION = [ 0, 0 ];

var map = L.map('map').setView([ 51.505, -0.09 ], 3);

L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
	attribution: 'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
}).addTo(map);

var markersLayer = new L.LayerGroup();
markersLayer.addTo(map);

var geojsonLayer = new L.LayerGroup();
geojsonLayer.addTo(map);

var geocoder = new L.Control.Geocoder.Nominatim();

// control that shows search box
var search = L.control();
search.onAdd = function(map) {
	this._div = L.DomUtil.get('search');
	return this._div;
};
search.addTo(map);

var info = L.control();
info.onAdd = function(map) {
	this._div = L.DomUtil.create('div', 'info'); // create a div with a class
	// "info"
	this.update();
	return this._div;
};
// method that we will use to update the control based on feature properties
// passed
info.update = function(props) {
	this._div.innerHTML = '<h4>Country cooperation value</h4>'
			+ (props ? '<b>' + props.ADMIN + '</b><br />' + props.density
					: 'Hover over a country');
};
info.addTo(map);

var legend = L.control({
	position : 'bottomright'
});
legend.onAdd = function(map) {

	var div = L.DomUtil.create('div', 'info legend'), grades = [ 0, 10, 20, 50,
			100, 200, 500, 1000 ], labels = [];

	// loop through our density intervals and generate a label with a colored
	// square for each interval
	for (var i = 0; i < grades.length; i++) {
		div.innerHTML += '<i style="background:' + getColor(grades[i] + 1)
				+ '"></i> ' + grades[i]
				+ (grades[i + 1] ? '&ndash;' + grades[i + 1] + '<br>' : '+');
	}

	return div;
};
legend.addTo(map);

var geojson = L.geoJson(countries);

// get color depending on population density value
function getColor(d) {
	return d > 1000 ? '#800026' : d > 500 ? '#BD0026' : d > 200 ? '#E31A1C'
			: d > 100 ? '#FC4E2A' : d > 50 ? '#FD8D3C' : d > 20 ? '#FEB24C'
					: d > 10 ? '#FED976' : '#FFEDA0';
}

function style(feature) {
	return {
		weight : 2,
		opacity : 1,
		color : 'white',
		dashArray : '3',
		fillOpacity : 0.7,
		fillColor : getColor(feature.properties.density)
	};
}

function highlightFeature(e) {
	var layer = e.target;

	layer.setStyle({
		weight : 5,
		color : '#666',
		dashArray : '',
		fillOpacity : 0.7
	});

	info.update(layer.feature.properties);
}

function resetHighlight(e) {
	geojson.resetStyle(e.target);
	info.update();
}

function zoomToFeature(e) {
	map.fitBounds(e.target.getBounds());
}

function onEachFeature(feature, layer) {
	layer.on({
		mouseover : highlightFeature,
		mouseout : resetHighlight,
		click : zoomToFeature
	});
}

function drawColorfulCountries(geoNetwork) {
	// console.log(geoNetwork);
	// console.log(geojson);

	geojson = L.geoJson(countries, {
		filter : function(feature, layer) {
			var country = feature.properties.ADMIN;
			console.log(country);

			if (geoNetwork.has(country)) {
				feature.properties.density = geoNetwork.get(country);
				return true;
			}
			return false;
		},
		style : style,
		onEachFeature : onEachFeature
	}).addTo(geojsonLayer);
}

function drawNode(label, size, location) {
	var marker = new L.CircleMarker(location, {
		radius : size,
		riseOnHover : true,
		fillOpacity : 0.85,
	});
	marker.bindTooltip(label, {
		permanent : false,
		offset : [ 0, 0 ]
	});
	// marker.addTo(map);
	markersLayer.addLayer(marker);
}

function calculateNodeSizeCoef(nodes) {
	var nodeValueMax = 0;
	for (var i = 0; i < nodes.length; i++) {
		if (nodes[i].value > nodeValueMax)
			nodeValueMax = nodes[i].value;
	}
	return NODE_SIZE_MAX / nodeValueMax;
}

function networkLayer(network) {
	markersLayer.clearLayers();
	geojsonLayer.clearLayers();

	var nodeSizeCoef = calculateNodeSizeCoef(network.nodes);

	var geoNetwork = new Map();

	var processingCounter = 0;
	
	var lazyCalls = [];

	network.nodes.forEach(function(node) {
		// console.log(node.label);

		geocoder.geocode(node.label, function(results) {
			processingCounter++;

			if (results == null || results.length == 0)
				return;

			var latLng = new L.LatLng(results[0].center.lat,
					results[0].center.lng);

			var country = results[0].properties.address.country;

			var value = node.value;
			if (geoNetwork.has(country)) {
				value += geoNetwork.get(country);
			}
			geoNetwork.set(country, value);

			var nodeSize = node.value * nodeSizeCoef;
			
			lazyCalls.push(function() {
				drawNode(node.label, Math.max(nodeSize, 5), latLng);
			});

			if (processingCounter == network.nodes.length) {
				drawColorfulCountries(geoNetwork);
				
				lazyCalls.forEach(function(call) {
					call();
				});
			}
		});
	});
}
