package scacchi;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import scacchi.Utile.ChessPiece;
import scacchi.Utile.Colour;

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
			// non fare nulla
		}
	}

	public void print() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); // """clear screen"""

		// stampa eventi della mossa precedente
		while (!msg.isEmpty()) {
			sb.append(msg.peek());
			sb.append("\n");
			logs.add(msg.poll());
		}

		sb.append("turno del giocatore: ");
		if (turnoBianco) {
			sb.append("BIANCO\n");
		} else {
			sb.append("NERO\n");
		}

		// notifica se re sotto scacco
		if (scaccoReBianco) {
			sb.append("re BIANCO sotto scacco\n");
		}
		if (scaccoReNero) {
			sb.append("re NERO sotto scacco\n");
		}

		// stampa scacchiera
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

	public boolean castellamento(boolean lungo) {
		int row = turnoBianco ? 0 : GRID_LEN - 1;
		int colonnaRe = 4;
		int colonnaTorre = lungo ? 0 : GRID_LEN - 1;
		int colonnaTorreDest = lungo ? 3 : 5;

		if (scacchiera[row][colonnaRe].getPezzo() instanceof Re &&
				scacchiera[row][colonnaTorre].getPezzo() instanceof Torre) {
			if (isPercorsoLibero(row, colonnaRe, row, colonnaTorre) &&
					scacchiera[row][colonnaRe].getPezzo().isPrimaMossa() &&
					scacchiera[row][colonnaTorre].getPezzo().isPrimaMossa()) {

				spostaPezzo(scacchiera[row][colonnaTorre], scacchiera[row][colonnaTorreDest]);
				if (lungo) {
					spostaPezzo(scacchiera[row][colonnaRe], scacchiera[row][colonnaRe - 2]);
				} else {
					spostaPezzo(scacchiera[row][colonnaRe], scacchiera[row][colonnaRe + 2]);
				}

				return true;
			} else {
				msg.offer("non puoi fare arrocco");
			}
		}
		return false;
	}

	public boolean mossa(String c) {
		c = c.toUpperCase();
		switch (c.charAt(0)) {
			case 'Q':
				inPartita = false;
				return true;
			case 'O':
				if (c.equals("O-O")) {
					return castellamento(false);
				} else if (c.equals("O-O-O")) {
					return castellamento(true);
				} else {
					msg.offer("mossa non valida");
					return false;
				}
			default:
				Pattern pattern = Pattern.compile("([A-H][1-8]){2}"); // fa match regex con "A1A2"
				Matcher matcher = pattern.matcher(c);
				if (!matcher.matches()) {
					msg.offer("mossa non valida");
					return false;
				} else {
					int sCol = c.charAt(0) - 'A';
					int sRow = '8' - c.charAt(1);
					int eCol = c.charAt(2) - 'A';
					int eRow = '8' - c.charAt(3);

					return mossa(sRow, sCol, eRow, eCol);
				}
		}
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

		boolean mossaVerificata = false;
		// se pezzo può fare mossa
		if (casellaIniziale.getPezzo().verificaMossa(startRow, startCol, endRow, endCol, cattura)) {
			// se pezzo non può saltare, controlla che non ci siano pezzi tra inizio e fine
			if (casellaIniziale.getPezzo().puoSaltare()) {
				mossaVerificata = true;
			} else {
				mossaVerificata = isPercorsoLibero(startRow, startCol, endRow, endCol);
			}
		}

		if (mossaVerificata) {
			tmpGriglia.spostaPezzo(casellaIniziale, casellaDestinazione); // nella scacchiera temporanea, sposta pezzo
		} else {
			msg.offer(String.format("mossa [%c%d] -> [%c%d] non valida", toColonna(startCol), toRiga(startRow),
					toColonna(endCol), toRiga(endRow)));
			return false;
		}

		try {
			tmpGriglia.aggiornaScacco(); // dopo aver fatto la mossa, controllo che risultato ha avuto nella scacchiera
											// temporanea
		} catch (PezzoException pe) {
			// System.out.println(pe.getMessage());
		}

		if (!(tmpGriglia.scaccoReBianco || tmpGriglia.scaccoReNero) ||
				(tmpGriglia.scaccoReBianco && !turnoBianco || tmpGriglia.scaccoReNero && turnoBianco)) {
			spostaPezzo(scacchiera[startRow][startCol], scacchiera[endRow][endCol]);
			msg.offer("pezzo spostato da [" + toColonna(startCol) + toRiga(startRow) + "] a [" + toColonna(endCol)
					+ toRiga(endRow) + "]");
			try {
				aggiornaScacco();
			} catch (PezzoException pe) {
				msg.offer(pe.getMessage());
			}
			turnoBianco = !turnoBianco;
		} else {
			msg.offer(String.format("non puoi muovere pezzo [%c%d]: re sotto scacco!", toColonna(startCol),
					toRiga(startRow)));
		}

		if (endRow == 0 || endRow == GRID_LEN - 1) {
			if (scacchiera[endRow][endCol].getPezzo().puoPromuovere()) {
				scacchiera[endRow][endCol].setPezzo(new Regina(scacchiera[endRow][endCol].getPezzo().getColour()));
				msg.offer("pedone promosso a regina");
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
		return isPercorsoLibero(startRow, startCol, endRow, endCol, false);
	}

	private boolean isPercorsoLibero(int startRow, int startCol, int endRow, int endCol, boolean stampaMessaggio) {
		int deltaRow = endRow - startRow;
		int deltaCol = endCol - startCol;
		int stepRow = deltaRow == 0 ? 0 : deltaRow / Math.abs(deltaRow);
		int stepCol = deltaCol == 0 ? 0 : deltaCol / Math.abs(deltaCol);
		int row = startRow + stepRow;
		int col = startCol + stepCol;
		while (row != endRow || col != endCol) {
			if (!isFloor(row, col)) {
				if (stampaMessaggio) {
					msg.offer(String.format("[%c%d]\tpezzo trovato tra inizio e fine", toColonna(col), toRiga(row)));
				}
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

		this.scaccoReNero = this.scaccoReBianco = false;
		for (int i = 0; i < GRID_LEN; i++) {
			for (int j = 0; j < GRID_LEN; j++) {
				Pezzo p = scacchiera[i][j].getPezzo();
				if (p != null) {
					if (p.getColour() == Colour.WHITE) {
						if (p.verificaMossa(i, j, neroRow, neroCol, true) &&
								(p.puoSaltare() || isPercorsoLibero(i, j, neroRow, neroCol, false))) {
							this.scaccoReNero = true;
							msg.offer("re NERO sotto scacco da " + p.getLettera());
						}
					} else {
						if (p.verificaMossa(i, j, biancoRow, biancoCol, true) &&
								(p.puoSaltare() || isPercorsoLibero(i, j, biancoRow, biancoCol, false))) {
							this.scaccoReBianco = true;
							msg.offer("re BIANCO sotto scacco da " + p.getLettera());
						}
					}
				}
			}
		}

		if (scaccoReBianco || scaccoReNero) {
			int row = scaccoReBianco ? biancoRow : neroRow;
			int col = scaccoReBianco ? biancoCol : neroCol;
			boolean haMossa = false;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (isInsideBounds(row + i, col + j)) {
						if (isFloor(row + i, col + j)) {
							haMossa = true;
							break;
						} else {
							Pezzo p = scacchiera[row + i][col + j].getPezzo();
							if (p.getColour() != (scaccoReBianco ? Colour.WHITE : Colour.BLACK)) {
								if (p.verificaMossa(row, col, row + i, col + i, true)) {
									if (isPercorsoLibero(row, col, row + i, col + i, true)) {
										haMossa = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			if (!haMossa) {
				msg.offer("re sotto scacco matto");
				inPartita = false;
			}
		}

		return this.scaccoReNero || this.scaccoReBianco;
	}

	private boolean haScaccoMatto() {
		if (!scaccoReBianco && !scaccoReNero) {
			return false;
		}
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

		int row = scaccoReBianco ? biancoRow : neroRow;
		int col = scaccoReBianco ? biancoCol : neroCol;
		Pezzo re = scacchiera[row][col].getPezzo();
		boolean haMossa = false;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (isInsideBounds(row + i, col + j)) {
					if (isFloor(row + i, col + j)) {
						haMossa = true;
					} else if (scacchiera[row + i][col + j].getPezzo()
							.getColour() != re.getColour()) {
						Griglia tmpGriglia = this.clone();
						tmpGriglia.spostaPezzo(scacchiera[row][col], scacchiera[row + i][col + j]);
						try {
							tmpGriglia.aggiornaScacco();
						} catch (PezzoException pe) {
							break;
						}
						if (scaccoReBianco && !tmpGriglia.scaccoReBianco || scaccoReNero && !tmpGriglia.scaccoReNero) {
							haMossa = true;
						}
					}
				}
			}
		}
		return !haMossa;
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
		return (row >= 0 && row < GRID_LEN) &&
				(col >= 0 && col < GRID_LEN);
	}

	private void spostaPezzo(Casella partenza, Casella fine) {
		fine.setPezzo(partenza.getPezzo());
		partenza.setPezzo(null);
		fine.getPezzo().mossaFatta();
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
		return GRID_LEN - row;
	}

	/**
	 * @return true se non c'e un pezzo nella casella corrispondente.
	 */
	private boolean isFloor(int row, int col) {
		return scacchiera[row][col].getPezzo() == null;
	}

	public boolean isInPartita() {
		return inPartita;
	}
}
