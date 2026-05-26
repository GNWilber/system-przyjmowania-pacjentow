# 📄 Dokumentacja – Etap I: Diagram UML

---

## 1) Spis narzędzi użytych przy tworzeniu projektu

| Narzędzie | Przeznaczenie |
|---|---|
| draw.io (diagrams.net) | Tworzenie diagramu klas UML |
| Java SE | Docelowe środowisko implementacji |
| IntelliJ IDEA | Planowane IDE do implementacji |
| GitHub | Hosting repozytorium i kontrola wersji |

---

## 2) Założenia i opis funkcjonalny programu

### Cel systemu

System Przyjmowania Pacjentów w Szpitalu to aplikacja desktopowa napisana w języku Java, wspierająca pracę personelu medycznego w zakresie rejestracji, obsługi i zarządzania pacjentami. System umożliwia kompleksową obsługę procesu – od momentu zgłoszenia się pacjenta na recepcji, przez przydzielenie go do lekarza, aż po wystawienie diagnozy i zamknięcie wizyty.

### Główne funkcjonalności

- **Rejestracja pacjentów** – wprowadzanie danych osobowych, grupy krwi, historii chorób i alergii; wyszukiwanie pacjentów po numerze PESEL lub imieniu i nazwisku.
- **Zarządzanie kolejką oczekujących** – dodawanie pacjentów do kolejki, obsługa trybu priorytetowego, wywoływanie kolejnego pacjenta.
- **Obsługa wizyt lekarskich** – umawianie wizyt, zmiana statusu wizyty (`ZAPLANOWANA → W_TRAKCIE → ZAKONCZONA / ODWOLANA`), dodawanie notatek.
- **Wystawianie diagnoz** – lekarz wystawia diagnozę z kodem ICD-10 i opcjonalnymi zaleceniami; diagnoza jest przypisywana do wizyty i zapisywana w karcie pacjenta.
- **Karta pacjenta** – każdy pacjent posiada indywidualną kartę przechowującą pełną historię wizyt, alergie i choroby przewlekłe.
- **Zarządzanie oddziałami** – przydzielanie pacjentów do oddziałów, zarządzanie łóżkami, przypisywanie personelu do oddziału.
- **Zarządzanie personelem** – obsługa danych lekarzy, pielęgniarek i recepcjonistów; dezaktywacja pracowników; przegląd harmonogramów.
- **Raportowanie** – generowanie raportu dziennego przez system oraz eksport danych pacjenta do pliku tekstowego.
- **Graficzny interfejs użytkownika (GUI)** – okno z zakładkami (JTabbedPane) oddzielającymi moduły: Pacjenci, Wizyty, Personel, Kolejka, Oddziały.

### Użytkownicy systemu

| Rola | Uprawnienia |
|---|---|
| Recepcjonista | Rejestracja pacjentów, zarządzanie kolejką |
| Lekarz | Prowadzenie wizyt, wystawianie diagnoz |
| Pielęgniarka | Przydzielanie łóżek, opieka nad pacjentem |
| Administrator (dev) | Pełny dostęp przez `SystemPrzyjec` |

### Założenia projektowe

- Brak metod `get()` / `set()` – dane są hermetycznie ukryte; dostęp odbywa się wyłącznie przez metody biznesowe.
- Klasy `Osoba` i `Pracownik` są abstrakcyjne – nie można tworzyć ich instancji bezpośrednio.
- `SystemPrzyjec` działa jako Singleton – istnieje dokładnie jedna instancja systemu w trakcie działania aplikacji.
- Polimorfizm realizowany jest przez nadpisanie metod `wyswietlInfo()` oraz `wykonajObowiazki()` w każdej klasie pochodnej.
- Interfejs `IRaportowalny` wymusza ujednolicony sposób raportowania dla klas, które go implementują.

---

## 3) Klasy

### a) Diagram UML własnych klas

Diagram klas został wykonany w narzędziu draw.io zgodnie z notacją UML.
Poniżej opisano wszystkie elementy diagramu.

