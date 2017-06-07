var NODE_SIZE_MAX = 15;
var NETWORK = [];

var map = L.map('map').setView([51.505, -0.09], 3);

L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
    attribution: 'Information technology of identification of a scientific cooperation. Vinnytsia National Technical University 2017'
}).addTo(map);

var markersLayer = new L.LayerGroup();
markersLayer.addTo(map);

var geojsonLayer = new L.LayerGroup();
geojsonLayer.addTo(map);

var geocoder = new L.Control.Geocoder.Nominatim();
var googleMapsGeocoder = new L.Control.Geocoder.Google('AIzaSyAR9MQdn8zLoLQBoT95oyrfeT-zPYygkqo');

/*label = "University of Gothenburg";
 geocode(label, function(latLng, country) {
 console.log(latLng);
 console.log(country);
 });*/

// control that shows search box
var search = L.control();
search.onAdd = function (map) {
    this._div = L.DomUtil.get('search');
    return this._div;
};
search.addTo(map);

var info = L.control();
info.onAdd = function (map) {
    this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
    this.update();
    return this._div;
};
// method that we will use to update the control based on feature properties passed
info.update = function (props) {
    if (props) {
        this._div.innerHTML = '<h4>Country cooperation value</h4>' + '<b>'
            + props.ADMIN + '</b><br />' + props.density;
        L.DomUtil.removeClass(this._div, 'invisible');
    } else {
        L.DomUtil.addClass(this._div, 'invisible');
    }
};
info.addTo(map);

var legend = L.control({
    position: 'bottomright'
});
legend.onAdd = function (map) {

    var div = L.DomUtil.create('div', 'info legend'), grades = [0, 10, 20, 50,
        100, 200, 500, 1000], labels = [];

    // loop through our density intervals and generate a label with a colored square for each interval
    for (var i = 0; i < grades.length; i++) {
        div.innerHTML += '<i style="background:' + getColor(grades[i] + 1)
            + '"></i> ' + grades[i]
            + (grades[i + 1] ? '&ndash;' + grades[i + 1] + '<br>' : '+');
    }

    return div;
};
legend.addTo(map);

var geojson = L.geoJson(countries);

var dataTableWindow = L.control.window(map, {
    content: '<table id="coopTable" class="display" width="100%"></table>'
});

var dataTableButton = L.easyButton('fa-table', function (btn, map) {
    makeDataTable();
}).addTo(map);
dataTableButton.disable();


//Functions to either disable (onmouseover) or enable (onmouseout) the map's dragging
function controlEnter(e) {
    map.dragging.disable();
    map.doubleClickZoom.disable();
}

function controlLeave() {
    map.dragging.enable();
    map.doubleClickZoom.enable();
}

var inputTags = document.getElementsByTagName("input")
for (var i = 0; i < inputTags.length; i++) {
    inputTags[i].onmouseover = controlEnter;
    inputTags[i].onmouseout = controlLeave;
}

// get color depending on population density value
function getColor(d) {
    return d > 1000 ? '#800026' : d > 500 ? '#BD0026' : d > 200 ? '#E31A1C'
        : d > 100 ? '#FC4E2A' : d > 50 ? '#FD8D3C' : d > 20 ? '#FEB24C'
            : d > 10 ? '#FED976' : '#FFEDA0';
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
        mouseover: highlightFeature,
        mouseout: resetHighlight,
        click: zoomToFeature
    });
}

function drawColorfulCountries(geoNetwork) {
    geojson = L.geoJson(countries, {
        filter: function (feature, layer) {
            var country = feature.properties.ADMIN;

            if (geoNetwork.has(country)) {
                feature.properties.density = geoNetwork.get(country);
                return true;
            }
            return false;
        },
        style: style,
        onEachFeature: onEachFeature
    }).addTo(geojsonLayer);
}

function drawNode(label, size, location) {
    var marker = new L.CircleMarker(location, {
        radius: size,
        riseOnHover: true,
        fillOpacity: 0.85,
    });
    marker.bindTooltip(label, {
        permanent: false,
        offset: [0, 0]
    });
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

function clearNetwork() {
    markersLayer.clearLayers();
    geojsonLayer.clearLayers();
}

function geocode(label, callback) {
    if (typeof callback === 'function') {
        geocoder.geocode(label, function (nominatimResults) {
            var latLng = geocoderResultsToLatLng(nominatimResults);
            if (latLng !== null) {
                var country = nominatimResults[0].properties.address.country;

                return callback(latLng, country);
            } else {
                console.log("Organization: \'" + label + "\' not found by nominatim. Trying google maps");
                googleMapsGeocoder.geocode(label, function (googleMapsResults) {
                    latLng = geocoderResultsToLatLng(googleMapsResults);
                    if (latLng !== null) {
                        for (var i in googleMapsResults[0].properties) {
                            var prop = googleMapsResults[0].properties[i];
                            if (prop.types.includes('country')) {
                                var country = prop.long_name;
                            }
                        }

                        return callback(latLng, country);
                    }
                    console.log("Organization: \'" + label + "\' not found");
                    return;
                });
            }
        });
    }
}

function drawNetwork(network) {
    clearNetwork();

    var nodeSizeCoef = calculateNodeSizeCoef(network);

    var geoNetwork = new Map();

    var processingCounter = 0;

    var lazyCalls = [];

    network.forEach(function (node) {

        geocode(node.label, function (latLng, country) {
            processingCounter++;

            var value = node.value;
            if (geoNetwork.has(country)) {
                value += geoNetwork.get(country);
            }
            geoNetwork.set(country, value);

            var nodeSize = node.value * nodeSizeCoef;

            lazyCalls.push(function () {
                drawNode(node.label, Math.max(nodeSize, 5), latLng);
            });

            if (processingCounter == network.length) {
                drawColorfulCountries(geoNetwork);

                lazyCalls.forEach(function (call) {
                    call();
                });
            }
        });
    });
}

function geocoderResultsToLatLng(results) {
    return (results !== null && results.length !== 0) ?
        new L.LatLng(results[0].center.lat, results[0].center.lng) :
        null;
}

function processingStart() {
    map.spin(true);
    clearNetwork();
    dataTableButton.disable();
}

function processingFinish(network) {
    NETWORK = network;

    dataTableButton.enable();
    drawNetwork(network);
    map.spin(false);
}

function processingError(message) {
    map.spin(false);
    alert("Error: " + message);
}

function makeDataTable() {
    var dataSet = [];

    NETWORK.forEach(function (node) {
        dataSet.push([node.label, node.value]);
    });

    $('#coopTable').DataTable({
        destroy: true,
        dom: 'Bfrtip',
        data: dataSet,
        columns: [{
            title: "Organization"
        }, {
            title: "Cooperation value"
        }],
        buttons: ['copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5']
    });

    dataTableWindow.show();
}
