package scacchi;

import scacchi.Utile.Colour;
import scacchi.Utile.ChessPiece;

/**
 * Classe base di ogni pezzo
 */
public abstract class Pezzo {
    private Colour colour;
    private ChessPiece tipo;

    public Pezzo(Colour colour, ChessPiece tipo) {
	this.colour = colour;
	this.tipo = tipo;
    }

    /* printing */
    public abstract char getLettera();
    public abstract boolean verificaMossa(int deltaRow, int deltaCol, boolean cattura);
    public abstract boolean verificaMossa(int startRow, int startCol, int endRow, int endCol, boolean cattura);
	
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
