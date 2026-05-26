package szpital;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KartaPacjenta {
    private String idKarty;
    private List<Wizyta> historiaWizyt;
    private List<String> alergie;
    private List<String> chorobyPrzewlekle;
    private LocalDate dataZalozenia;

    public KartaPacjenta(String idKarty) {
        this.idKarty = idKarty;
        this.historiaWizyt = new ArrayList<>();
        this.alergie = new ArrayList<>();
        this.chorobyPrzewlekle = new ArrayList<>();
        this.dataZalozenia = LocalDate.now();
    }

    public void dodajWizyte(Wizyta wizyta) {
        historiaWizyt.add(wizyta);
    }

    public void dodajAlergie(String alergia) {
        alergie.add(alergia);
    }

    public void dodajChorobe(String choroba) {
        chorobyPrzewlekle.add(choroba);
    }

    public String generujHistorie() {
        if (historiaWizyt.isEmpty()) {
            return "Brak wizyt w historii.\n";
        }
        StringBuilder sb = new StringBuilder("Historia wizyt:\n");
        for (Wizyta w : historiaWizyt) {
            sb.append("  ").append(w.toString()).append("\n");
        }
        return sb.toString();
    }

    public String wyswietlAlergie() {
        if (alergie.isEmpty()) {
            return "Brak zarejestrowanych alergii.";
        }
        StringBuilder sb = new StringBuilder("Alergie:\n");
        for (String a : alergie) {
            sb.append("  - ").append(a).append("\n");
        }
        return sb.toString();
    }

    public String wyswietlChoroby() {
        if (chorobyPrzewlekle.isEmpty()) {
            return "Brak chorob przewleklych.";
        }
        StringBuilder sb = new StringBuilder("Choroby przewlekle:\n");
        for (String c : chorobyPrzewlekle) {
            sb.append("  - ").append(c).append("\n");
        }
        return sb.toString();
    }

    public int liczbaWizyt() {
        return historiaWizyt.size();
    }
}