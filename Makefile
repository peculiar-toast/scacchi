src = src/scacchi/
out = src/scacchi/
javas = $(wildcard $(src)*.java)

all:
	javac -d out $(javas)

run:
	java -cp out scacchi.Main

clean:
	rm -r out/*

srcTestPedone = src/test/TestPedone.java src/scacchi/Pedone.java src/scacchi/Pezzo.java src/scacchi/Utile.java

testPedone:
	javac -d out $(srcTestPedone)
	java -cp out test.TestPedone

srcTestGriglia = src/test/TestGriglia.java src/scacchi/Griglia.java src/scacchi/Casella.java src/scacchi/Pezzo.java src/scacchi/Pedone.java src/scacchi/Utile.java src/scacchi/PezzoException.java src/scacchi/MossaPezzoException.java

testGriglia:
	javac -d out $(srcTestGriglia)
	java -cp out test.TestGriglia
