class Entita {
    protected Colour colour;
    protected ChessPiece tipo;

    public Entita(Colour colour, ChessPiece tipo) {
	this.colour = colour;
	this.tipo = tipo;
    }

    /* printing */
    public char getLettera() {
	char notazione = '?';
	switch (tipo) {
	case PAWN:
	    notazione = (colour == Colour.WHITE) ? 'p' : 'P';
	    break;
	case ROOK:
	    notazione = (colour == Colour.WHITE) ? 'r' : 'R';
	    break;
	case KNIGT:
	    notazione = (colour == Colour.WHITE) ? 't' : 'T';
	    break;
	case BISHOP:
	    notazione = (colour == Colour.WHITE) ? 'b' : 'B';
	    break;
	case QUEEN:
	    notazione = (colour == Colour.WHITE) ? 'q' : 'Q';
	    break;
	case KING:
	    notazione = (colour == Colour.WHITE) ? 'k' : 'K';
	    break;
	}
	return notazione;
    }

    public boolean mossa(int startRow, int startCol, int endRow, int endCol)
    {
	return false;
    }
	
    public String toString() {
	return "Entita ["+ getLettera() +"]";
    }
    
    /* tipo */
    public ChessPiece getTipo() {
	return this.tipo;
    }

    public void setTipo(ChessPiece tipo) {
	// TODO permetti solo se tipo == PAWN
	this.tipo = tipo;
    }

    /* colour */
    public Colour getColour() {
	return this.colour;
    }
}
