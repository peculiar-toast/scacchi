package test;
import scacchi.Utile.Colour;
import scacchi.Pedone;

class TestPedone {
    public static boolean testMossa() {
	StringBuffer logs = new StringBuffer();

	System.out.println("********** testMossa Bianco *************");

	Pedone p = new Pedone(Colour.WHITE);
	if (!p.verificaMossa(0, 0,-1, 0, false)) logs.append("muoviBiancoUnoSu fallito\n");
	if ( p.verificaMossa(0, 0, 1, 0, false)) logs.append("muoviBiancoUnoGiu non fallito\n");
	if ( p.verificaMossa(0, 0, 0, 1, false)) logs.append("muoviBiancoUnoDestra non fallito\n");
	if ( p.verificaMossa(0, 0, 0,-1, false)) logs.append("muoviBiancoUnoSinistra non fallito\n");

	p = new Pedone(Colour.WHITE);
	if (!p.verificaMossa(0, 0, -2, 0, false)) logs.append("muoviBiancoDueSu fallito");
	if ( p.verificaMossa(0, 0, -2, 0, false)) logs.append("muoviBiancoDueSuDiNuovo non fallito");

	System.out.println("********** testMossa Nero *************");

	p = new Pedone(Colour.BLACK);
	if ( p.verificaMossa(0,0, -1,0, false)) logs.append("muoviNeroUnoSu non fallito\n");
	if (!p.verificaMossa(0,0,  1,0, false)) logs.append("muoviNeroUnoGiu fallito\n");

	p = new Pedone(Colour.BLACK);
	if (!p.verificaMossa(0, 0, 2, 0, false)) logs.append("muoviNeroDueGiu fallito");
	if ( p.verificaMossa(0, 0, 2, 0, false)) logs.append("muoviNeroDueGiuDiNuovo non fallito");
	
	System.out.print(logs);
	return logs.length() == 0;
    }

    public static void main(String [] args) {
	System.out.println(String.format("test pedone: %B", testMossa()));
    }
}