---

#### Legenda notacji

| Symbol | Znaczenie |
|---|---|
| `+` | składowa publiczna (`public`) |
| `-` | składowa prywatna (`private`) |
| `#` | składowa chroniona (`protected`) |
| `<<abstract>>` | klasa abstrakcyjna |
| `<<interface>>` | interfejs |
| `<<enum>>` | typ wyliczeniowy |
| →▷ (pusta strzałka) | uogólnienie / dziedziczenie |
| --▷ (przerywana pusta) | realizacja interfejsu |
| ◆— (romb wypełniony) | kompozycja |
| --→ (przerywana pełna) | zależność |

---

#### Wykaz klas i ich składowych

**`<<interface>> IRaportowalny`**
Interfejs wymuszający kontrakt raportowania na klasach implementujących.

| Składowa | Typ | Opis |
|---|---|---|
| `+ generujRaport()` | `String` | Zwraca raport tekstowy obiektu |
| `+ eksportujDoPliku(sciezka: String)` | `void` | Eksportuje raport do pliku |

---

**`<<abstract>> Osoba`**
Abstrakcyjna klasa bazowa dla wszystkich osób w systemie. Przechowuje dane osobowe wspólne dla pacjentów i pracowników.

| Składowa | Typ | Opis |
|---|---|---|
| `# imie` | `String` | Imię osoby |
| `# nazwisko` | `String` | Nazwisko osoby |
| `# pesel` | `String` | Numer PESEL |
| `# dataUrodzenia` | `LocalDate` | Data urodzenia |
| `+ Osoba(imie, nazwisko, pesel, dataUrodzenia)` | konstruktor | Inicjalizuje dane osobowe |
| `+ wyswietlInfo()` | `String` *(abstract)* | Zwraca opis osoby – implementacja w podklasach |
| `+ toString()` | `String` | Tekstowa reprezentacja obiektu |
| `+ peselPasuje(pesel: String)` | `boolean` | Sprawdza zgodność PESEL |
| `+ obliczWiek()` | `int` | Oblicza wiek na podstawie daty urodzenia |

---

**`<<abstract>> Pracownik`** *(dziedziczy po `Osoba`)*
Abstrakcyjna klasa reprezentująca każdego pracownika szpitala. Rozszerza `Osoba` o dane służbowe.

| Składowa | Typ | Opis |
|---|---|---|
| `# idPracownika` | `String` | Unikalny identyfikator pracownika |
| `# specjalizacja` | `String` | Specjalizacja / stanowisko |
| `# aktywny` | `boolean` | Czy pracownik jest aktywny w systemie |
| `+ Pracownik(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja)` | konstruktor | Inicjalizuje dane pracownika |
| `+ wykonajObowiazki()` | `void` *(abstract)* | Polimorficzne wykonanie obowiązków |
| `+ wyswietlInfo()` | `String` *(abstract)* | Polimorficzny opis pracownika |
| `+ wyswietlKarte()` | `String` | Zwraca kartę służbową pracownika |
| `+ dezaktywuj()` | `void` | Oznacza pracownika jako nieaktywnego |

---

**`Pacjent`** *(dziedziczy po `Osoba`, implementuje `IRaportowalny`)*
Klasa reprezentująca pacjenta szpitala. Posiada kartę pacjenta (kompozycja).

