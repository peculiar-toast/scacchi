@echo off
echo test re

javac -d out/ src/scacchi/Re.java src/scacchi/Pezzo.java src/scacchi/Utile.java src/scacchi/PezzoException.java src/test/TestRe.java

java -cp out/ test.TestRe



