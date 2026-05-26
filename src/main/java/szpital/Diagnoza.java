package szpital;

import java.time.LocalDate;

public class Diagnoza {
    private String idDiagnozy;
    private String opis;
    private String kodICD;
    private String zalecenia;
    private LocalDate dataWystawienia;

    public Diagnoza(String idDiagnozy, String opis, String kodICD) {
        this.idDiagnozy = idDiagnozy;
        this.opis = opis;
        this.kodICD = kodICD;
        this.zalecenia = "";
        this.dataWystawienia = LocalDate.now();
    }

    public Diagnoza(String idDiagnozy, String opis, String kodICD, String zalecenia) {
        this(idDiagnozy, opis, kodICD);
        this.zalecenia = zalecenia;
    }

    public String wyswietlDiagnoze() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID Diagnozy: ").append(idDiagnozy).append("\n");
        sb.append("Opis: ").append(opis).append("\n");
        sb.append("Kod ICD-10: ").append(kodICD).append("\n");
        sb.append("Data wystawienia: ").append(dataWystawienia).append("\n");
        if (!zalecenia.isEmpty()) {
            sb.append("Zalecenia: ").append(zalecenia).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return kodICD + ": " + opis;
    }

    public boolean czyPilna() {
        String[] pilneCody = {"I21", "I22", "I63", "I64", "J96", "R57", "T07", "S06"};
        for (String kod : pilneCody) {
            if (kodICD.startsWith(kod)) {
                return true;
            }
        }
        return false;
    }
}