| Składowa | Typ | Opis |
|---|---|---|
| `- idPacjenta` | `String` | Unikalny identyfikator pacjenta |
| `- grupaKrwi` | `String` | Grupa krwi pacjenta |
| `- kartaPacjenta` | `KartaPacjenta` | Karta z historią wizyt (kompozycja) |
| `- wPoczekalni` | `boolean` | Czy pacjent aktualnie czeka |
| `+ Pacjent(imie, nazwisko, pesel, dataUrodzenia, grupaKrwi)` | konstruktor | Inicjalizuje pacjenta i tworzy jego kartę |
| `+ wyswietlInfo()` | `String` | Nadpisana – zwraca dane pacjenta |
| `+ generujRaport()` | `String` | Implementacja z `IRaportowalny` |
| `+ eksportujDoPliku(sciezka: String)` | `void` | Implementacja z `IRaportowalny` |
| `+ dodajWizyte(wizyta: Wizyta)` | `void` | Dodaje wizytę do karty |
| `+ wyswietlHistorieWizyt()` | `String` | Deleguje do `KartaPacjenta` |
| `+ zaznaczWPoczekalni(stan: boolean)` | `void` | Zmienia status oczekiwania |

---

**`Lekarz`** *(dziedziczy po `Pracownik`)*
Klasa reprezentująca lekarza. Umożliwia prowadzenie wizyt i wystawianie diagnoz (z przeciążeniem metody).

| Składowa | Typ | Opis |
|---|---|---|
| `- numerPWZ` | `String` | Numer prawa wykonywania zawodu |
| `- listaWizyt` | `List<Wizyta>` | Wizyty przypisane do lekarza |
| `- dostepny` | `boolean` | Czy lekarz jest dostępny |
| `+ Lekarz(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja, numerPWZ)` | konstruktor | Inicjalizuje lekarza |
| `+ wykonajObowiazki()` | `void` | Nadpisana – lekarz przyjmuje pacjentów |
| `+ wyswietlInfo()` | `String` | Nadpisana – dane lekarza + PWZ |
| `+ przyjmijPacjenta(pacjent: Pacjent)` | `Wizyta` | Rozpoczyna wizytę z pacjentem |
| `+ wystawDiagnoze(wizyta, opis, kodICD)` | `Diagnoza` | Wystawia diagnozę bez zaleceń |
| `+ wystawDiagnoze(wizyta, opis, kodICD, zalecenia)` | `Diagnoza` | **Przeciążona** – z zaleceniami |
| `+ ustawDostepnosc(stan: boolean)` | `void` | Zmienia dostępność lekarza |
| `+ wyswietlHarmonogram()` | `String` | Zwraca listę zaplanowanych wizyt |

---

**`Pielegniarka`** *(dziedziczy po `Pracownik`)*
Klasa reprezentująca pielęgniarkę przypisaną do oddziału.

| Składowa | Typ | Opis |
|---|---|---|
| `- oddzial` | `Oddzial` | Przypisany oddział |
| `- numerSali` | `int` | Numer sali dyżurowej |
| `+ Pielegniarka(imie, nazwisko, pesel, dataUrodzenia, idPracownika, specjalizacja, numerSali)` | konstruktor | Inicjalizuje pielęgniarkę |
| `+ wykonajObowiazki()` | `void` | Nadpisana – pielęgniarka opiekuje się pacjentami |
| `+ wyswietlInfo()` | `String` | Nadpisana – dane pielęgniarki + sala |
| `+ przydzielLozko(pacjent, numerLozka)` | `void` | Przydziela łóżko pacjentowi |
| `+ zaopiekujSie(pacjent: Pacjent)` | `void` | Rejestruje opiekę nad pacjentem |
| `+ zmienOddzial(oddzial: Oddzial)` | `void` | Przenosi na inny oddział |

---

**`Recepcjonista`** *(dziedziczy po `Pracownik`)*
Klasa obsługująca rejestrację pacjentów i kolejkę. Posiada przeciążoną metodę rejestracji.

| Składowa | Typ | Opis |
|---|---|---|
| `- numerStanowiska` | `int` | Numer stanowiska rejestracji |
| `- zarejestrowanychDzisiaj` | `int` | Licznik dziennych rejestracji |
| `+ Recepcjonista(imie, nazwisko, pesel, dataUrodzenia, idPracownika, numerStanowiska)` | konstruktor | Inicjalizuje recepcjonistę |
| `+ wykonajObowiazki()` | `void` | Nadpisana – recepcjonista rejestruje pacjentów |
| `+ wyswietlInfo()` | `String` | Nadpisana – dane + numer stanowiska |
| `+ zarejestrujPacjenta(pacjent: Pacjent)` | `String` | Standardowa rejestracja |
| `+ zarejestrujPacjenta(pacjent, priorytet: boolean)` | `String` | **Przeciążona** – z trybem priorytetowym |
| `+ wyswietlStatystyki()` | `String` | Zwraca statystyki stanowiska |

