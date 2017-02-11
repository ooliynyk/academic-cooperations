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

var geojson = L.geoJson(countries);

var NODE_SIZE_MAX = 30;

var DUMMY_LOCATION = [ 0, 0 ];

function networkLayer(network) {
	markersLayer.clearLayers();

	var nodeSizeCoef = calculateNodeSizeCoef(network.nodes);

	var geoNetwork = new Set();
	
	var processingCounter = 0;

	network.nodes.forEach(function(node) {
		//console.log(node.label);

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
				drawColorfulCountries(geoNetwork);
			}
		});
	});
}

function drawColorfulCountries(geoNetwork) {
	//console.log(geoNetwork);
	//console.log(geojson);
	
	geojson = L.geoJson(countries, {
	    filter: function(feature, layer) {
	    	var country = feature.properties.ADMIN;
	    	console.log(country);

	    	for (let geoNode of geoNetwork) {
	    		if (geoNode.country == country)
	    			return true;
	    	}
	    	return false;
	    },
		style: style,
	    onEachFeature: onEachFeature
	}).addTo(map);
}

function drawNode(label, size, location) {
	var marker = new L.CircleMarker(location, {
		radius : size,
		riseOnHover: true
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

function getColor(d) {
    return d > 1000 ? '#800026' :
           d > 500  ? '#BD0026' :
           d > 200  ? '#E31A1C' :
           d > 100  ? '#FC4E2A' :
           d > 50   ? '#FD8D3C' :
           d > 20   ? '#FEB24C' :
           d > 10   ? '#FED976' :
                      '#FFEDA0';
}

function style(feature) {
	return {
		weight: 2,
		opacity: 1,
		color: 'white',
		dashArray: '3',
		fillOpacity: 0.7,
		fillColor: getColor(feature.properties.density)
	};
}

function highlightFeature(e) {
    var layer = e.target;

    layer.setStyle({
        weight: 5,
        color: '#666',
        dashArray: '',
        fillOpacity: 0.7
    });

    if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
        layer.bringToFront();
    }
}

function resetHighlight(e) {
    geojson.resetStyle(e.target);
}

function zoomToFeature(e) {
    map.fitBounds(e.target.getBounds());
}

function onEachFeature(feature, layer) {
    layer.on({
        mouseover: highlightFeature,
        mouseout: resetHighlight,
        click: zoomToFeature
    });
}


