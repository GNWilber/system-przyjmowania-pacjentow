# Dokumentacja

## Założenia i opis funkcjonalny

System zarządzania przychodnią lekarską uruchamiany jako aplikacja desktopowa (Java Swing). Przy starcie ładowane są przykładowe dane (lekarze, pielęgniarki, recepcjonista, 10 pacjentów, 5 oddziałów, kilka wizyt).

**Funkcjonalności:**
- Rejestracja pacjentów z grupą krwi, alergiami i chorobami przewlekłymi
- Wyszukiwanie pacjentów po PESEL lub imieniu i nazwisku
- Umawianie, prowadzenie i anulowanie wizyt lekarskich
- Wystawianie diagnoz z kodami ICD-10 i zaleceniami
- Zarządzanie personelem (dodawanie lekarzy, dezaktywacja pracowników)
- Kolejka oczekujących – dodawanie pacjentów i wywoływanie kolejnych
- Oddziały – przyjmowanie/wypisywanie pacjentów, przypisywanie personelu
- Eksport karty pacjenta do pliku `.txt`
- Raport dzienny ze statystykami systemu

---

## Diagram UML

### Klasy — zawartość pól

```
<<interface>>
IRaportowalny
---
+ generujRaport() : String
+ eksportujDoPliku(sciezka : String) : void
```

```
<<abstract>>
Osoba
---
# imie : String
# nazwisko : String
# pesel : String
# dataUrodzenia : LocalDate
---
+ Osoba(imie, nazwisko, pesel, dataUrodzenia)
+ wyswietlInfo() : String  {abstract}
+ obliczWiek() : int
+ peselPasuje(pesel : String) : boolean
+ pobierzImie() : String
+ pobierzNazwisko() : String
+ pobierzPesel() : String
+ toString() : String
```

```
<<abstract>>
Pracownik
---
# idPracownika : String
# specjalizacja : String
# aktywny : boolean
---
+ Pracownik(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja)
+ wykonajObowiazki() : void  {abstract}
+ wyswietlInfo() : String  {abstract}
+ dezaktywuj() : void
+ wyswietlKarte() : String
+ pobierzIdPracownika() : String
+ pobierzSpecjalizacje() : String
+ czyAktywny() : boolean
```

```
Lekarz
---
- numerPWZ : String
- listaWizyt : List<Wizyta>
- dostepny : boolean
---
+ Lekarz(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja, numerPWZ)
+ wykonajObowiazki() : void
+ wyswietlInfo() : String
+ przyjmijPacjenta(pacjent : Pacjent) : Wizyta
+ wystawDiagnoze(wizyta, opis, kodICD) : Diagnoza
+ wystawDiagnoze(wizyta, opis, kodICD, zalecenia) : Diagnoza
+ dodajWizyte(wizyta : Wizyta) : void
+ ustawDostepnosc(stan : boolean) : void
+ wyswietlHarmonogram() : String
+ pobierzNumerPWZ() : String
+ czyDostepny() : boolean
+ toString() : String
```

```
Pielegniarka
---
- oddzial : Oddzial
- numerSali : int
---
+ Pielegniarka(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja, numerSali)
+ wykonajObowiazki() : void
+ wyswietlInfo() : String
+ przydzielLozko(pacjent : Pacjent, numerLozka : int) : void
+ zaopiekujSie(pacjent : Pacjent) : void
+ zmienOddzial(oddzial : Oddzial) : void
+ pobierzNumerSali() : int
+ toString() : String
```

```
Recepcjonista
---
- numerStanowiska : int
- zarejestrowanychDzisiaj : int
---
+ Recepcjonista(imie, nazwisko, pesel, dataUrodzenia, idPracownika, numerStanowiska)
+ wykonajObowiazki() : void
+ wyswietlInfo() : String
+ zarejestrujPacjenta(pacjent : Pacjent) : String
+ zarejestrujPacjenta(pacjent : Pacjent, priorytet : boolean) : String
+ wyswietlStatystyki() : String
+ pobierzNumerStanowiska() : int
+ toString() : String
```

