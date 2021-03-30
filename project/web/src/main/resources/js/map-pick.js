map.on('move', function() {
  marker.setLatLng(map.getCenter());
});
map.on('dragend', function(e) {
  marker.setLatLng(map.getCenter());
});
