package test;
import scacchi.Utile.*;
import scacchi.Regina;

public class TestRegina {
    public static boolean testMossaNPassi(int passi) {
	System.out.printf("   *** testMossaNPassi: %d\n", passi);
	Regina r = new Regina(Colour.WHITE);
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

    public static boolean testMossa() {
	return
	    !testMossaNPassi(0) && // non può stare fermo
	    testMossaNPassi(1) &&
	    testMossaNPassi(2) &&
	    testMossaNPassi(3) &&
	    testMossaNPassi(4);
    }

    public static void main(String [] args) {
	System.out.println("****** test regina ******");
	System.out.printf("testMossa: %B\n", testMossa());
    }
}
