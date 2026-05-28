package szpital;

import java.util.ArrayList;
import java.util.List;

public class Oddzial {
    private String nazwaOddzialu;
    private String numerOddzialu;
    private int maxLozek;
    private List<Pracownik> pracownicy;
    private List<Pacjent> pacjenci;

    public Oddzial(String nazwaOddzialu, String numerOddzialu, int maxLozek) {
        this.nazwaOddzialu = nazwaOddzialu;
        this.numerOddzialu = numerOddzialu;
        this.maxLozek = maxLozek;
        this.pracownicy = new ArrayList<>();
        this.pacjenci = new ArrayList<>();
    }

    // Zwraca false jesli brak wolnych miejsc
    public boolean przyjmijPacjenta(Pacjent pacjent) {
        if (dostepneLozka() <= 0) return false;
        pacjenci.add(pacjent);
        return true;
    }

    public boolean wypiszPacjenta(Pacjent pacjent) {
        return pacjenci.remove(pacjent);
    }

    public void dodajPracownika(Pracownik pracownik) {
        pracownicy.add(pracownik);
    }

    public void usunPracownika(Pracownik pracownik) {
        pracownicy.remove(pracownik);
    }

    public int dostepneLozka() {
        return maxLozek - pacjenci.size();
    }

    public String wyswietlStatus() {
        return "Oddzial: " + nazwaOddzialu + " (" + numerOddzialu + ")\n" +
               "Zajete lozka: " + pacjenci.size() + " / " + maxLozek + "\n" +
               "Wolne lozka: " + dostepneLozka() + "\n" +
               "Liczba personelu: " + pracownicy.size();
    }

    public String wyswietlPracownikow() {
        if (pracownicy.isEmpty()) return "Brak przypisanego personelu do oddzialu " + nazwaOddzialu + ".";
        StringBuilder sb = new StringBuilder("Personel oddzialu " + nazwaOddzialu + ":\n");
        for (Pracownik p : pracownicy) sb.append("  ").append(p).append("\n");
        return sb.toString();
    }

    public String pobierzNazwe()      { return nazwaOddzialu; }
    public String pobierzNumer()      { return numerOddzialu; }
    public int pobierzMaxLozek()      { return maxLozek; }
    public List<Pacjent> pobierzPacjentow()     { return pacjenci; }
    public List<Pracownik> pobierzPracownikow() { return pracownicy; }

    @Override
    public String toString() {
        return nazwaOddzialu + " [" + numerOddzialu + "] | wolne: " + dostepneLozka() + "/" + maxLozek;
    }
}