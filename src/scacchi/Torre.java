package scacchi;

import scacchi.Utile.*;

public class Torre extends Pezzo {
    public Torre(Colour colore) {
        super(colore, ChessPiece.QUEEN);
    }

    public boolean verificaMossa(int sRow, int sCol, int eRow, int eCol, boolean cattura) {
        return verificaMossa(eRow - sRow, eCol - sCol, cattura);
    }

    public boolean verificaMossa(int dRow, int dCol) {
        return verificaMossa(dRow, dCol, false);
    }

    public boolean verificaMossa(int dRow, int dCol, boolean cattura) {
        boolean mossaVerificata = true;
        mossaVerificata &= // non puoi stare fermo
                !(dRow == 0 && dCol == 0);
        mossaVerificata &= // permetti mosse orizzontali o diagonali
                (dRow == 0 && dCol != 0) || (dRow != 0 && dCol == 0);
        return mossaVerificata;
    }

    public Pezzo clone() {
        return new Torre(this.getColour());
    }

    public char getLettera() {
        return (this.getColour() == Colour.WHITE) ? 'r' : 'R';
    }
}
