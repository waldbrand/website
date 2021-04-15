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
  Object.keys(data.markers).forEach(function(id) {
    var list = data.markers[id];
    list.forEach(item => {
      var marker = L.marker([item.lat, item.lon], {
        icon: icons.get(id)
      });
      markers.get(id).addLayer(marker);
      marker.bindPopup(item.popup);
    });
  });
}
