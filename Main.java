import java.util.Scanner;

class Main {
    static Scanner in = new Scanner(System.in);
    static Griglia g = new Griglia();

    // TODO lmao trova un modo migliore
    public static String getMossa()
    {
	System.out.print("Inserisci comando: ");
	char comm = in.next().charAt(0);
	System.out.print("\n");

	System.out.print("inserisci riga del pezzo iniziale: ");
	int sRow = in.nextInt();
	System.out.print("\n");

	System.out.print("inserisci colonna del pezzo iniziale: ");
	int sCol = in.nextInt();
	System.out.print("\n");

	System.out.print("inserisci riga del pezzo finale: ");
	int eRow = in.nextInt();
	System.out.print("\n");

	System.out.print("inserisci colonna del pezzo finale: ");
	int eCol = in.nextInt();
	System.out.print("\n");

	System.out.printf("risultati: %c %d%d %d%d\n", comm, sRow, sCol, eRow, eCol);

	g.mossa(sRow, sCol, eRow, eCol);
	return "string";
    }
    
    public static void main (String[] args) {
	g.print();
	getMossa();
	
	g.print();
    }
}
