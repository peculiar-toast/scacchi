package test;

import scacchi.Griglia;
import scacchi.Pedone;
import scacchi.Utile.Colour;;

class TestGriglia extends Griglia {
    
    // deve funzionare. pedone bianco può catturare
    public static boolean testCatturaBianco() {
	System.out.println("*** testCatturaBianco ***");
	Griglia g = new Griglia();
	g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
	g.addPezzo(new Pedone(Colour.BLACK), 3, 3);
	return g.mossaNotazione("E4D5");
    }

    // deve funzionare. pedone nero può catturare
    public static boolean testCatturaNero() {
	System.out.println("*** testCatturaNero ***");
	Griglia g = new Griglia();
	g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
	g.addPezzo(new Pedone(Colour.BLACK), 3, 3);
	return g.mossaNotazione("D5E4");
    }

    // deve fallire. pezzo non può andare fuori dalla scacchiera
    public static boolean testMossaFuoriScacchiera() {
	System.out.println("*** testMossaFuoriScacchiera ***");
	Griglia g = new Griglia();
	g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
	return !g.mossaNotazione("D5E9");
    }

    // deve fallire. non posso muovere un pezzo che non esiste
    public static boolean testMossaPezzoNull() {
	System.out.println("*** testMossaPezzoNull ***");
	Griglia g = new Griglia();
	return !g.mossaNotazione("D5E9");
    }

    
    public static void main(String[] args) {
	System.out.println(testCatturaBianco());
	System.out.println(testCatturaNero());
	
	System.out.println(testMossaFuoriScacchiera());
	System.out.println(testMossaPezzoNull());
    }
}
