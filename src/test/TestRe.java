package test;
import scacchi.Utile.*;
import scacchi.Re;

public class TestRe {
    public static boolean testMossaNPassi(int passi) {
	System.out.printf("   *** testMossaNPassi: %d\n", passi);
	Re r = new Re(Colour.WHITE);
	return
	    r.verificaMossa(-passi,-passi) &&
	    r.verificaMossa(-passi, 0) &&
	    r.verificaMossa(-passi, passi) &&
	    r.verificaMossa( 0,-passi) &&
	    r.verificaMossa( 0, passi) &&
	    r.verificaMossa( passi,-passi) &&
	    r.verificaMossa( passi, 0) &&
	    r.verificaMossa( passi, passi);
    }

    public static boolean testMossaArrocco(boolean latoRe) {
	System.out.printf("   *** testMossaArrocco: %B\n", latoRe);
	Re r = new Re(Colour.WHITE);
	int passo = (latoRe)? 2 : -2;
	return
	    r.verificaMossa(0, passo) &&
	    !r.verificaMossa(0, passo); // deve funzionare solo la prima volta
    }
    
    public static boolean testMossa() {
	return
	    !testMossaNPassi(0) && // non può stare fermo
	    testMossaNPassi(1) &&
	    !testMossaNPassi(2) &&
	    testMossaArrocco(true) &&
	    testMossaArrocco(false);
    }

    public static void main(String [] args) {
	System.out.println("****** test re ******");
	System.out.printf("testMossa: %B\n", testMossa());
    }
}
