package scacchi;

public class MossaPezzoException extends PezzoException {
    public MossaPezzoException(String messaggio) {
        super(messaggio);
    }

    public MossaPezzoException(String messaggio, int row, int col) {
        super(String.format("[%c %d]\t%s", Griglia.toColonna(col), Griglia.toRiga(row), messaggio));
    }
}
