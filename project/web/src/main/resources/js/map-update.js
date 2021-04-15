map.on("moveend", function() {
  reloadPois();
});

function reloadPois() {
  if (map.getZoom() < 10) {
    clearPois();
  } else {
    $.getJSON("/pois?bbox=" + map.getBounds().toBBoxString(), function(data) {
      updatePois(data);
    }).fail(function(d, textStatus, error) {
      console.error("getJSON failed, status: " + textStatus + ", error: " + error)
    });
  }
}

function clearPois() {
  for (let [key, value] of markers) {
    value.clearLayers();
  }
}

function updatePois(data) {
  clearPois();
  data.markers.forEach(item => {
    var marker = L.marker([item.lat, item.lon], {
      icon: redMarker
    });
    markers.get('SUCTION_POINT').addLayer(marker);
    marker.bindPopup(item.popup);
  });
}
