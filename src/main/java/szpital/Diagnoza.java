package szpital;

import java.time.LocalDate;

public class Diagnoza {
    private static int licznik = 0;

    private String idDiagnozy;
    private String opis;
    private String kodICD;
    private String zalecenia;
    private LocalDate dataWystawienia;

    // Konstruktor bez zalecen
    public Diagnoza(String opis, String kodICD) {
        this.idDiagnozy = "D" + String.format("%04d", ++licznik);
        this.opis = opis;
        this.kodICD = kodICD;
        this.zalecenia = "";
        this.dataWystawienia = LocalDate.now();
    }

    // Konstruktor z zaleceniami
    public Diagnoza(String opis, String kodICD, String zalecenia) {
        this(opis, kodICD);
        this.zalecenia = zalecenia;
    }

    public String wyswietlDiagnoze() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID Diagnozy: ").append(idDiagnozy).append("\n");
        sb.append("Opis: ").append(opis).append("\n");
        sb.append("Kod ICD-10: ").append(kodICD).append("\n");
        sb.append("Data wystawienia: ").append(dataWystawienia).append("\n");
        if (!zalecenia.isEmpty()) sb.append("Zalecenia: ").append(zalecenia).append("\n");
        return sb.toString();
    }

    // Sprawdza czy diagnoza dotyczy stanow naglych na podstawie kodu ICD
    public boolean czyPilna() {
        String[] kodyNagle = {"I21", "I22", "I63", "I64", "J96", "R57", "T07", "S06"};
        for (String kod : kodyNagle) {
            if (kodICD.startsWith(kod)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return kodICD + ": " + opis;
    }
}