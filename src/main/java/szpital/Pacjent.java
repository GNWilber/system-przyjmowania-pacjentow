package szpital;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Pacjent extends Osoba implements IRaportowalny {
    private static int licznik = 0;

    private String idPacjenta;
    private String grupaKrwi;
    private KartaPacjenta kartaPacjenta;
    private boolean wPoczekalni;

    public Pacjent(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia, String grupaKrwi) {
        super(imie, nazwisko, pesel, dataUrodzenia);
        this.idPacjenta = "P" + String.format("%04d", ++licznik);
        this.grupaKrwi = grupaKrwi;
        this.kartaPacjenta = new KartaPacjenta("K" + idPacjenta);
        this.wPoczekalni = false;
    }

    @Override
    public String wyswietlInfo() {
        return "Pacjent: " + imie + " " + nazwisko + "\n" +
               "PESEL: " + pesel + "\n" +
               "Wiek: " + obliczWiek() + "\n" +
               "Grupa krwi: " + grupaKrwi + "\n" +
               "W poczekalni: " + wPoczekalni + "\n" +
               kartaPacjenta.wyswietlAlergie() + "\n" +
               kartaPacjenta.wyswietlChoroby();
    }

    @Override
    public String generujRaport() {
        return "=== RAPORT PACJENTA ===\n" + wyswietlInfo() + "\n" + kartaPacjenta.generujHistorie();
    }

    @Override
    public void eksportujDoPliku(String sciezka) {
        try (FileWriter fw = new FileWriter(sciezka)) {
            fw.write(generujRaport());
        } catch (IOException e) {
            System.out.println("Blad eksportu: " + e.getMessage());
        }
    }

    public void dodajWizyte(Wizyta wizyta) {
        kartaPacjenta.dodajWizyte(wizyta);
    }

    public void dodajAlergie(String alergia) {
        kartaPacjenta.dodajAlergie(alergia);
    }

    public void dodajChorobe(String choroba) {
        kartaPacjenta.dodajChorobe(choroba);
    }

    public void zaznaczWPoczekalni(boolean stan) {
        this.wPoczekalni = stan;
    }

    public String wyswietlHistorieWizyt() {
        return kartaPacjenta.generujHistorie();
    }

    public String pobierzIdPacjenta() { return idPacjenta; }
    public String pobierzGrupeKrwi() { return grupaKrwi; }
    public boolean czyWPoczekalni()   { return wPoczekalni; }

    @Override
    public String toString() {
        return imie + " " + nazwisko + " | PESEL: " + pesel + " | Gr. krwi: " + grupaKrwi;
    }
}