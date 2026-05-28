package szpital;

import java.time.LocalDate;

public class Pielegniarka extends Pracownik {
    private Oddzial oddzial;
    private int numerSali;

    public Pielegniarka(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia,
                        String idPracownika, String specjalizacja, int numerSali) {
        super(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja);
        this.numerSali = numerSali;
    }

    @Override
    public void wykonajObowiazki() {
        System.out.println("Pielęgniarka " + imie + " " + nazwisko +
                           " opiekuje sie pacjentami na sali " + numerSali + ".");
    }

    @Override
    public String wyswietlInfo() {
        return "Pielęgniarka: " + imie + " " + nazwisko + "\n" +
               "Specjalizacja: " + specjalizacja + "\n" +
               "Sala dyżurowa: " + numerSali + "\n" +
               "Aktywna: " + aktywny;
    }

    public void przydzielLozko(Pacjent pacjent, int numerLozka) {
        System.out.println("Pielęgniarka " + imie + " przydzielila lozko nr " +
                           numerLozka + " dla: " + pacjent);
    }

    public void zaopiekujSie(Pacjent pacjent) {
        System.out.println("Pielęgniarka " + imie + " " + nazwisko +
                           " zaopiekowala sie pacjentem: " + pacjent);
    }

    public void zmienOddzial(Oddzial nowyOddzial) {
        this.oddzial = nowyOddzial;
        System.out.println("Pielęgniarka " + imie + " " + nazwisko + " przeniesiona na inny oddzial.");
    }

    public int pobierzNumerSali() { return numerSali; }

    @Override
    public String toString() {
        return imie + " " + nazwisko + " [Pielęgniarka, sala " + numerSali + "]";
    }
}