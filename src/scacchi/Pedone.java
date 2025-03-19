package scacchi;

import scacchi.Utile.*;

public class Pedone extends Pezzo {
    private char lettera;
    private boolean primaMossa; // per controllare se puo muoversi di due spazi
    
    public Pedone(Colour colour) {
	super(colour, ChessPiece.PAWN);
	lettera = (colour == Colour.WHITE)? 'p' : 'P';
	primaMossa = true;
    }

    /**
     * pedone puo muoversi solo avanti, di due spazi se primaMossa.
     */
    public boolean verificaMossa(int startRow, int startCol, int endRow, int endCol, boolean cattura) {
	// pedoni bianchi e neri si muovono in direzioni opposte:
	// con questo voglio invertire la direzione dei pedoni bianchi.
	return verificaMossa(endRow - startRow, endCol - startCol, cattura);
    }

    public boolean verificaMossa(int deltaRow, int deltaCol, boolean cattura) {
	int direzioneVerticale = (getColour() == Colour.WHITE)? -1 : 1;
	deltaRow *= direzioneVerticale;

	boolean corretto = false;
	if (cattura) {
	    // per catturare, pedone può muovere avanti in diagonale
	    boolean catturaDiagonale = (deltaRow == 1) && (deltaCol == 1 || deltaCol == -1);
	    corretto = catturaDiagonale;
	} else {
	    // altrimenti può muoversi di uno in avanti. due se prima mossa.
	    boolean evitaMovimentoOrizzontale = deltaCol == 0;
	    boolean verificaMovimentoVerticale = deltaRow == 1 || (deltaRow == 2 && primaMossa);
	    corretto = evitaMovimentoOrizzontale && verificaMovimentoVerticale;
	}

	if (corretto && primaMossa) {
	    primaMossa = false;
	}
	
	return corretto;
    }

    public Pezzo clone() {
	return new Pedone(this.getColour());
    }
    
    public char getLettera()
    {
	return lettera;
    }
}
