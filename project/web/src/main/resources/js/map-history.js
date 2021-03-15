function update_history() {
  var hash = window.location.hash;
  var uri = window.location.href.split("#")[0];
  var center = map.getCenter();
  var zoom = map.getZoom();
  var loc = uri + "#" + center.lat + ";" + center.lng + ";" + zoom;
  history.replaceState(null, "", loc);
}

function set_location_from_href() {
  var parts = window.location.href.split("#");
  if (parts.length < 2) {
    return;
  }
  var loc = parts[1];
  var locparts = loc.split(";");
  if (locparts.length < 3) {
    return;
  }
  var lat = locparts[0];
  var lon = locparts[1];
  var zoom = locparts[2];
  map.setView([lat, lon], zoom);
}

map.on('zoomend', function() {
  update_history();
});

map.on('dragend', function() {
  update_history();
});

$(function() {
  set_location_from_href();
});