---

**`Wizyta`**
Klasa reprezentująca pojedynczą wizytę lekarską. Łączy pacjenta z lekarzem i przechowuje diagnozę.

| Składowa | Typ | Opis |
|---|---|---|
| `- idWizyty` | `String` | Unikalny identyfikator wizyty |
| `- pacjent` | `Pacjent` | Pacjent uczestniczący w wizycie |
| `- lekarz` | `Lekarz` | Lekarz prowadzący wizytę |
| `- dataWizyty` | `LocalDateTime` | Data i godzina wizyty |
| `- status` | `StatusWizyty` | Aktualny status wizyty (enum) |
| `- diagnoza` | `Diagnoza` | Diagnoza wystawiona podczas wizyty |
| `- notatki` | `String` | Notatki lekarza |
| `+ Wizyta(idWizyty, pacjent, lekarz, dataWizyty)` | konstruktor | Tworzy wizytę ze statusem ZAPLANOWANA |
| `+ zakonczWizyte(diagnoza: Diagnoza)` | `void` | Zamyka wizytę, zapisuje diagnozę |
| `+ anulujWizyte(powod: String)` | `void` | Odwołuje wizytę |
| `+ rozpocznijWizyte()` | `void` | Zmienia status na W_TRAKCIE |
| `+ dodajNotatke(notatka: String)` | `void` | Dopisuje notatkę do wizyty |
| `+ wyswietlSzczegoly()` | `String` | Pełny opis wizyty |
| `+ toString()` | `String` | Skrócony opis wizyty |

---

**`Diagnoza`**
Klasa przechowująca wynik diagnostyczny wizyty. Konstruktor jest przeciążony.

| Składowa | Typ | Opis |
|---|---|---|
| `- idDiagnozy` | `String` | Unikalny identyfikator diagnozy |
| `- opis` | `String` | Opis słowny diagnozy |
| `- kodICD` | `String` | Kod ICD-10 schorzenia |
| `- zalecenia` | `String` | Zalecenia lekarskie (opcjonalne) |
| `- dataWystawienia` | `LocalDate` | Data wystawienia |
| `+ Diagnoza(idDiagnozy, opis, kodICD)` | konstruktor | Diagnoza bez zaleceń |
| `+ Diagnoza(idDiagnozy, opis, kodICD, zalecenia)` | **konstruktor przeciążony** | Diagnoza z zaleceniami |
| `+ wyswietlDiagnoze()` | `String` | Pełny opis diagnozy |
| `+ toString()` | `String` | Skrócony opis |
| `+ czyPilna()` | `boolean` | Sprawdza czy diagnoza wymaga pilnej interwencji |

---

**`KartaPacjenta`**
Klasa przechowująca kompletną dokumentację medyczną pacjenta. Tworzona wyłącznie przez `Pacjent` (kompozycja).

| Składowa | Typ | Opis |
|---|---|---|
| `- idKarty` | `String` | Identyfikator karty |
| `- historiaWizyt` | `List<Wizyta>` | Lista wszystkich wizyt |
| `- alergie` | `List<String>` | Lista alergii pacjenta |
| `- chorobyPrzewlekle` | `List<String>` | Lista chorób przewlekłych |
| `- dataZalozenia` | `LocalDate` | Data założenia karty |
| `+ KartaPacjenta(idKarty: String)` | konstruktor | Tworzy pustą kartę |
| `+ dodajWizyte(wizyta: Wizyta)` | `void` | Dodaje wizytę do historii |
| `+ dodajAlergie(alergia: String)` | `void` | Rejestruje alergię |
| `+ dodajChorobe(choroba: String)` | `void` | Rejestruje chorobę przewlekłą |
| `+ generujHistorie()` | `String` | Zwraca pełną historię wizyt |
| `+ wyswietlAlergie()` | `String` | Zwraca listę alergii |
| `+ wyswietlChoroby()` | `String` | Zwraca listę chorób |
| `+ liczbaWizyt()` | `int` | Zwraca liczbę odbytych wizyt |

