import Library.Biblioteca;
import Library.InvalidCode;
import Library.InvalidIsbn;
import Library.Libro;
import Library.Utente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Specifichiamo il nome del file di log
        String fileLog = "log_operazioni.txt";

        // Usiamo PrintWriter per scrivere su un file, con try-with-resources
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileLog)))) {
            Biblioteca b1 = new Biblioteca("Centrale");
            String nomeFile = "libri.txt";

            // Chiamiamo il metodo per caricare i libri dal file
            List<Libro> listaLibri = caricaLibriDaFile(nomeFile);

            if (!listaLibri.isEmpty()) {
                writer.println("Libri caricati dal file:");
                try {
                    for (Libro l : listaLibri) {
                        b1.addLibro(l);
                        writer.println("Aggiunto: " + l.getAutori() + ", Titolo: " + l.getTitolo());
                    }
                } catch (InvalidIsbn i) {
                    i.printStackTrace(writer); // Stampa l'errore nel file
                }
            } else {
                writer.println("Nessun libro caricato. Controlla il file o il suo percorso.");
            }

            Libro l1 = b1.getLibro("0-273-75976-0");
            Libro l2 = b1.getLibro("0-321-48681-1");
            Libro l3 = b1.getLibro("88-18-12369-6");
            Libro l4 = b1.getLibro("88-06-02550-3");

            writer.println("\n*** Informazioni sui libri caricati ***");
            writer.println("l1 = " + l1);
            writer.println("l2 = " + l2);
            writer.println("l3 = " + l3);
            writer.println("l4 = " + l4);

            writer.println("\n*** Libri ordinati per autore ***");
            for (Libro lib : b1.libriPerAutore()) {
                writer.println(lib);
            }

            Utente u1 = new Utente(1, "Mario", "Rossi");
            Utente u2 = new Utente(2, "Giuseppe", "Verdi");
            Utente u3 = new Utente(3, "Pietro", "Bianchi");
            Utente u4 = new Utente(4, "Giovanni", "Rossi");
            Utente u5 = new Utente(5, "Antonio", "Verdi");

            writer.println("\n*** Aggiunta utenti ***");
            try {
                b1.addUtente(u2);
                b1.addUtente(u1);
                b1.addUtente(u3);
                b1.addUtente(u5);
                b1.addUtente(u4);
            } catch (InvalidCode c) {
                c.printStackTrace(writer); // Stampa l'errore nel file
            }
            writer.println("Utenti ordinati per codice:");
            for (Utente u : b1.utenti()) {
                writer.println(u);
            }

            writer.println("\n*** Operazioni di prestito e restituzione ***");
            try {
                writer.println("Prestato a " + u1.getNome() + ": " + b1.prestito(1, "0-321-48681-1"));
                writer.println("Prestato a " + u1.getNome() + ": " + b1.prestito(1, "0-273-75976-0"));
                writer.println("Prestato a " + u1.getNome() + ": " + b1.prestito(1, "88-06-02550-3"));
                writer.println("Prestato a " + u3.getNome() + ": " + b1.prestito(3, "0-321-48681-1"));
                writer.println("Prestato a " + u2.getNome() + ": " + b1.prestito(2, "0-321-48681-1"));
                writer.println("Restituito da " + u1.getNome() + ": " + b1.restituzione(1, "0-321-48681-1"));
                writer.println("Prestato a " + u5.getNome() + ": " + b1.prestito(5, "88-06-02550-3"));
                writer.println("Prestato a " + u5.getNome() + ": " + b1.prestito(5, "88-18-12369-6"));
                writer.println("Prestato a " + u4.getNome() + ": " + b1.prestito(4, "0-321-48681-1"));
                writer.println("Restituito da " + u1.getNome() + ": " + b1.restituzione(1, "88-06-02550-3"));
                writer.println("Prestato a " + u5.getNome() + ": " + b1.prestito(5, "88-18-12369-6"));
                writer.println("Prestato a " + u2.getNome() + ": " + b1.prestito(2, "0-273-75976-0"));
            } catch (InvalidCode c) {
                c.printStackTrace(writer);
            } catch (InvalidIsbn i) {
                i.printStackTrace(writer);
            }

            writer.println("\n*** Elenco prestiti utente " + u1 + " ***");
            for (Libro lib : u1.prestiti()) {
                writer.println("  " + lib);
            }

            writer.println("\n*** Elenco richieste per il libro " + l2.getTitolo() + " ***");
            for (Utente u : b1.getRichieste(l2)) {
                writer.println("  " + u);
            }

            writer.println("\n*** Elenco prestiti totali ***");
            for (Libro lib : b1.elencoPrestiti()) {
                writer.println(" " + lib);
            }

            writer.println("\n*** Elenco richieste totali ***");
            for (Libro lib : b1.elencoRichieste()) {
                writer.println("  " + lib);
            }

            System.out.println("Output salvato con successo nel file: " + fileLog);

        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file di log: " + e.getMessage());
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