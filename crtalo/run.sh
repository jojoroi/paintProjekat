#!/bin/bash
mkdir -p out
javac -d out -sourcepath src src/shapes/*.java src/*.java || { echo "Kompilacija neuspešna."; exit 1; }
java -cp out Main
