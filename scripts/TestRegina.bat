@echo off
echo test regina

javac -d out/ src/scacchi/Regina.java src/scacchi/Pezzo.java src/scacchi/Utile.java src/scacchi/PezzoException.java src/test/TestRegina.java

java -cp out/ test.TestRegina



