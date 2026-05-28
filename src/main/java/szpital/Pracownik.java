package szpital;

import java.time.LocalDate;

public abstract class Pracownik extends Osoba {
    protected String idPracownika;
    protected String specjalizacja;
    protected boolean aktywny;

    public Pracownik(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia,
                     String idPracownika, String specjalizacja) {
        super(imie, nazwisko, pesel, dataUrodzenia);
        this.idPracownika = idPracownika;
        this.specjalizacja = specjalizacja;
        this.aktywny = true;
    }

    public abstract void wykonajObowiazki();

    @Override
    public abstract String wyswietlInfo();

    public void dezaktywuj() {
        this.aktywny = false;
    }

    public String wyswietlKarte() {
        return "ID: " + idPracownika + "\n" +
               "Pracownik: " + imie + " " + nazwisko + "\n" +
               "Specjalizacja: " + specjalizacja + "\n" +
               "Aktywny: " + aktywny;
    }

    public String pobierzIdPracownika()  { return idPracownika; }
    public String pobierzSpecjalizacje() { return specjalizacja; }
    public boolean czyAktywny()          { return aktywny; }
}