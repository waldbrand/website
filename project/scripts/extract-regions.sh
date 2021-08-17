#!/bin/bash

DIR=$(dirname $0)
REPO="$DIR/../.."
TBO="$HOME/github/waldbrand/osm-data/Brandenburg-osm4j.tbo"
TMP="$REPO/tmp-osm4j"

mkdir -p "$TMP"

OsmExtraExtractDiverseRegions --input-format tbo --input "$TBO" \
	--nodes-data "$TMP/nodes.dat" --nodes-index "$TMP/nodes.idx" \
	--ways-data "$TMP/ways.dat" --ways-index "$TMP/ways.idx" \
	--output "$REPO/extracted-regions"
