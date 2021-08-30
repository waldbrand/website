#!/bin/bash

DIR=$(dirname $0)
REPO="$DIR/../.."
ASSETS="$REPO/../app/waldbrand-app/app/src/main/assets/"

cp "$REPO/Brandenburg-waldbrand.xmap" "$ASSETS/waldbrand.xmap.jet"
