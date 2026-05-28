package szpital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Lekarz extends Pracownik {
    private String numerPWZ;
    private List<Wizyta> listaWizyt;
    private boolean dostepny;

    public Lekarz(String imie, String nazwisko, String pesel, LocalDate dataUrodzenia,
                  String idPracownika, String specjalizacja, String numerPWZ) {
        super(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja);
        this.numerPWZ = numerPWZ;
        this.listaWizyt = new ArrayList<>();
        this.dostepny = true;
    }

    @Override
    public void wykonajObowiazki() {
        System.out.println("Lekarz " + imie + " " + nazwisko + " przyjmuje pacjentow.");
    }

    @Override
    public String wyswietlInfo() {
        return "Lekarz: " + imie + " " + nazwisko + "\n" +
               "Specjalizacja: " + specjalizacja + "\n" +
               "PWZ: " + numerPWZ + "\n" +
               "Dostepny: " + dostepny + "\n" +
               "Liczba wizyt: " + listaWizyt.size();
    }

    // Rejestruje wizyte ambulatoryjną na miejscu (poza harmonogramem)
    public Wizyta przyjmijPacjenta(Pacjent pacjent) {
        Wizyta wizyta = new Wizyta(pacjent, this, LocalDateTime.now());
        listaWizyt.add(wizyta);
        wizyta.rozpocznijWizyte();
        return wizyta;
    }

    // Przeciazenie: diagnoza bez zalecen
    public Diagnoza wystawDiagnoze(Wizyta wizyta, String opis, String kodICD) {
        Diagnoza diagnoza = new Diagnoza(opis, kodICD);
        wizyta.zakonczWizyte(diagnoza);
        return diagnoza;
    }

    // Przeciazenie: diagnoza z zaleceniami
    public Diagnoza wystawDiagnoze(Wizyta wizyta, String opis, String kodICD, String zalecenia) {
        Diagnoza diagnoza = new Diagnoza(opis, kodICD, zalecenia);
        wizyta.zakonczWizyte(diagnoza);
        return diagnoza;
    }

    public void dodajWizyte(Wizyta wizyta) {
        listaWizyt.add(wizyta);
    }

    public void ustawDostepnosc(boolean stan) {
        this.dostepny = stan;
    }

    public String wyswietlHarmonogram() {
        if (listaWizyt.isEmpty()) {
            return "Brak zaplanowanych wizyt dla lekarza " + imie + " " + nazwisko + ".";
        }
        StringBuilder sb = new StringBuilder("Harmonogram: " + imie + " " + nazwisko + "\n");
        for (Wizyta w : listaWizyt) {
            sb.append("  ").append(w.toString()).append("\n");
        }
        return sb.toString();
    }

    public String pobierzNumerPWZ() { return numerPWZ; }
    public boolean czyDostepny()    { return dostepny; }

    @Override
    public String toString() {
        return "Dr " + imie + " " + nazwisko + " [" + specjalizacja + "]";
    }
}