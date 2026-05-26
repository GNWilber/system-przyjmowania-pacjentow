package szpital;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Wizyta {
    private String idWizyty;
    private Pacjent pacjent;
    private Lekarz lekarz;
    private LocalDateTime dataWizyty;
    private StatusWizyty status;
    private Diagnoza diagnoza;
    private String notatki;

    public Wizyta(String idWizyty, Pacjent pacjent, Lekarz lekarz, LocalDateTime dataWizyty) {
        this.idWizyty = idWizyty;
        this.pacjent = pacjent;
        this.lekarz = lekarz;
        this.dataWizyty = dataWizyty;
        this.status = StatusWizyty.ZAPLANOWANA;
        this.notatki = "";
    }

    public void zakonczWizyte(Diagnoza diagnoza) {
        this.diagnoza = diagnoza;
        this.status = StatusWizyty.ZAKONCZONA;
        pacjent.dodajWizyte(this);
    }

    public void anulujWizyte(String powod) {
        this.status = StatusWizyty.ODWOLANA;
        dodajNotatke("Powod odwolania: " + powod);
    }

    public void rozpocznijWizyte() {
        this.status = StatusWizyty.W_TRAKCIE;
    }

    public void dodajNotatke(String notatka) {
        if (notatki.isEmpty()) {
            notatki = notatka;
        } else {
            notatki = notatki + "\n" + notatka;
        }
    }

    public String wyswietlSzczegoly() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("=== WIZYTA ===\n");
        sb.append("ID: ").append(idWizyty).append("\n");
        sb.append("Pacjent: ").append(pacjent.toString()).append("\n");
        sb.append("Lekarz: ").append(lekarz.toString()).append("\n");
        sb.append("Data: ").append(dataWizyty.format(fmt)).append("\n");
        sb.append("Status: ").append(status).append("\n");
        if (diagnoza != null) {
            sb.append("Diagnoza: ").append(diagnoza.toString()).append("\n");
        }
        if (!notatki.isEmpty()) {
            sb.append("Notatki: ").append(notatki).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return idWizyty + " | " + pacjent.toString() + " | " + dataWizyty.format(fmt) + " | " + status;
    }

    public String pobierzIdWizyty() {
        return idWizyty;
    }

    public Pacjent pobierzPacjenta() {
        return pacjent;
    }

    public Lekarz pobierzLekarza() {
        return lekarz;
    }

    public LocalDateTime pobierzDate() {
        return dataWizyty;
    }

    public StatusWizyty pobierzStatus() {
        return status;
    }
}