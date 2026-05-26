package szpital;

import java.time.LocalDate;
import java.time.Period;

public abstract class Osoba {
    protected String imie;
    protected String nazwisko;
    protected String pesel;
    protected LocalDate dataUrodzenia;

    public Osoba(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.dataUrodzenia = dataUrodzenia;
    }

    public abstract String wyswietlInfo();

    @Override
    public String toString() {
        return imie + " " + nazwisko + " (PESEL: " + pesel + ")";
    }

    public boolean peselPasuje(String pesel) {
        return this.pesel.equals(pesel);
    }

    public int obliczWiek() {
        return Period.between(dataUrodzenia, LocalDate.now()).getYears();
    }
}