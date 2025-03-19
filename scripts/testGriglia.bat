@echo off
echo test pedone

javac -d out/ src/test/TestGriglia.java src/scacchi/*.java

java -cp out/ test.TestGriglia
