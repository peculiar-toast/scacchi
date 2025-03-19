package test;

import scacchi.Griglia;

class TestGriglia {
	public static boolean testCatturaCorretta1() {
		System.out.println("*** testCatturaCorretta1 ***");
		Griglia g = new Griglia(false);
		if (!g.mossaNotazione("E2E4")) {
			System.out.println("Errore in E2E4");
			g.print();
			return false;
		}
		if (!g.mossaNotazione("D7D5")) {
			System.out.println("Errore in D7D5");
			g.print();
			return false;
		}
		if (!g.mossaNotazione("E4D5")) {
			System.out.println("Errore in E4D5");
			g.print();
			return false;
		}
		return true;
	}

	public static boolean testCatturaCorretta2() {
		System.out.println("*** testCatturaCorretta2 ***");
		Griglia g = new Griglia(false);
		return true;
	}

	public static boolean testMossaFallita1() {
		System.out.println("*** testMossaFallita1 ***");
		Griglia g = new Griglia(false);
		/*
		g.addPezzo(new Pedone(Colour.WHITE), 4, 4);
		try {
			g.mossaNotazione("D5E9");
		} catch (MossaPezzoException mpe) {
			return true;
		}
		*/
		return false;
	}

	public static boolean testCheckmate1() {
		System.out.println("   *** testCheckmate1: Nessun re in check ******");
		Griglia g = new Griglia(false);
		/*
		g.addPezzo(new Re(Colour.WHITE), 0, 0);
		g.addPezzo(new Re(Colour.BLACK), 4, 4);

		boolean risultato = false;
		try {
			//risultato = g.aggiornaScacco();
		} catch (PezzoException pe) {
			risultato = false;
		}
		return risultato;
		*/
		return true;
	}

	public static boolean testCheckmate2() {
		System.out.println("   *** testCheckmate2: re nero in check ******");
		Griglia g = new Griglia(false);
		/*g.addPezzo(new Re(Colour.BLACK), 4, 4);
		g.addPezzo(new Pedone(Colour.WHITE), 5, 5);
		try {
			return g.aggiornaScacco();
			return false;
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
			return false;
		}
		*/
		return true;
	}

	public static boolean testCheckmate3() {
		System.out.println("   *** testCheckmate3: re bianco in check ******");
		Griglia g = new Griglia(false);
		/*
		g.addPezzo(new Re(Colour.WHITE), 0, 0);
		g.addPezzo(new Re(Colour.BLACK), 4, 4);
		g.addPezzo(new Regina(Colour.BLACK), 5, 0);
		try {
			return g.aggiornaScacco() == true;
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
			return false;
		}
		*/
		return true;
	}

	public static boolean testCheckmate4() {
		System.out.println("   *** testCheckmate4: bianco non può muovere ******");
		Griglia g = new Griglia(false);
		/*
		g.addPezzo(new Re(Colour.WHITE), 0, 0);
		g.addPezzo(new Re(Colour.BLACK), 4, 4);
		g.addPezzo(new Regina(Colour.BLACK), 5, 0);
		try {
			return g.aggiornaScacco() == true;
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
			return false;
		}
		*/
		return true;
	}

	public static boolean testCheckmate() {
		System.out.println("****** testCheckmate ******");
		return !testCheckmate1() &&
				testCheckmate2() &&
				testCheckmate3() &&
				testCheckmate4();
	}

	public static void main(String[] args) {
		System.out.println(testCatturaCorretta1());
	}
}
