package szpital;

import java.time.LocalDate;

public class Recepcjonista extends Pracownik {
    private int numerStanowiska;
    private int zarejestrowanychDzisiaj;

    public Recepcjonista(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia,
                         String idPracownika, int numerStanowiska) {
        super(imie, nazwisko, pesel, dataUrodzenia, idPracownika, "Recepcja");
        this.numerStanowiska = numerStanowiska;
        this.zarejestrowanychDzisiaj = 0;
    }

    @Override
    public void wykonajObowiazki() {
        System.out.println("Recepcjonista " + imie + " " + nazwisko +
                           " rejestruje pacjentow na stanowisku nr " + numerStanowiska + ".");
    }

    @Override
    public String wyswietlInfo() {
        return "Recepcjonista: " + imie + " " + nazwisko + "\n" +
               "Stanowisko nr: " + numerStanowiska + "\n" +
               "Zarejestrowanych dzisiaj: " + zarejestrowanychDzisiaj;
    }

    public String zarejestrujPacjenta(Pacjent pacjent) {
        zarejestrowanychDzisiaj++;
        return "Pacjent " + pacjent.toString() + " zostal zarejestrowany na stanowisku " + numerStanowiska + ".";
    }

    public String zarejestrujPacjenta(Pacjent pacjent, boolean priorytet) {
        zarejestrowanychDzisiaj++;
        String komunikat = "Pacjent " + pacjent.toString() + " zostal zarejestrowany";
        if (priorytet) {
            komunikat += " z TRYBEM PRIORYTETOWYM";
        }
        return komunikat + " na stanowisku " + numerStanowiska + ".";
    }

    public String wyswietlStatystyki() {
        return "Stanowisko nr " + numerStanowiska + " | " +
               "Zarejestrowanych dzisiaj: " + zarejestrowanychDzisiaj;
    }

    public int pobierzNumerStanowiska() {
        return numerStanowiska;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko + " [Recepcja, stanowisko " + numerStanowiska + "]";
    }
}