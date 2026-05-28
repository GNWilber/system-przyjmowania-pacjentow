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

    // Przeciazenie: rejestracja standardowa
    public String zarejestrujPacjenta(Pacjent pacjent) {
        zarejestrowanychDzisiaj++;
        return "Pacjent " + pacjent + " zostal zarejestrowany na stanowisku " + numerStanowiska + ".";
    }

    // Przeciazenie: rejestracja z opcjonalnym priorytetem
    public String zarejestrujPacjenta(Pacjent pacjent, boolean priorytet) {
        zarejestrowanychDzisiaj++;
        String info = priorytet ? " z TRYBEM PRIORYTETOWYM" : "";
        return "Pacjent " + pacjent + " zostal zarejestrowany" + info +
               " na stanowisku " + numerStanowiska + ".";
    }

    public String wyswietlStatystyki() {
        return "Stanowisko nr " + numerStanowiska + " | Zarejestrowanych dzisiaj: " + zarejestrowanychDzisiaj;
    }

    public int pobierzNumerStanowiska() { return numerStanowiska; }

    @Override
    public String toString() {
        return imie + " " + nazwisko + " [Recepcja, stanowisko " + numerStanowiska + "]";
    }
}