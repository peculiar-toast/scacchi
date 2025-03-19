package scacchi;

import scacchi.Utile.Colour;

class Casella {
    private Colour colore;
    private Pezzo pezzo;

    Casella(Colour colore, Pezzo pezzo) {
        this.colore = colore;
        this.pezzo = pezzo;
    }

    Casella(Casella c) {
        this.colore = c.colore;
        this.pezzo = (c.getPezzo() == null) ? null : c.pezzo.clone();
    }

    /* lettera corrispondente */
    public char getLettera() {
        char lettera = '?';
        if (pezzo == null) {
            lettera = (colore == Colour.WHITE) ? '.' : '#';
        } else {
            lettera = pezzo.getLettera();
        }
        return lettera;
    }

    /* colore */
    public Colour getColore() {
        return this.colore;
    }

    /* pezzo */
    public Pezzo getPezzo() {
        return this.pezzo;
    }

    public void setPezzo(Pezzo pezzo) {
        this.pezzo = pezzo;
    }
}
