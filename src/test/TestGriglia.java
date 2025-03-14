package test;

import scacchi.Griglia;
import scacchi.MossaPezzoException;
import scacchi.Pedone;
import scacchi.Utile.Colour;;

class TestGriglia {
    
    public static boolean testCatturaCorretta1() {
	System.out.println("*** testCatturaCorretta1 ***");
	Griglia g = new Griglia();
	g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
	g.addPezzo(new Pedone(Colour.BLACK), 3, 3);
	try {
	    g.mossaNotazione("E4D5");
	} catch (MossaPezzoException mpe) {
	    System.out.println(mpe.getMessage());
	    return false;
	}
	return true;
    }
    
    public static boolean testCatturaCorretta2() {
	System.out.println("*** testCatturaCorretta2 ***");
	Griglia g = new Griglia();
	g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
	g.addPezzo(new Pedone(Colour.BLACK), 3, 3);
	try {
	    g.mossaNotazione("D5E4");
	} catch (MossaPezzoException mpe) {
	    System.out.println(mpe.getMessage());
	    return false;
	}
	return true;
    }

    public static boolean testMossaFallita1() {
	System.out.println("*** testMossaFallita1 ***");
	Griglia g = new Griglia();
	g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
	try {
	    g.mossaNotazione("D5E9");
	} catch (MossaPezzoException mpe) {
	    return true;
	}
	return false;
    }

    
    public static void main(String[] args) {
	System.out.println(testCatturaCorretta1());
	System.out.println(testCatturaCorretta2());
	
	System.out.println(testMossaFallita1());
    }
}
