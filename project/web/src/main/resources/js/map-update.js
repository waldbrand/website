map.on("moveend", function() {
  reloadPois();
});

function reloadPois() {
  if (map.getZoom() < 11) {
    clearPois();
    $("#overlay").show();
  } else {
    $("#overlay").hide();
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
    var poiMarkers = data.markers[id];
    var iconId = poiMarkers.iconId;
    var list = poiMarkers.list;
    list.forEach(item => {
      var marker = L.marker([item.lat, item.lon], {
        icon: icons.get(iconId)
      });
      markers.get(iconId).addLayer(marker);
      marker.bindPopup(item.popup);
    });
  });
}
