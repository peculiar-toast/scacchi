package scacchi;

import java.util.ArrayList;
import java.util.ArrayDeque;

import scacchi.Utile.ChessPiece;
import scacchi.Utile.Colour;
import scacchi.Utile.ChessPiece;

public class Griglia {
	private static final int GRID_LEN = 8;
	private Casella[][] scacchiera;
	private boolean scaccoReBianco, scaccoReNero;
	private boolean inPartita;
	private boolean turnoBianco;
	private ArrayList<String> logs; // contiene tutti i messaggi
	private ArrayDeque<String> msg; // messaggi vengono mostrati, poi inseriti in logs

	public Griglia(boolean caricaStato) {
		this.inPartita = true;
		this.turnoBianco = true;
		this.scaccoReBianco = this.scaccoReNero = false;

		scacchiera = new Casella[GRID_LEN][GRID_LEN];
		logs = new ArrayList<String>();
		msg = new ArrayDeque<String>();

		for (int riga = 0; riga < GRID_LEN; riga++) {
			for (int colonna = 0; colonna < GRID_LEN; colonna++) {
				Colour coloreCasella = (riga + colonna) % 2 == 0 ? Colour.WHITE : Colour.BLACK;
				scacchiera[riga][colonna] = new Casella(coloreCasella, null);
			}
		}

		if (caricaStato) {
			statoIniziale();
		}
	}

	@Override
	public Griglia clone() {
		Griglia clonedGriglia = new Griglia(false);
		clonedGriglia.scaccoReBianco = this.scaccoReBianco;
		clonedGriglia.scaccoReNero = this.scaccoReNero;
		clonedGriglia.inPartita = this.inPartita;
		clonedGriglia.turnoBianco = this.turnoBianco;
		clonedGriglia.logs = new ArrayList<>(this.logs);
		clonedGriglia.msg = new ArrayDeque<>(this.msg);

		for (int riga = 0; riga < GRID_LEN; riga++) {
			for (int colonna = 0; colonna < GRID_LEN; colonna++) {
				clonedGriglia.scacchiera[riga][colonna] = new Casella(this.scacchiera[riga][colonna]);
			}
		}

		return clonedGriglia;
	}

	public Griglia(Griglia g) {
		this.scaccoReBianco = g.scaccoReBianco;
		this.scaccoReNero = g.scaccoReNero;
		this.inPartita = g.inPartita;
		this.turnoBianco = g.turnoBianco;
		this.logs = new ArrayList<String>();
		this.msg = new ArrayDeque<String>();

		this.scacchiera = new Casella[GRID_LEN][GRID_LEN];
		for (int riga = 0; riga < GRID_LEN; riga++) {
			for (int colonna = 0; colonna < GRID_LEN; colonna++) {
				this.scacchiera[riga][colonna] = new Casella(g.scacchiera[riga][colonna]);
			}
		}
	}

	/**
	 * TODO mettere nel costruttore
	 * uso per piazzare pezzi in uno stato iniziale
	 */
	public void statoIniziale() {
		for (int colonna = 0; colonna < GRID_LEN; colonna++) {
			scacchiera[1][colonna].setPezzo(new Pedone(Colour.BLACK));
			scacchiera[GRID_LEN - 2][colonna].setPezzo(new Pedone(Colour.WHITE));
		}
		addPezzo(new Torre(Colour.WHITE), 'A', 1);
		addPezzo(new Cavallo(Colour.WHITE), 'B', 1);
		addPezzo(new Alfiere(Colour.WHITE), 'C', 1);
		addPezzo(new Regina(Colour.WHITE), 'D', 1);
		addPezzo(new Re(Colour.WHITE), 'E', 1);
		addPezzo(new Alfiere(Colour.WHITE), 'F', 1);
		addPezzo(new Cavallo(Colour.WHITE), 'G', 1);
		addPezzo(new Torre(Colour.WHITE), 'H', 1);

		addPezzo(new Torre(Colour.BLACK), 'A', 8);
		addPezzo(new Cavallo(Colour.BLACK), 'B', 8);
		addPezzo(new Alfiere(Colour.BLACK), 'C', 8);
		addPezzo(new Regina(Colour.BLACK), 'D', 8);
		addPezzo(new Re(Colour.BLACK), 'E', 8);
		addPezzo(new Alfiere(Colour.BLACK), 'F', 8);
		addPezzo(new Cavallo(Colour.BLACK), 'G', 8);
		addPezzo(new Torre(Colour.BLACK), 'H', 8);
		try {
			aggiornaScacco();
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
		}
	}

