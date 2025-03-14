package scacchi;

import java.util.Scanner;

class Main {
    static Scanner in = new Scanner(System.in);
    static Griglia g = new Griglia();
    
    public static String getMossa()
    {
	System.out.print("inserisci mossa [a1A2]: ");
	String c = in.next().toUpperCase();
	int sCol = c.charAt(0) - 'A';
	int sRow = '8' - c.charAt(1);	
	int eCol = c.charAt(2) - 'A';
	int eRow = '8' - c.charAt(3);
	try {
	    g.mossa(sRow, sCol, eRow, eCol);
	} catch (PezzoException pe) {
	    System.out.println(pe.getMessage());
	}
	return "string";
    }
    
    public static void main (String[] args) {
	g.statoIniziale();
	g.print();
	try {	
	    g.mossaNotazione("A2a4");
	} catch (PezzoException pe) {
	    System.out.println(pe.getMessage());
	}
	g.print();
    }
}
