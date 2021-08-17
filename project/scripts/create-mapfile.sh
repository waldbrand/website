#!/bin/bash

DIR=$(dirname $0)
REPO="$DIR/../.."
TBO="$HOME/github/waldbrand/osm-data/Brandenburg.tbo"
BOUNDARY="$REPO/project/core/src/main/resources/Brandenburg.smx"
TMP="$REPO/tmp"
RULES="$HOME/github/topobyte/mapocado/config/citymaps/rendertheme-v2/rules/default"

~/github/topobyte/mapocado/scripts/mapocado mapfile create \
    --input "$TBO" --boundary "$BOUNDARY" \
    --node-db "$TMP/nodes" --way-db "$TMP/ways" \
    --rules "$RULES" --logs logs --output "$REPO/Brandenburg.xmap"
