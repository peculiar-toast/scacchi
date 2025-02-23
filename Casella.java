class Casella 
{
    private Colour colore;
    private Entita pezzo;

    Casella (Colour colore, Entita pezzo)
    {
	this.colore = colore;
	this.pezzo = pezzo;
    }

    /* lettera corrispondente */
    public char getLettera()
    {
	char lettera = '?';
	if (pezzo == null) {
	    lettera = (colore == Colour.WHITE) ? '.' : '#';
	} else {
	    lettera = pezzo.getLettera();
	}
	return lettera;
    }

    /* colore */
    public Colour getColore() 
    {
	return this.colore;
    }

    /* pezzo */
    public Entita getPezzo() 
    {
	return this.pezzo;
    }
    
    public void setPezzo(Entita pezzo) 
    {
	this.pezzo = pezzo;
    }
}
