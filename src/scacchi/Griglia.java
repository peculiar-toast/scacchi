package scacchi;

import java.util.ArrayList;
import java.util.ArrayDeque;

import scacchi.Utile.ChessPiece;
import scacchi.Utile.Colour;

public class Griglia {
    private static final int GRID_LEN = 8;
    private Casella [][] scacchiera;
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

    /**
     * uso per piazzare pezzi in uno stato iniziale
     */
    public void statoIniziale() {
	for (int colonna = 0; colonna < GRID_LEN; colonna++) {
	    scacchiera[1][colonna].setPezzo(new Pedone(Colour.BLACK));
	    scacchiera[GRID_LEN - 2][colonna].setPezzo(new Pedone(Colour.WHITE));
	}
	aggiornaScacco();
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
	    sb.append((char)('0' + GRID_LEN - i) + "|");
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
	    sb.append((char)('A' + i) + " ");
	}
	sb.append("\n");
	System.out.print(sb);
    }

    private boolean isInsideBounds(int row, int col) {
	return
	    (row >= 0 && row <= GRID_LEN) &&
	    (col >= 0 && col <= GRID_LEN);
    }

    private void spostaPezzo(Casella partenza, Casella fine)
    {
	fine.setPezzo(partenza.getPezzo());
	partenza.setPezzo(null);
    }

    /**
     * trova i re
     * guarda se ogni re è sotto scacco
     */
    private boolean aggiornaScacco() {
	int reBiancoRow, reBiancoCol;
	int reNeroRow, reNeroCol;
	reBiancoRow = reBiancoCol = reNeroRow = reNeroCol = -1;
	
	for (int i = 0; i < GRID_LEN; i++) {
	    for (int j = 0; j < GRID_LEN; j++) {
		Pezzo pezzo = scacchiera[i][j].getPezzo();
		if (pezzo != null && pezzo.getTipo() == ChessPiece.KING) {
		    if (pezzo.getColour() == Colour.WHITE) {
			reBiancoRow = i;
			reBiancoCol = j;
		    } else {
			reNeroRow = i;
			reNeroCol = j;
		    }
		}
	    }
	}

	this.scaccoReBianco = this.scaccoReNero = false;
	for (int i = 0; i < GRID_LEN; i++) {
	    for (int j = 0; j < GRID_LEN; j++) {
		if (!isFloor(i, j)) {
		    this.scaccoReBianco |= verificaMossa(i, j, reBiancoRow, reBiancoCol);
		    this.scaccoReNero |= verificaMossa(i, j, reNeroRow, reNeroCol);
		}
	    }
	}

	return this.scaccoReNero || this.scaccoReBianco;
    }

    public boolean mossaNotazione(String c) {
	c = c.toUpperCase();
	int sCol = c.charAt(0) - 'A';
	int sRow = '8' - c.charAt(1);
	int eCol = c.charAt(2) - 'A';
	int eRow = '8' - c.charAt(3);

	return mossa(sRow, sCol, eRow, eCol);
    }

    public boolean verificaMossa(int startRow, int startCol, int endRow, int endCol)
    {
	if (!isInsideBounds(startRow, startCol)) {
	    msg.offer(String.format("ERR verificaMossa(%d, %d): %s", startRow, startCol, "inizio mossa fuori da scacchiera"));
	    return false;
	}
	if (!isInsideBounds(endRow, endCol)) {
	    msg.offer(String.format("ERR verificaMossa(%d, %d): %s", startRow, startCol, "destinazione mossa fuori da scacchiera"));
	    return false;
	}
	if (isFloor(startRow, startCol)) {
	    msg.offer(String.format("ERR verificaMossa(%d, %d): %s", startRow, startCol, "nessun pezzo da muovere"));
	    return false;
	}

	Casella casellaIniziale = scacchiera[startRow][startCol];
	Casella casellaDestinazione = scacchiera[endRow][endCol];

	// se casellaDestinazione ha pezzo nemico, allora cattura
	// serve per pedone
	boolean cattura = false;
	if (casellaDestinazione.getPezzo() != null) {
	    if (casellaDestinazione.getPezzo().getColour() != casellaIniziale.getPezzo().getColour()) {
		cattura = true;		
	    } else {
		msg.offer(String.format("ERR verificaMossa(%d, %d): %s", startRow, startCol, "sto provando a catturare un alleato!"));
	    }
	}
	return casellaIniziale.getPezzo().verificaMossa(startRow, startCol, endRow, endCol, cattura);
    }
    
    /**
     * se mossa segue regole, sposta pezzo  
     * @return true se pezzo esiste e mossa avvenuta con successo
     */
    public boolean mossa(int startRow, int startCol, int endRow, int endCol)
    {
	if (verificaMossa(startRow, startCol, endRow, endCol)) {
	    spostaPezzo(scacchiera[startRow][startCol], scacchiera[endRow][endCol]);
	    return true;
	}
	return false;
    }
    
    /**
     * @return true se non c'e un pezzo nella casella corrispondente.
     */
    private boolean isFloor(int row, int col) {
	return scacchiera[row][col].getPezzo() == null;
    }
}
