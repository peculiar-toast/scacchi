@echo off
echo test pedone

javac -d out/ src/scacchi/Pedone.java src/scacchi/Pezzo.java src/scacchi/Utile.java src/scacchi/PezzoException.java src/test/TestPedone.java

java -cp out/ test.TestPedone
