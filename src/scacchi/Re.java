package scacchi;

import scacchi.Utile.*;

public class Re extends Pezzo {
	boolean primaMossa;

	public Re(Colour colore) {
		super(colore, ChessPiece.KING);
		primaMossa = true;
	}

	public boolean verificaMossa(int sRow, int sCol, int eRow, int eCol, boolean cattura) {
		return verificaMossa(eRow - sRow, eCol - sCol, cattura);
	}

	public boolean verificaMossa(int dRow, int dCol) {
		return verificaMossa(dRow, dCol, false);
	}

	// non gestisco arrocco?
	public boolean verificaMossa(int dRow, int dCol, boolean cattura) {
		boolean mossaVerificata = true;

		// permetti arrocco se prima mossa
		int dColMassimo = (primaMossa) ? 2 : 1;
		mossaVerificata &= !(dRow == 0 && dCol == 0) &&
				dRow >= -1 && dRow <= 1 &&
				dCol >= -dColMassimo && dCol <= dColMassimo;

		if (mossaVerificata) {
			this.primaMossa = false;
		}
		return mossaVerificata;
	}

	public Pezzo clone() {
		return new Re(this.getColour());
	}

	public char getLettera() {
		return (this.getColour() == Colour.WHITE) ? 'k' : 'K';
	}
}