```
Pacjent
---
- licznik : int  {static}
- idPacjenta : String
- grupaKrwi : String
- kartaPacjenta : KartaPacjenta
- wPoczekalni : boolean
---
+ Pacjent(imie, nazwisko, pesel, dataUrodzenia, grupaKrwi)
+ wyswietlInfo() : String
+ generujRaport() : String
+ eksportujDoPliku(sciezka : String) : void
+ dodajWizyte(wizyta : Wizyta) : void
+ dodajAlergie(alergia : String) : void
+ dodajChorobe(choroba : String) : void
+ zaznaczWPoczekalni(stan : boolean) : void
+ wyswietlHistorieWizyt() : String
+ pobierzIdPacjenta() : String
+ pobierzGrupeKrwi() : String
+ czyWPoczekalni() : boolean
+ toString() : String
```

```
KartaPacjenta
---
- idKarty : String
- historiaWizyt : List<Wizyta>
- alergie : List<String>
- chorobyPrzewlekle : List<String>
- dataZalozenia : LocalDate
---
+ KartaPacjenta(idKarty : String)
+ dodajWizyte(wizyta : Wizyta) : void
+ dodajAlergie(alergia : String) : void
+ dodajChorobe(choroba : String) : void
+ generujHistorie() : String
+ wyswietlAlergie() : String
+ wyswietlChoroby() : String
+ liczbaWizyt() : int
```

```
Wizyta
---
- licznik : int  {static}
- idWizyty : String
- pacjent : Pacjent
- lekarz : Lekarz
- dataWizyty : LocalDateTime
- status : StatusWizyty
- diagnoza : Diagnoza
- notatki : String
---
+ Wizyta(pacjent, lekarz, dataWizyty)
+ rozpocznijWizyte() : void
+ zakonczWizyte(diagnoza : Diagnoza) : void
+ anulujWizyte(powod : String) : void
+ dodajNotatke(notatka : String) : void
+ wyswietlSzczegoly() : String
+ pobierzIdWizyty() : String
+ pobierzPacjenta() : Pacjent
+ pobierzLekarza() : Lekarz
+ pobierzDate() : LocalDateTime
+ pobierzStatus() : StatusWizyty
+ toString() : String
```

```
Diagnoza
---
- licznik : int  {static}
- idDiagnozy : String
- opis : String
- kodICD : String
- zalecenia : String
- dataWystawienia : LocalDate
---
+ Diagnoza(opis, kodICD)
+ Diagnoza(opis, kodICD, zalecenia)
+ wyswietlDiagnoze() : String
+ czyPilna() : boolean
+ toString() : String
```

```
Oddzial
---
- nazwaOddzialu : String
- numerOddzialu : String
- maxLozek : int
- pracownicy : List<Pracownik>
- pacjenci : List<Pacjent>
---
+ Oddzial(nazwaOddzialu, numerOddzialu, maxLozek)
+ przyjmijPacjenta(pacjent : Pacjent) : boolean
+ wypiszPacjenta(pacjent : Pacjent) : boolean
+ dodajPracownika(pracownik : Pracownik) : void
+ usunPracownika(pracownik : Pracownik) : void
+ dostepneLozka() : int
+ wyswietlStatus() : String
+ wyswietlPracownikow() : String
+ pobierzNazwe() : String
+ pobierzNumer() : String
+ pobierzMaxLozek() : int
+ pobierzPacjentow() : List<Pacjent>
+ pobierzPracownikow() : List<Pracownik>
+ toString() : String
```

