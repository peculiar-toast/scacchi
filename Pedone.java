class Pedone extends Entita {
    private char lettera;
    private boolean primaMossa; // per controllare se puo muoversi di due spazi
    
    Pedone(Colour colour) {
	super(colour, ChessPiece.PAWN);
	primaMossa = true;
	lettera = (colour == Colour.WHITE)? 'p' : 'P';
    }

    /**
     * pedone puo muoversi solo avanti, di due spazi se primaMossa.
     */
    @Override
    public boolean mossa(int startRow, int startCol, int endRow, int endCol) {
	// pedoni bianchi e neri si muovono in direzioni opposte:
	// con questo voglio invertire la direzione dei pedoni bianchi.
	int direzioneVerticale = (colour == Colour.WHITE)? -1 : 1;

	int deltaRow = direzioneVerticale * (endRow - startRow);
	int deltaCol = endCol - startCol;

	boolean evitaMovimentoOrizzontale = deltaCol == 0;
	boolean verificaMovimentoVerticale = deltaRow == 1 || (deltaRow == 2 && primaMossa);
	boolean corretto = evitaMovimentoOrizzontale && verificaMovimentoVerticale;

	if (corretto && primaMossa) {
	    primaMossa = false;
	}
	
	return corretto;
    }

    @Override
    public char getLettera()
    {
	return lettera;
    }
}
