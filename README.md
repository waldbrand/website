# Waldbrand website and utilities

## Preparing OSM data

The tools in this repository work with OSM data stored at
`~/github/waldbrand/osm-data`.

To obtain basic OSM data, navigate to the `project` directory,
and run the following scripts in this order:

    ./scripts/download-osm-data
    ./scripts/extract-region-data
    ./scripts/extract-osm-data

Doing that will:

* download data from Geofabrik's download
  server to `~/github/waldbrand/osm-data`,
* extract Brandenburg from the Berlin-Brandenburg file,
* extract relevant OSM data from the Brandenburg file.

## Data packaging

Create assets for Android app and copy them to the app repository:

    ./scripts/create-database.sh
    ./scripts/create-mapfile.sh
    ./scripts/create-mapfile-waldbrand
    ./scripts/copy-assets.sh

## Website deployment

To upload a new version of the website, run this:

    ./deploy.to.production.sh