---

**`Oddzial`**
Klasa reprezentująca oddział szpitalny. Zarządza przydzielonymi pacjentami i personelem.

| Składowa | Typ | Opis |
|---|---|---|
| `- nazwaOddzialu` | `String` | Nazwa oddziału |
| `- numerOddzialu` | `String` | Numer/kod oddziału |
| `- maxLozek` | `int` | Maksymalna liczba łóżek |
| `- pracownicy` | `List<Pracownik>` | Lista pracowników oddziału |
| `- pacjenci` | `List<Pacjent>` | Lista pacjentów na oddziale |
| `+ Oddzial(nazwaOddzialu, numerOddzialu, maxLozek)` | konstruktor | Inicjalizuje oddział |
| `+ przyjmijPacjenta(pacjent: Pacjent)` | `boolean` | Przyjmuje jeśli są wolne łóżka |
| `+ wypiszPacjenta(pacjent: Pacjent)` | `boolean` | Wypisuje pacjenta z oddziału |
| `+ dodajPracownika(pracownik: Pracownik)` | `void` | Przydziela pracownika |
| `+ usunPracownika(pracownik: Pracownik)` | `void` | Usuwa pracownika z oddziału |
| `+ wyswietlStatus()` | `String` | Raport stanu oddziału |
| `+ dostepneLozka()` | `int` | Zwraca liczbę wolnych łóżek |
| `+ wyswietlPracownikow()` | `String` | Lista personelu oddziału |

---

**`SystemPrzyjec`** *(Singleton)*
Centralna klasa kontrolera systemu. Zarządza wszystkimi obiektami w aplikacji.

| Składowa | Typ | Opis |
|---|---|---|
| `- instancja` | `SystemPrzyjec` *(static)* | Jedyna instancja Singletona |
| `- listaPacjentow` | `List<Pacjent>` | Wszyscy zarejestrowani pacjenci |
| `- listaPracownikow` | `List<Pracownik>` | Wszyscy pracownicy |
| `- listaOddzialow` | `List<Oddzial>` | Wszystkie oddziały |
| `- kolejkaOczekujacych` | `Queue<Pacjent>` | Kolejka pacjentów w poczekalni |
| `+ getInstance()` | `SystemPrzyjec` *(static)* | Zwraca lub tworzy instancję |
| `+ zarejestrujPacjenta(pacjent: Pacjent)` | `void` | Dodaje pacjenta do systemu |
| `+ wyszukajPacjenta(pesel: String)` | `Pacjent` | Wyszukuje po PESEL |
| `+ wyszukajPacjenta(imie, nazwisko)` | `List<Pacjent>` | **Przeciążona** – wyszukuje po nazwisku |
| `+ umowWizyte(pacjent, lekarz, data)` | `Wizyta` | Tworzy i rejestruje wizytę |
| `+ dodajDoKolejki(pacjent: Pacjent)` | `void` | Dodaje do kolejki oczekujących |
| `+ wywolajNastepnego()` | `Pacjent` | Pobiera pierwszego z kolejki |
| `+ dodajOddzial(oddzial: Oddzial)` | `void` | Rejestruje oddział w systemie |
| `+ generujRaportDzienny()` | `String` | Zestawienie aktywności z danego dnia |
| `+ wyswietlKolejke()` | `String` | Lista oczekujących pacjentów |
| `+ wyswietlWszystkichPacjentow()` | `void` | Wyświetla wszystkich pacjentów |

---

