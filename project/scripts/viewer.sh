#!/bin/bash

DIR=$(dirname $0)
REPO="$DIR/../.."
DB="$REPO/Brandenburg.sqlite"
MAP="$REPO/Brandenburg.xmap"

~/github/topobyte/mapocado/scripts/mapocado city-viewer \
    --database "$DB" --mapfile "$MAP"