```
SystemPrzyjec
---
- instancja : SystemPrzyjec  {static}
- listaPacjentow : List<Pacjent>
- listaPracownikow : List<Pracownik>
- listaOddzialow : List<Oddzial>
- listaWszystkichWizyt : List<Wizyta>
- kolejkaOczekujacych : Queue<Pacjent>
---
- SystemPrzyjec()
+ getInstance() : SystemPrzyjec  {static}
+ zarejestrujPacjenta(pacjent : Pacjent) : void
+ wyszukajPacjenta(pesel : String) : Pacjent
+ wyszukajPacjenta(imie : String, nazwisko : String) : List<Pacjent>
+ umowWizyte(pacjent, lekarz, data) : Wizyta
+ dodajDoKolejki(pacjent : Pacjent) : void
+ wywolajNastepnego() : Pacjent
+ dodajOddzial(oddzial : Oddzial) : void
+ dodajPracownika(pracownik : Pracownik) : void
+ generujRaportDzienny() : String
+ wyswietlKolejke() : String
+ pobierzListePacjentow() : List<Pacjent>
+ pobierzListePracownikow() : List<Pracownik>
+ pobierzListeOddzialow() : List<Oddzial>
+ pobierzListeWizyt() : List<Wizyta>
+ pobierzKolejke() : Queue<Pacjent>
```

```
<<enum>>
StatusWizyty
---
ZAPLANOWANA
W_TRAKCIE
ZAKONCZONA
ODWOLANA
```

```
GlowneOkno
---
- system : SystemPrzyjec
- tabbedPane : JTabbedPane
- modelTabeliPacjentow : DefaultTableModel
- modelTabeliWizyt : DefaultTableModel
- modelTabeliPersonelu : DefaultTableModel
- modelTabeliOddzialow : DefaultTableModel
- cbPacjenciWizyt : JComboBox<Pacjent>
- cbLekarzWizyt : JComboBox<Lekarz>
---
+ GlowneOkno()
- zaladujDanePrzykladowe() : void
- inicjalizujKomponenty() : void
- inicjalizujMenu() : void
- zbudujPanelPacjentow() : JPanel
- zbudujPanelWizyt() : JPanel
- zbudujPanelPersonelu() : JPanel
- zbudujPanelKolejki() : JPanel
- zbudujPanelOddzialow() : JPanel
- odswiezKomboWizyt() : void
- odswiezTabelePacjentow() : void
- odswiezTabeleWizyt() : void
- odswiezTabelePersonelu() : void
- odswiezTabeleOddzialow() : void
- odswiezPanelKolejki() : void
+ main(args : String[]) : void  {static}
```

### Połączenia

```
Osoba          <|--  Pacjent          (dziedziczenie)
Osoba          <|--  Pracownik        (dziedziczenie)
Pracownik      <|--  Lekarz           (dziedziczenie)
Pracownik      <|--  Pielegniarka     (dziedziczenie)
Pracownik      <|--  Recepcjonista    (dziedziczenie)
Pacjent        ..|>  IRaportowalny    (realizacja interfejsu)
Pacjent        *--   KartaPacjenta    (kompozycja: Pacjent posiada KartaPacjenta)
Wizyta         o--   Pacjent          (asocjacja)
Wizyta         o--   Lekarz           (asocjacja)
Wizyta         o--   Diagnoza         (asocjacja)
Wizyta         --    StatusWizyty     (użycie enuma)
KartaPacjenta  o--   Wizyta           (agregacja: lista wizyt)
Oddzial        o--   Pracownik        (agregacja: lista pracowników)
Oddzial        o--   Pacjent          (agregacja: lista pacjentów)
Lekarz         o--   Wizyta           (agregacja: lista wizyt lekarza)
SystemPrzyjec  o--   Pacjent          (agregacja)
SystemPrzyjec  o--   Pracownik        (agregacja)
SystemPrzyjec  o--   Oddzial          (agregacja)
SystemPrzyjec  o--   Wizyta           (agregacja)
GlowneOkno     o--   SystemPrzyjec    (asocjacja: singleton)
```