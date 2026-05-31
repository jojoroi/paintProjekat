@echo off
mkdir out 2>nul
javac -d out -sourcepath src src\shapes\*.java src\*.java
if errorlevel 1 ( echo Greška pri kompilaciji. & pause & exit /b 1 )
java -cp out Main
