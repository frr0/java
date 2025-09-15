import Library.Biblioteca;
import Library.InvalidCode;
import Library.InvalidIsbn;
import Library.Libro;
import Library.Utente;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Biblioteca b1 = new Biblioteca("Centrale");

        String nomeFile = "libri.txt";

        // Chiamiamo il metodo per caricare i libri dal file
        List<Libro> listaLibri = caricaLibriDaFile(nomeFile);

        if (!listaLibri.isEmpty()) {
            System.out.println("Libri caricati dal file:");
            try {
                for (Libro l : listaLibri) {
                    b1.addLibro(l);
                    System.out.println("Aggiunto: " + l.getAutori() + ", Titolo: " + l.getTitolo());
                }
            } catch (InvalidIsbn i) {
                i.printStackTrace();
            }
        } else {
            System.out.println("Nessun libro caricato. Controlla il file o il suo percorso.");
        }

        Libro l1 = b1.getLibro("0-273-75976-0");
        Libro l2 = b1.getLibro("0-321-48681-1");
        Libro l3 = b1.getLibro("88-18-12369-6");
        Libro l4 = b1.getLibro("88-06-02550-3");

        System.out.println("\n*** Informazioni sui libri caricati ***");
        System.out.println("l1 = " + l1);
        System.out.println("l2 = " + l2);
        System.out.println("l3 = " + l3);
        System.out.println("l4 = " + l4);

        System.out.println("\n*** Libri ordinati per autore ***");
        for(Libro lib : b1.libriPerAutore()) {
            System.out.println(lib);
        }

        Utente u1 = new Utente(1, "Mario", "Rossi");
        Utente u2 = new Utente(2, "Giuseppe", "Verdi");
        Utente u3 = new Utente(3, "Pietro", "Bianchi");
        Utente u4 = new Utente(4, "Giovanni", "Rossi");
        Utente u5 = new Utente(5, "Antonio", "Verdi");

        System.out.println("\n*** Aggiunta utenti ***");
        try {
            b1.addUtente(u2);
            b1.addUtente(u1);
            b1.addUtente(u3);
            b1.addUtente(u5);
            b1.addUtente(u4);
        } catch (InvalidCode c) {
            c.printStackTrace();
        }
        System.out.println("Utenti ordinati per codice:");
        for(Utente u : b1.utenti()) {
            System.out.println(u);
        }

        System.out.println("\n*** Operazioni di prestito e restituzione ***");
        try {
            System.out.println("Prestato a " + u1.getNome() + ": " + b1.prestito(1,"0-321-48681-1"));
            System.out.println("Prestato a " + u1.getNome() + ": " + b1.prestito(1,"0-273-75976-0"));
            System.out.println("Prestato a " + u1.getNome() + ": " + b1.prestito(1,"88-06-02550-3"));
            System.out.println("Prestato a " + u3.getNome() + ": " + b1.prestito(3,"0-321-48681-1"));
            System.out.println("Prestato a " + u2.getNome() + ": " + b1.prestito(2,"0-321-48681-1"));
            System.out.println("Restituito da " + u1.getNome() + ": " + b1.restituzione(1,"0-321-48681-1"));
            System.out.println("Prestato a " + u5.getNome() + ": " + b1.prestito(5,"88-06-02550-3"));
            System.out.println("Prestato a " + u5.getNome() + ": " + b1.prestito(5,"88-18-12369-6"));
            System.out.println("Prestato a " + u4.getNome() + ": " + b1.prestito(4,"0-321-48681-1"));
            System.out.println("Restituito da " + u1.getNome() + ": " + b1.restituzione(1,"88-06-02550-3"));
            System.out.println("Prestato a " + u5.getNome() + ": " + b1.prestito(5,"88-18-12369-6"));
            System.out.println("Prestato a " + u2.getNome() + ": " + b1.prestito(2,"0-273-75976-0"));
        } catch (InvalidCode c) {
            c.printStackTrace();
        } catch (InvalidIsbn i) {
            i.printStackTrace();
        }

        System.out.println("\n*** Elenco prestiti utente " + u1 + " ***");
        for(Libro lib : u1.prestiti()) {
            System.out.println("  " + lib);
        }

        System.out.println("\n*** Elenco richieste per il libro " + l2.getTitolo() + " ***");
        for(Utente u : b1.getRichieste(l2)) {
            System.out.println("  " + u);
        }

        System.out.println("\n*** Elenco prestiti totali ***");
        for(Libro lib : b1.elencoPrestiti()) {
            System.out.println(" " + lib);
        }

        System.out.println("\n*** Elenco richieste totali ***");
        for(Libro lib : b1.elencoRichieste()){
            System.out.println("  " + lib);
        }
    }

    public static List<Libro> caricaLibriDaFile(String nomeFile) {
        List<Libro> libri = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nomeFile))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.trim().isEmpty()) {
                    continue;
                }
                String[] campi = riga.split(";");
                if (campi.length == 4) {
                    Libro nuovoLibro = new Libro(
                            campi[0].trim(),
                            campi[1].trim(),
                            campi[2].trim(),
                            campi[3].trim()
                    );
                    libri.add(nuovoLibro);
                } else {
                    System.err.println("Riga non valida nel file: " + riga);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file " + nomeFile + ": " + e.getMessage());
        }
        return libri;
    }
}