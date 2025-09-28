#!/bin/bash
set -e

SRC_DIR="src"
OUT_DIR="bin"
LIB_DIR="lib"
MAIN_CLASS="Main"
JAR_NAME="OakGB.jar"
MANIFEST="manifest.txt"

mkdir -p "$OUT_DIR"

CP="."
if [ -d "$LIB_DIR" ]; then
    for jar in "$LIB_DIR"/*.jar; do
        [ -f "$jar" ] || continue
        CP="$CP:$jar"
    done
fi

javac -cp "$CP" -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")

if [ -d "$LIB_DIR" ]; then
    for jar in "$LIB_DIR"/*.jar; do
        [ -f "$jar" ] || continue
        (cd "$OUT_DIR" && jar xf "../$jar")
    done
fi

echo -e "Manifest-Version: 1.0\nMain-Class: $MAIN_CLASS\n" > "$MANIFEST"

jar cfm "$JAR_NAME" "$MANIFEST" -C "$OUT_DIR" .