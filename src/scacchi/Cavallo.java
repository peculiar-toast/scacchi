package scacchi;

import scacchi.Utile.*;

public class Cavallo extends Pezzo {
    public Cavallo(Colour colore) {
        super(colore, ChessPiece.QUEEN);
    }

    public boolean verificaMossa(int sRow, int sCol, int eRow, int eCol, boolean cattura) {
        return verificaMossa(eRow - sRow, eCol - sCol, cattura);
    }

    public boolean verificaMossa(int dRow, int dCol) {
        return verificaMossa(dRow, dCol, false);
    }

    private int toPos(int v) {
        return (v < 0) ? -v : v;
    }

    public boolean verificaMossa(int dRow, int dCol, boolean cattura) {
        boolean mossaVerificata = true;
        dRow = toPos(dRow);
        dCol = toPos(dCol);

        mossaVerificata &= (dRow == 1 && dCol == 2) || (dRow == 2 && dCol == 1);
        return mossaVerificata;
    }

    public boolean puoSaltare() {
        return true;
    }

    public Pezzo clone() {
        return new Cavallo(this.getColour());
    }

    public char getLettera() {
        return (this.getColour() == Colour.WHITE) ? 't' : 'T';
    }
}
