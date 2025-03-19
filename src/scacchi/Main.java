package scacchi;

import java.util.Scanner;

class Main {
	static Scanner in = new Scanner(System.in);
	static Griglia g = new Griglia();

	public static void main(String[] args) {
		g.statoIniziale();
		g.print();
		while (true) {
			try {
				String c = in.next();
				g.mossaNotazione(c);
			} catch (PezzoException pe) {
				System.out.println(pe.getMessage());
			}
			g.print();
		}
	}
}
