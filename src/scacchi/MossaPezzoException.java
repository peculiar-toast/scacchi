package scacchi;

public class MossaPezzoException extends PezzoException {
    public MossaPezzoException(String messaggio) {
	super(messaggio);
    }
    
    public MossaPezzoException(String messaggio, int row, int col) {
	super(String.format("[%d %d]\t%s", row, col, messaggio));
    }
}