**`<<enum>> StatusWizyty`**
Typ wyliczeniowy określający możliwe stany wizyty.

| Wartość | Opis |
|---|---|
| `ZAPLANOWANA` | Wizyta zaplanowana, jeszcze się nie rozpoczęła |
| `W_TRAKCIE` | Wizyta trwa |
| `ZAKONCZONA` | Wizyta zakończona z diagnozą |
| `ODWOLANA` | Wizyta odwołana |

---

**`GlowneOkno`** *(GUI – rozszerza `JFrame`)*
Główne okno aplikacji z graficznym interfejsem użytkownika opartym na zakładkach.

| Składowa | Typ | Opis |
|---|---|---|
| `- system` | `SystemPrzyjec` | Referencja do systemu (Singleton) |
| `- tabbedPane` | `JTabbedPane` | Kontener zakładek |
| `- panelPacjentow` | `JPanel` | Zakładka zarządzania pacjentami |
| `- panelWizyt` | `JPanel` | Zakładka wizyt |
| `- panelLekarzy` | `JPanel` | Zakładka personelu |
| `- panelKolejki` | `JPanel` | Zakładka kolejki oczekujących |
| `+ GlowneOkno()` | konstruktor | Inicjalizuje okno i komponenty |
| `+ inicjalizujKomponenty()` | `void` | Buduje strukturę GUI |
| `+ inicjalizujMenu()` | `void` | Tworzy pasek menu |
| `+ pokazPanelPacjentow()` | `void` | Przełącza na panel pacjentów |
| `+ pokazPanelWizyt()` | `void` | Przełącza na panel wizyt |
| `+ pokazPanelKolejki()` | `void` | Przełącza na panel kolejki |
| `+ odswiezWidok()` | `void` | Odświeża dane w aktywnym panelu |
| `+ main(args: String[])` | `void` *(static)* | Punkt startowy aplikacji |

---

#### Zestawienie powiązań na diagramie

| Typ powiązania | Od | Do | Opis |
|---|---|---|---|
| Uogólnienie | `Pracownik` | `Osoba` | Pracownik jest Osobą |
| Uogólnienie | `Pacjent` | `Osoba` | Pacjent jest Osobą |
| Uogólnienie | `Lekarz` | `Pracownik` | Lekarz jest Pracownikiem |
| Uogólnienie | `Pielegniarka` | `Pracownik` | Pielęgniarka jest Pracownikiem |
| Uogólnienie | `Recepcjonista` | `Pracownik` | Recepcjonista jest Pracownikiem |
| Realizacja | `Pacjent` | `IRaportowalny` | Pacjent implementuje raportowanie |
| Kompozycja | `Pacjent` | `KartaPacjenta` | Karta istnieje tylko wraz z pacjentem |
| Kompozycja | `Wizyta` | `Diagnoza` | Diagnoza jest częścią wizyty |
| Kompozycja | `Oddzial` | `Pacjent` | Pacjenci należą do oddziału |
| Kompozycja | `Oddzial` | `Pracownik` | Pracownicy należą do oddziału |
| Zależność | `Lekarz` | `Wizyta` | Lekarz tworzy wizyty |
| Zależność | `Lekarz` | `Diagnoza` | Lekarz wystawia diagnozy |
| Zależność | `Recepcjonista` | `Pacjent` | Recepcjonista rejestruje pacjentów |
| Zależność | `SystemPrzyjec` | `Wizyta` | System zarządza wizytami |
| Zależność | `SystemPrzyjec` | `Pacjent` | System zarządza pacjentami |
| Zależność | `SystemPrzyjec` | `Oddzial` | System zarządza oddziałami |
| Zależność | `GlowneOkno` | `SystemPrzyjec` | GUI korzysta z systemu |
| Zależność | `Wizyta` | `StatusWizyty` | Wizyta używa enuma statusu |

---

*Sekcje 3b, 4, 5, 6, 7, 8 – do uzupełnienia po etapie implementacji.*