package scacchi;

import java.util.ArrayList;
import java.util.ArrayDeque;

import scacchi.Utile.Colour;
import scacchi.Utile.ChessPiece;
import scacchi.PezzoException;

public class Griglia {
	private static final int GRID_LEN = 8;
	private Casella[][] scacchiera;
	private boolean scaccoReBianco, scaccoReNero;
	private boolean inPartita;
	private boolean turnoBianco;
	private ArrayList<String> logs; // contiene tutti i messaggi
	private ArrayDeque<String> msg; // messaggi vengono mostrati, poi inseriti in logs

	public Griglia() {
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
		addPezzo(new Re(Colour.WHITE), 'E', 1);
		addPezzo(new Regina(Colour.WHITE), 'D', 1);
		addPezzo(new Alfiere(Colour.WHITE), 'F', 1);
		addPezzo(new Cavallo(Colour.WHITE), 'G', 1);
		addPezzo(new Torre(Colour.WHITE), 'H', 1);

		addPezzo(new Torre(Colour.BLACK), 'A', 8);
		addPezzo(new Cavallo(Colour.BLACK), 'B', 8);
		addPezzo(new Alfiere(Colour.BLACK), 'C', 8);
		addPezzo(new Re(Colour.BLACK), 'E', 8);
		addPezzo(new Regina(Colour.BLACK), 'D', 8);
		addPezzo(new Alfiere(Colour.BLACK), 'F', 8);
		addPezzo(new Cavallo(Colour.BLACK), 'G', 8);
		addPezzo(new Torre(Colour.BLACK), 'H', 8);
		try {
			aggiornaScacco();
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
		}
	}

	public int toCoord(char col) {
		return col - 'A';
	}

	public int toCoord(int row) {
		return 8 - row;
	}

	public void addPezzo(Pezzo pezzo, char col, int row) {
		if ((col >= 'A' && col <= 'H') &&
				(row >= 1 && row <= 8)) {
			int nCol = toCoord(col);
			int nRow = toCoord(row);

			addPezzo(pezzo, nRow, nCol);
		}
	}

	public void addPezzo(Pezzo pezzo, int row, int col) {
		if (isFloor(row, col)) {
			scacchiera[row][col].setPezzo(pezzo);
			msg.offer(String.format("AddPezzo T [%d, %d] %c", row, col, pezzo.getLettera()));
		}
		msg.offer(String.format("AddPezzo F [%d, %d] %c", row, col, pezzo.getLettera()));
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
		System.out.print(sb);
	}

	private boolean isInsideBounds(int row, int col) {
		return (row >= 0 && row <= GRID_LEN) &&
				(col >= 0 && col <= GRID_LEN);
	}

	private void spostaPezzo(Casella partenza, Casella fine) {
		fine.setPezzo(partenza.getPezzo());
		partenza.setPezzo(null);
	}

	/**
	 * trova i re
	 * guarda se ogni re è sotto scacco
	 */
	public boolean aggiornaScacco() throws PezzoException {
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
			throw new PezzoException("devono esserci un re bianco e uno nero");

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

	public boolean mossaNotazione(String c) throws MossaPezzoException {
		c = c.toUpperCase();
		int sCol = c.charAt(0) - 'A';
		int sRow = '8' - c.charAt(1);
		int eCol = c.charAt(2) - 'A';
		int eRow = '8' - c.charAt(3);

		return mossa(sRow, sCol, eRow, eCol);
	}

	/**
	 * se mossa segue regole, sposta pezzo
	 * 
	 * @return true se pezzo esiste e mossa avvenuta con successo
	 */
	public boolean mossa(int startRow, int startCol, int endRow, int endCol) throws MossaPezzoException {
		if (!isInsideBounds(startRow, startCol)) {
			msg.offer(String.format("[%d, %d] inizio mossa fuori da scacchiera", startRow, startCol));
			throw new MossaPezzoException("inizio mossa fuori da scacchiera", startRow, startCol);
		}
		if (!isInsideBounds(endRow, endCol))
			throw new MossaPezzoException("destinazione mossa fuori da scacchiera", endRow, endCol);
		if (isFloor(startRow, startCol))
			throw new MossaPezzoException("nessun pezzo da muovere", startRow, startCol);

		Griglia tmpGriglia = new Griglia(this); // creo una copia temporanea
		Casella casellaIniziale = tmpGriglia.scacchiera[startRow][startCol];
		Casella casellaDestinazione = tmpGriglia.scacchiera[endRow][endCol];

		// se casellaDestinazione ha pezzo nemico, allora cattura
		// serve per pedone
		boolean cattura = false;
		if (casellaDestinazione.getPezzo() != null) {
			if (casellaDestinazione.getPezzo().getColour() != casellaIniziale.getPezzo().getColour()) {
				cattura = true;
			} else {
				throw new MossaPezzoException("stai provando a catturare un pezzo dello stesso colore!");
			}
		}

		// spostamento rientra nelle regole del pezzo?
		if (casellaIniziale.getPezzo().verificaMossa(startRow, startCol, endRow, endCol, cattura)) {
			spostaPezzo(casellaIniziale, casellaDestinazione);
		}

		try {
			tmpGriglia.aggiornaScacco(); // dopo aver fatto la mossa, controllo che risultato avrebbe
		} catch (PezzoException pe) {
			System.out.println(pe.getMessage());
		}

		if (!(scaccoReBianco || scaccoReNero) ||
				(scaccoReBianco && !turnoBianco ||
						scaccoReNero && turnoBianco)) {
			spostaPezzo(scacchiera[startRow][startCol], scacchiera[endRow][endCol]);
		}
		return true;
	}

	/**
	 * @return true se non c'e un pezzo nella casella corrispondente.
	 */
	private boolean isFloor(int row, int col) {
		return scacchiera[row][col].getPezzo() == null;
	}
}
