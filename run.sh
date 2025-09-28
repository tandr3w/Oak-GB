#!/bin/bash
set -e

SRC_DIR="src"   # .java
OUT_DIR="out"   # .class
LIB_DIR="lib"
MAIN_CLASS="Main"

mkdir -p "$OUT_DIR"

CP="."
if [ -d "$LIB_DIR" ]; then
    for jar in "$LIB_DIR"/*.jar; do
        CP="$CP:$jar"
    done
fi

javac -cp "$CP" -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")
java -cp "$CP:$OUT_DIR" "$MAIN_CLASS"
