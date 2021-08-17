#!/bin/bash

DIR=$(dirname $0)
REPO="$DIR/../.."
TBO="$HOME/github/waldbrand/osm-data/Brandenburg.tbo"
BOUNDARY="$REPO/project/core/src/main/resources/Brandenburg.smx"
TMP="$REPO/tmp"
CONFIG="$REPO/project/pois.xml"
REGIONS="$REPO/data/regions"

~/github/topobyte/nomioc/setup-cli/scripts/NomiocDatabaseCreatorCustom \
    --input "$TBO" --boundary "$BOUNDARY" --regions "$REGIONS" \
    --node-db "$TMP/nodes" --way-db "$TMP/ways" \
    --config "$CONFIG" --output "$REPO/Brandenburg.sqlite"
