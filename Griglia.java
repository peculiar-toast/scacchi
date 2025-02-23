enum Colour {
    WHITE,
    BLACK,
}

enum ChessPiece {
    PAWN,
    ROOK,
    KNIGT,
    BISHOP,
    QUEEN,
    KING,
}

class Griglia {
    private static final int GRID_LEN = 8;
    private Casella [][] board;
    
    public Griglia() {
	board = new Casella[GRID_LEN][GRID_LEN];

	for (int riga = 0; riga < GRID_LEN; riga++) {
	    for (int colonna = 0; colonna < GRID_LEN; colonna++) {
		Colour coloreCasella = (riga + colonna) % 2 == 0 ? Colour.WHITE : Colour.BLACK;
		board[riga][colonna] = new Casella(coloreCasella, null);
	    }
	}
	
	statoIniziale();
    }

    /**
     * uso per piazzare pezzi in uno stato iniziale
     */
    public void statoIniziale() {
	for (int colonna = 0; colonna < GRID_LEN; colonna++) {
	    board[1][colonna].setPezzo(new Pedone(Colour.BLACK));
	    board[GRID_LEN - 2][colonna].setPezzo(new Pedone(Colour.WHITE));
	}
    }

    public void print() {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < GRID_LEN; i++) {
	    sb.append((char)('0' + GRID_LEN - i) + "|");
	    for (int j = 0; j < GRID_LEN; j++) {
		sb.append(board[i][j].getLettera() + " ");
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
     * @return true se pezzo esiste e mossa avvenuta con successo
     */
    public boolean mossa(int startRow, int startCol, int endRow, int endCol) 
    {
	Entita pezzoDaMuovere = board[startRow][startCol].getPezzo();
	boolean risultato = false;
	if (pezzoDaMuovere != null) {
	    risultato = pezzoDaMuovere.mossa(startRow, startCol, endRow, endCol);
	    if (risultato) {
		spostaPezzo(board[startRow][startCol], board[endRow][endCol]);
	    }
	}
	return risultato;
    }
    
    /**
     * @return true se non c'e un pezzo nella casella corrispondente.
     */
    private boolean isFloor(int row, int col) {
	return board[row][col].getPezzo() == null;
    }

    /**
     * @return true se casella a posizione e bianca
     */
    private boolean isWhiteSpace(int row, int col) {
	return board[row][col].getColore() == Colour.WHITE;
    }
}
