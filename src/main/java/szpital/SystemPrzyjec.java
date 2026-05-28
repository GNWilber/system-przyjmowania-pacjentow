package szpital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Singleton zarzadzajacy calym stanem systemu
public class SystemPrzyjec {
    private static SystemPrzyjec instancja;

    private List<Pacjent> listaPacjentow;
    private List<Pracownik> listaPracownikow;
    private List<Oddzial> listaOddzialow;
    private List<Wizyta> listaWszystkichWizyt;
    private Queue<Pacjent> kolejkaOczekujacych;

    private SystemPrzyjec() {
        listaPacjentow = new ArrayList<>();
        listaPracownikow = new ArrayList<>();
        listaOddzialow = new ArrayList<>();
        listaWszystkichWizyt = new ArrayList<>();
        kolejkaOczekujacych = new LinkedList<>();
    }

    public static SystemPrzyjec getInstance() {
        if (instancja == null) instancja = new SystemPrzyjec();
        return instancja;
    }

    public void zarejestrujPacjenta(Pacjent pacjent) {
        listaPacjentow.add(pacjent);
    }

    // Wyszukiwanie po PESEL
    public Pacjent wyszukajPacjenta(String pesel) {
        for (Pacjent p : listaPacjentow) {
            if (p.peselPasuje(pesel)) return p;
        }
        return null;
    }

    // Przeciazenie: wyszukiwanie po imieniu i nazwisku (zwraca liste pasujacych)
    public List<Pacjent> wyszukajPacjenta(String imie, String nazwisko) {
        List<Pacjent> wyniki = new ArrayList<>();
        for (Pacjent p : listaPacjentow) {
            if (p.pobierzImie().toLowerCase().contains(imie.toLowerCase()) &&
                p.pobierzNazwisko().toLowerCase().contains(nazwisko.toLowerCase())) {
                wyniki.add(p);
            }
        }
        return wyniki;
    }

    public Wizyta umowWizyte(Pacjent pacjent, Lekarz lekarz, LocalDateTime data) {
        Wizyta wizyta = new Wizyta(pacjent, lekarz, data);
        listaWszystkichWizyt.add(wizyta);
        lekarz.dodajWizyte(wizyta);
        return wizyta;
    }

    public void dodajDoKolejki(Pacjent pacjent) {
        pacjent.zaznaczWPoczekalni(true);
        kolejkaOczekujacych.offer(pacjent);
    }

    public Pacjent wywolajNastepnego() {
        Pacjent pacjent = kolejkaOczekujacych.poll();
        if (pacjent != null) pacjent.zaznaczWPoczekalni(false);
        return pacjent;
    }

    public void dodajOddzial(Oddzial oddzial) {
        listaOddzialow.add(oddzial);
    }

    public void dodajPracownika(Pracownik pracownik) {
        listaPracownikow.add(pracownik);
    }

    public String generujRaportDzienny() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPORT DZIENNY ===\n");
        sb.append("Data: ").append(LocalDate.now()).append("\n");
        sb.append("Pacjenci w systemie: ").append(listaPacjentow.size()).append("\n");
        sb.append("Pracownicy: ").append(listaPracownikow.size()).append("\n");
        sb.append("Oddzialy: ").append(listaOddzialow.size()).append("\n");
        sb.append("Wizyty lacznie: ").append(listaWszystkichWizyt.size()).append("\n");
        sb.append("W kolejce: ").append(kolejkaOczekujacych.size()).append("\n");
        return sb.toString();
    }

    public String wyswietlKolejke() {
        if (kolejkaOczekujacych.isEmpty()) return "Kolejka oczekujacych jest pusta.";
        StringBuilder sb = new StringBuilder("Kolejka oczekujacych:\n");
        int nr = 1;
        for (Pacjent p : kolejkaOczekujacych) {
            sb.append(nr++).append(". ").append(p).append("\n");
        }
        return sb.toString();
    }

    public List<Pacjent> pobierzListePacjentow()     { return listaPacjentow; }
    public List<Pracownik> pobierzListePracownikow() { return listaPracownikow; }
    public List<Oddzial> pobierzListeOddzialow()     { return listaOddzialow; }
    public List<Wizyta> pobierzListeWizyt()          { return listaWszystkichWizyt; }
    public Queue<Pacjent> pobierzKolejke()           { return kolejkaOczekujacych; }
}