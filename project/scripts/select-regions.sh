#!/bin/bash

DIR=$(dirname $0)
REPO="$DIR/../.."
INPUT="$REPO/extracted-regions"
OUTPUT="$REPO/data/regions"
BOUNDARY="$REPO/project/core/src/main/resources/Brandenburg.smx"

mkdir -p "$OUTPUT"
GeometrySelector --boundary "$BOUNDARY" --output "$OUTPUT/6" --threshold 0.95 "$INPUT/admin/6/"
GeometrySelector --boundary "$BOUNDARY" --output "$OUTPUT/8" --threshold 0.95 "$INPUT/admin/8/"
GeometrySelector --boundary "$BOUNDARY" --output "$OUTPUT/post" --threshold 0.95 "$INPUT/postalcode1/"
