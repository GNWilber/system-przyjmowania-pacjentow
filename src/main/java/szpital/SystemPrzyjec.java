package szpital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        if (instancja == null) {
            instancja = new SystemPrzyjec();
        }
        return instancja;
    }

    public void zarejestrujPacjenta(Pacjent pacjent) {
        listaPacjentow.add(pacjent);
    }

    public Pacjent wyszukajPacjenta(String pesel) {
        for (Pacjent p : listaPacjentow) {
            if (p.peselPasuje(pesel)) {
                return p;
            }
        }
        return null;
    }

    public List<Pacjent> wyszukajPacjenta(String imie, String nazwisko) {
        List<Pacjent> wyniki = new ArrayList<>();
        String szukanyImie = imie.toLowerCase();
        String szukaneNazwisko = nazwisko.toLowerCase();
        for (Pacjent p : listaPacjentow) {
            boolean pasuje = p.pobierzImie().toLowerCase().contains(szukanyImie)
                    && p.pobierzNazwisko().toLowerCase().contains(szukaneNazwisko);
            if (pasuje) {
                wyniki.add(p);
            }
        }
        return wyniki;
    }

    public Wizyta umowWizyte(Pacjent pacjent, Lekarz lekarz, LocalDateTime data) {
        String idWizyty = "W" + System.currentTimeMillis();
        Wizyta wizyta = new Wizyta(idWizyty, pacjent, lekarz, data);
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
        if (pacjent != null) {
            pacjent.zaznaczWPoczekalni(false);
        }
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
        sb.append("Wizyty laczne: ").append(listaWszystkichWizyt.size()).append("\n");
        sb.append("W kolejce: ").append(kolejkaOczekujacych.size()).append("\n");
        return sb.toString();
    }

    public String wyswietlKolejke() {
        if (kolejkaOczekujacych.isEmpty()) {
            return "Kolejka oczekujacych jest pusta.";
        }
        StringBuilder sb = new StringBuilder("Kolejka oczekujacych:\n");
        int nr = 1;
        for (Pacjent p : kolejkaOczekujacych) {
            sb.append(nr++).append(". ").append(p.toString()).append("\n");
        }
        return sb.toString();
    }

    public void wyswietlWszystkichPacjentow() {
        if (listaPacjentow.isEmpty()) {
            System.out.println("Brak zarejestrowanych pacjentow.");
            return;
        }
        for (Pacjent p : listaPacjentow) {
            System.out.println(p.wyswietlInfo());
            System.out.println("---");
        }
    }

    public List<Pacjent> pobierzListePacjentow() {
        return listaPacjentow;
    }

    public List<Pracownik> pobierzListePracownikow() {
        return listaPracownikow;
    }

    public List<Oddzial> pobierzListeOddzialow() {
        return listaOddzialow;
    }

    public List<Wizyta> pobierzListeWizyt() {
        return listaWszystkichWizyt;
    }

    public Queue<Pacjent> pobierzKolejke() {
        return kolejkaOczekujacych;
    }
}