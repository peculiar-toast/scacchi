package scacchi;

import scacchi.Utile.Colour;
import scacchi.Utile.ChessPiece;

/**
 * Classe base di ogni pezzo
 * I metodi di questa classe dovrebbero fallire se chiamati
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
}
