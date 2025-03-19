package scacchi;

import java.util.Scanner;

class Main {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Griglia g = new Griglia(true);
		while (true) {
			g.print();
			System.out.print("Inserisci mossa: ");
			String c = "";
			if (in.hasNext()) {
				c = in.next();
			} else {
				System.out.println("no input");
				break;
			}
			g.mossaNotazione(c);
		}
	}
}