	public void print() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < GRID_LEN; i++) {
			sb.append((char) ('0' + GRID_LEN - i) + "|");
			for (int j = 0; j < GRID_LEN; j++) {
				sb.append(scacchiera[i][j].getLettera() + " ");
			}
			sb.append('\n');
		}

		sb.append("  ");
		for (int i = 0; i < GRID_LEN; i++) {
			sb.append("- ");
		}
		sb.append("\n  ");
		for (int i = 0; i < GRID_LEN; i++) {
			sb.append((char) ('A' + i) + " ");
		}
		sb.append("\n");

		while (!msg.isEmpty()) {
			sb.append(msg.peek());
			sb.append("\n");
			logs.add(msg.poll());
		}
		System.out.print(sb);
	}

	public boolean mossaNotazione(String c) {
		c = c.toUpperCase();
		int sCol = c.charAt(0) - 'A';
		int sRow = '8' - c.charAt(1);
		int eCol = c.charAt(2) - 'A';
		int eRow = '8' - c.charAt(3);

		return mossa(sRow, sCol, eRow, eCol);
	}

	/**
	 * se mossa segue regole, sposta pezzo
	 * altrimenti non fare nulla
	 * 
	 * @return true se pezzo esiste e mossa avvenuta con successo
	 */
	public boolean mossa(int startRow, int startCol, int endRow, int endCol) {
		if (!segueRegoleMossa(startRow, startCol, endRow, endCol)) {
			return false;
		}
		Griglia tmpGriglia = this.clone(); // copia della griglia per controllare scacco
		Casella casellaIniziale = tmpGriglia.scacchiera[startRow][startCol];
		Casella casellaDestinazione = tmpGriglia.scacchiera[endRow][endCol];

		// se casellaDestinazione ha pezzo nemico, allora cattura
		boolean cattura = casellaDestinazione.getPezzo() != null;

		boolean mossaVerificata = true;
		// se pezzo può fare mossa
		if (casellaIniziale.getPezzo().verificaMossa(startRow, startCol, endRow, endCol, cattura)) {
			// se pezzo non può saltare, controlla che non ci siano pezzi tra inizio e fine
			if (!casellaIniziale.getPezzo().puoSaltare()) {
				mossaVerificata = isPercorsoLibero(startRow, startCol, endRow, endCol);
			} else {
				mossaVerificata = true;
			}
		}

		if (mossaVerificata) {
			spostaPezzo(casellaIniziale, casellaDestinazione); // sposta pezzo nella scacchiera temporanea
		}

		try {
			tmpGriglia.aggiornaScacco(); // dopo aver fatto la mossa, controllo che risultato avrebbe
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
		}

		if (!(scaccoReBianco || scaccoReNero) ||
				(scaccoReBianco && !turnoBianco || scaccoReNero && turnoBianco)) {
			spostaPezzo(scacchiera[startRow][startCol], scacchiera[endRow][endCol]);
			turnoBianco = !turnoBianco;
		}

		if (endRow == 0 || endRow == GRID_LEN - 1) {
			if (scacchiera[endRow][endCol].getPezzo().puoPromuovere()) {
				// TODO aggiungere scelta per promuovere
				scacchiera[endRow][endCol].setPezzo(new Regina(scacchiera[endRow][endCol].getPezzo().getColour()));
			}
		}
		return true;
	}

	private boolean segueRegoleMossa(int startRow, int startCol, int endRow, int endCol) {
		if (!isInsideBounds(startRow, startCol)) {
			msg.offer(String.format("[%d, %d]\tinizio mossa fuori da scacchiera", startRow, startCol));
			return false;
		}
		if (!isInsideBounds(endRow, endCol)) {
			msg.offer(String.format("[%d, %d]\tdestinazione mossa fuori da scacchiera", endRow, endCol));
			return false;
		}
		if (isFloor(startRow, startCol)) {
			msg.offer(String.format("[%c%d]\tnessun pezzo da muovere", toColonna(startCol), toRiga(startRow)));
			return false;
		}
		if (scacchiera[startRow][startCol].getPezzo() != null) {
			if (scacchiera[startRow][startCol].getPezzo().getColour() == Colour.WHITE && !turnoBianco) {
				msg.offer("non è il turno del giocatore bianco");
				return false;
			}
			if (scacchiera[startRow][startCol].getPezzo().getColour() == Colour.BLACK && turnoBianco) {
				msg.offer("non è il turno del giocatore nero");
				return false;
			}

			if (scacchiera[endRow][endCol].getPezzo() != null) {
				if (scacchiera[startRow][startCol].getPezzo().getColour() == scacchiera[endRow][endCol].getPezzo()
						.getColour()) {
					msg.offer("non puoi catturare un pezzo dello stesso colore");
					return false;
				}
			}
		}
		return true;
	}

	private boolean isPercorsoLibero(int startRow, int startCol, int endRow, int endCol) {
		int deltaRow = endRow - startRow;
		int deltaCol = endCol - startCol;
		int stepRow = deltaRow == 0 ? 0 : deltaRow / Math.abs(deltaRow);
		int stepCol = deltaCol == 0 ? 0 : deltaCol / Math.abs(deltaCol);
		int row = startRow + stepRow;
		int col = startCol + stepCol;
		while (row != endRow || col != endCol) {
			if (!isFloor(row, col)) {
				msg.offer(String.format("[%c%d]\tpezzo trovato tra inizio e fine", toColonna(col), toRiga(row)));
				return false;
			}
			row += stepRow;
			col += stepCol;
		}
		return true;
	}

	/**
	 * trova i re
	 * guarda se ogni re è sotto scacco
	 */
	private boolean aggiornaScacco() throws PezzoException {
		int biancoRow, biancoCol;
		int neroRow, neroCol;
		biancoRow = biancoCol = neroRow = neroCol = -1;
		for (int i = 0; i < GRID_LEN; i++) {
			for (int j = 0; j < GRID_LEN; j++) {
				Pezzo pezzo = scacchiera[i][j].getPezzo();
				if (pezzo != null && pezzo.getTipo() == ChessPiece.KING) {
					if (pezzo.getColour() == Colour.WHITE) {
						biancoRow = i;
						biancoCol = j;
					} else {
						neroRow = i;
						neroCol = j;
					}
				}
			}
		}

		if (biancoRow == -1 || neroRow == -1)
			throw new PezzoException("devono esistere un re bianco e un re nero");

		for (int i = 0; i < GRID_LEN; i++) {
			for (int j = 0; j < GRID_LEN; j++) {
				Pezzo p = scacchiera[i][j].getPezzo();
				if (p != null) {
					if (p.getColour() == Colour.WHITE) {
						if (p.verificaMossa(i, j, neroRow, neroCol, true)) {
							this.scaccoReNero = true;
						}
					} else {
						if (p.verificaMossa(i, j, biancoRow, biancoCol, true)) {
							this.scaccoReBianco = true;
						}
					}
				}
			}
		}
		return this.scaccoReNero || this.scaccoReBianco;
	}

	private void addPezzo(Pezzo pezzo, char col, int row) {
		if ((col >= 'A' && col <= 'H') &&
				(row >= 1 && row <= 8)) {
			int nCol = toCoord(col);
			int nRow = toCoord(row);

			addPezzo(pezzo, nRow, nCol);
		}
	}

	private void addPezzo(Pezzo pezzo, int row, int col) {
		if (isFloor(row, col)) {
			scacchiera[row][col].setPezzo(pezzo);
		}
	}

	private boolean isInsideBounds(int row, int col) {
		return (row >= 0 && row <= GRID_LEN) &&
				(col >= 0 && col <= GRID_LEN);
	}

	private void spostaPezzo(Casella partenza, Casella fine) {
		fine.setPezzo(partenza.getPezzo());
		partenza.setPezzo(null);
	}

	private static int toCoord(char col) {
		return col - 'A';
	}

	private static int toCoord(int row) {
		return GRID_LEN - row;
	}

	public static char toColonna(int col) {
		return (char) ('A' + col);
	}

	public static int toRiga(int row) {
		return GRID_LEN + row;
	}

	/**
	 * @return true se non c'e un pezzo nella casella corrispondente.
	 */
	private boolean isFloor(int row, int col) {
		return scacchiera[row][col].getPezzo() == null;
	}
}
