package Library;

public class Libro implements Comparable<Libro>{
    private String autori;
    private String titolo;
    private String editore;
    private String isbn;

    public Libro (String a, String t, String e, String i) {
        autori = a;
        titolo = t;
        editore = e;
        isbn = i;
    }

    public String getIsbn () {
        return isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutori () {
        return autori;
    }

    // Aggiunto per un output più chiaro nel main
    public String toString(){
        return "Libro: " + titolo + " (Autore: " + autori + ", ISBN: " + isbn + ")";
    }

    @Override
    public int compareTo(Libro l) {
        return autori.compareTo(l.getAutori());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return isbn.equals(libro.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}