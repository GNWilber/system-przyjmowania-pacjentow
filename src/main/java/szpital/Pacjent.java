package szpital;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Pacjent extends Osoba implements IRaportowalny {
    private String idPacjenta;
    private String grupaKrwi;
    private KartaPacjenta kartaPacjenta;
    private boolean wPoczekalni;

    public Pacjent(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia, String grupaKrwi) {
        super(imie, nazwisko, pesel, dataUrodzenia);
        this.idPacjenta = "P" + System.currentTimeMillis();
        this.grupaKrwi = grupaKrwi;
        this.kartaPacjenta = new KartaPacjenta("K" + System.currentTimeMillis());
        this.wPoczekalni = false;
    }

    @Override
    public String wyswietlInfo() {
        return "Pacjent: " + imie + " " + nazwisko + "\n" +
               "PESEL: " + pesel + "\n" +
               "Grupa krwi: " + grupaKrwi + "\n" +
               "Wiek: " + obliczWiek() + "\n" +
               "W poczekalni: " + wPoczekalni + "\n" +
               kartaPacjenta.wyswietlAlergie() + "\n" +
               kartaPacjenta.wyswietlChoroby();
    }

    @Override
    public String generujRaport() {
        return "=== RAPORT PACJENTA ===\n" +
               wyswietlInfo() + "\n" +
               kartaPacjenta.generujHistorie();
    }

    @Override
    public void eksportujDoPliku(String sciezka) {
        try (FileWriter fw = new FileWriter(sciezka)) {
            fw.write(generujRaport());
            System.out.println("Wyeksportowano dane do: " + sciezka);
        } catch (IOException e) {
            System.out.println("Blad podczas eksportu: " + e.getMessage());
        }
    }

    public void dodajWizyte(Wizyta wizyta) {
        kartaPacjenta.dodajWizyte(wizyta);
    }

    public String wyswietlHistorieWizyt() {
        return kartaPacjenta.generujHistorie();
    }

    public void zaznaczWPoczekalni(boolean stan) {
        this.wPoczekalni = stan;
    }

    public void dodajAlergie(String alergia) {
        kartaPacjenta.dodajAlergie(alergia);
    }

    public void dodajChorobe(String choroba) {
        kartaPacjenta.dodajChorobe(choroba);
    }

    public String pobierzImie() {
        return imie;
    }

    public String pobierzNazwisko() {
        return nazwisko;
    }

    public String pobierzGrupeKrwi() {
        return grupaKrwi;
    }

    public String pobierzIdPacjenta() {
        return idPacjenta;
    }

    public boolean czyWPoczekalni() {
        return wPoczekalni;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko + " | PESEL: " + pesel + " | Gr. krwi: " + grupaKrwi;
    }
}