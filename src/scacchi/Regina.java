package scacchi;

import scacchi.Utile.*;

public class Regina extends Pezzo {
    public Regina(Colour colore) {
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
        mossaVerificata &= // non puoi stare fermo
                !(dRow == 0 && dCol == 0);
        mossaVerificata &= // permetti mosse orizzontali o diagonali
                (dRow == 0 && dCol != 0) ||
                        (dRow != 0 && dCol == 0) ||
                        (toPos(dRow) == toPos(dCol));
        return mossaVerificata;
    }

    public Pezzo clone() {
        return new Regina(this.getColour());
    }

    public char getLettera() {
        return (this.getColour() == Colour.WHITE) ? 'q' : 'Q';
    }
}
