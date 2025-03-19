package scacchi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

class Main {
	

	public static void caricaPartita(String nomeFile, Griglia g) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				g.mossaNotazione(line);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Errore apertura file: " + e.getMessage());
			return;
		}
	}

	public static void main(String[] args) {
		Griglia g = new Griglia(true);
		caricaPartita("partita.txt", g);
		Scanner in = new Scanner(System.in);
		while (true) {
			g.print();
			System.out.println("Inserisci mossa: ");
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
