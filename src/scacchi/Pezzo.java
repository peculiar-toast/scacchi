package scacchi;

import scacchi.Utile.Colour;
import scacchi.Utile.ChessPiece;

/**
 * Classe base di ogni pezzo
 */
public abstract class Pezzo {
    private Colour colour;
    private ChessPiece tipo;
    private boolean primaMossa = true;

    public Pezzo(Colour colour, ChessPiece tipo) {
        this.colour = colour;
        this.tipo = tipo;
    }

    /* printing */
    public abstract char getLettera();

    public abstract boolean verificaMossa(int deltaRow, int deltaCol, boolean cattura);

    public abstract boolean verificaMossa(int startRow, int startCol, int endRow, int endCol, boolean cattura);

    public boolean puoSaltare() {
        return false;
    }

    public boolean puoPromuovere() {
        return false;
    }

    public abstract Pezzo clone();

    /* tipo */
    public ChessPiece getTipo() {
        return this.tipo;
    }

    /* colour */
    public Colour getColour() {
        return this.colour;
    }

    public boolean isPrimaMossa() {
		return primaMossa;
	}

	public void mossaFatta() {
		this.primaMossa = false;
	}
}
