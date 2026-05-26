package szpital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class GlowneOkno extends JFrame {

    private SystemPrzyjec system;
    private JTabbedPane tabbedPane;
    private JPanel panelPacjentow;
    private JPanel panelWizyt;
    private JPanel panelLekarzy;
    private JPanel panelKolejki;

    private DefaultTableModel modelTabeliPacjentow;
    private DefaultTableModel modelTabeliWizyt;
    private DefaultTableModel modelTabeliPersonelu;
    private DefaultTableModel modelTabeliOddzialow;

    private JComboBox<Pacjent> cbPacjenciWizyt;
    private JComboBox<Lekarz>  cbLekarzWizyt;

    public GlowneOkno() {
        system = SystemPrzyjec.getInstance();
        zaladujDanePrzykladowe();
        inicjalizujKomponenty();
        inicjalizujMenu();
        setTitle("System Przyjec Pacjentow - Szpital");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // -------------------------------------------------------------------------
    // DANE PRZYKLADOWE
    // -------------------------------------------------------------------------
    private void zaladujDanePrzykladowe() {
        // 5 lekarzy roznych specjalizacji
        Lekarz l1 = new Lekarz("Jan",       "Kowalski",   "70010112345", LocalDate.of(1970,  1,  1), "L001", "Kardiologia",   "PWZ100001");
        Lekarz l2 = new Lekarz("Anna",      "Nowak",      "80020223456", LocalDate.of(1980,  2, 14), "L002", "Neurologia",    "PWZ100002");
        Lekarz l3 = new Lekarz("Piotr",     "Wójcik",     "75030312347", LocalDate.of(1975,  3, 22), "L003", "Ortopedia",     "PWZ100003");
        Lekarz l4 = new Lekarz("Marta",     "Dabrowska",  "83040423458", LocalDate.of(1983,  4,  5), "L004", "Pediatria",     "PWZ100004");
        Lekarz l5 = new Lekarz("Tomasz",    "Lewandowski","68050534569", LocalDate.of(1968,  5, 30), "L005", "Chirurgia",     "PWZ100005");

        // 5 pielegniarek
        Pielegniarka p1 = new Pielegniarka("Maria",    "Wisniewska", "85030334567", LocalDate.of(1985,  3,  3), "P001", "Kardiologiczna", 101);
        Pielegniarka p2 = new Pielegniarka("Katarzyna","Wojciechowska","90060645671", LocalDate.of(1990, 6,  6), "P002", "Neurologiczna",  102);
        Pielegniarka p3 = new Pielegniarka("Joanna",   "Kaminska",   "88070756782", LocalDate.of(1988,  7, 18), "P003", "Ortopedyczna",   103);
        Pielegniarka p4 = new Pielegniarka("Barbara",  "Lewandowska","92080867893", LocalDate.of(1992,  8, 25), "P004", "Pediatryczna",   104);
        Pielegniarka p5 = new Pielegniarka("Dorota",   "Szymanska",  "86090978904", LocalDate.of(1986,  9, 11), "P005", "Chirurgiczna",   105);

        // 1 recepcjonistka
        Recepcjonista r1 = new Recepcjonista("Ewa", "Zielinska", "94110145678", LocalDate.of(1994, 11,  4), "R001", 1);

        for (Pracownik pr : new Pracownik[]{l1,l2,l3,l4,l5,p1,p2,p3,p4,p5,r1})
            system.dodajPracownika(pr);

        // 10 zroznicowanych pacjentow
        Pacjent pac1  = new Pacjent("Tomasz",    "Adamski",    "95050556789", LocalDate.of(1995,  5,  5), "A+");
        pac1.dodajAlergie("Penicylina"); pac1.dodajChorobe("Nadcisnienie");

        Pacjent pac2  = new Pacjent("Karolina",  "Brzezinska", "88060667890", LocalDate.of(1988,  6,  6), "B-");
        pac2.dodajChorobe("Migrena");

        Pacjent pac3  = new Pacjent("Marek",     "Czerwinski", "72030378901", LocalDate.of(1972,  3, 14), "0+");
        pac3.dodajAlergie("Ibuprofen"); pac3.dodajChorobe("Cukrzyca typu 2");

        Pacjent pac4  = new Pacjent("Aleksandra","Dabek",      "99120489012", LocalDate.of(1999, 12, 24), "AB+");
        pac4.dodajAlergie("Sulfonamidy");

        Pacjent pac5  = new Pacjent("Krzysztof", "Elk",        "60070590123", LocalDate.of(1960,  7,  7), "A-");
        pac5.dodajChorobe("Choroba Parkinsona"); pac5.dodajChorobe("Nadcisnienie");

        Pacjent pac6  = new Pacjent("Zofia",     "Frankowska", "03021601234", LocalDate.of(2003,  2, 16), "B+");

        Pacjent pac7  = new Pacjent("Rafal",     "Grabowski",  "85081712345", LocalDate.of(1985,  8, 17), "0-");
        pac7.dodajAlergie("Aspiryna"); pac7.dodajChorobe("Astma");

        Pacjent pac8  = new Pacjent("Iwona",     "Hermanska",  "78091823456", LocalDate.of(1978,  9, 18), "AB-");
        pac8.dodajChorobe("Niedoczynnosc tarczycy");

        Pacjent pac9  = new Pacjent("Stanislaw", "Ignatowicz", "55102934567", LocalDate.of(1955, 10, 29), "A+");
        pac9.dodajChorobe("Niewydolnosc serca"); pac9.dodajChorobe("Cukrzyca typu 2");

        Pacjent pac10 = new Pacjent("Natalia",   "Jasinska",   "01030545678", LocalDate.of(2001,  3,  5), "B+");
        pac10.dodajAlergie("Lateks");

        for (Pacjent p : new Pacjent[]{pac1,pac2,pac3,pac4,pac5,pac6,pac7,pac8,pac9,pac10})
            system.zarejestrujPacjenta(p);

        // Oddzialy
        Oddzial o1 = new Oddzial("Kardiologia",  "ODD-K1", 20);
        Oddzial o2 = new Oddzial("Neurologia",   "ODD-N1", 15);
        Oddzial o3 = new Oddzial("Ortopedia",    "ODD-O1", 12);
        Oddzial o4 = new Oddzial("Pediatria",    "ODD-P1", 10);
        Oddzial o5 = new Oddzial("Chirurgia",    "ODD-C1", 18);

        o1.dodajPracownika(l1); o1.dodajPracownika(p1); o1.przyjmijPacjenta(pac1); o1.przyjmijPacjenta(pac9);
        o2.dodajPracownika(l2); o2.dodajPracownika(p2); o2.przyjmijPacjenta(pac2); o2.przyjmijPacjenta(pac5);
        o3.dodajPracownika(l3); o3.dodajPracownika(p3); o3.przyjmijPacjenta(pac7);
        o4.dodajPracownika(l4); o4.dodajPracownika(p4); o4.przyjmijPacjenta(pac6); o4.przyjmijPacjenta(pac10);
        o5.dodajPracownika(l5); o5.dodajPracownika(p5); o5.przyjmijPacjenta(pac3); o5.przyjmijPacjenta(pac8);

        for (Oddzial o : new Oddzial[]{o1,o2,o3,o4,o5})
            system.dodajOddzial(o);

        // Kilka przykladowych wizyt
        Wizyta w1 = system.umowWizyte(pac1, l1, LocalDateTime.now().minusHours(3));
        l1.wystawDiagnoze(w1, "Nadcisnienie tetnicze", "I10", "Odpoczynek, dieta niskosodowa");

        Wizyta w2 = system.umowWizyte(pac2, l2, LocalDateTime.now().minusHours(1));
        w2.rozpocznijWizyte();

        system.umowWizyte(pac3, l5, LocalDateTime.now().plusDays(1));
        system.umowWizyte(pac6, l4, LocalDateTime.now().plusDays(2));
        system.umowWizyte(pac9, l1, LocalDateTime.now().plusDays(3));
    }

    // -------------------------------------------------------------------------
    // INICJALIZACJA KOMPONENTOW
    // -------------------------------------------------------------------------
    public void inicjalizujKomponenty() {
        tabbedPane = new JTabbedPane();

        panelPacjentow = zbudujPanelPacjentow();
        panelWizyt     = zbudujPanelWizyt();
        panelLekarzy   = zbudujPanelPersonelu();
        panelKolejki   = zbudujPanelKolejki();
        JPanel panelOddzialow = zbudujPanelOddzialow();

        tabbedPane.addTab("Pacjenci",  new ImageIcon(), panelPacjentow);
        tabbedPane.addTab("Wizyty",    new ImageIcon(), panelWizyt);
        tabbedPane.addTab("Personel",  new ImageIcon(), panelLekarzy);
        tabbedPane.addTab("Kolejka",   new ImageIcon(), panelKolejki);
        tabbedPane.addTab("Oddzialy",  new ImageIcon(), panelOddzialow);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) odswiezKomboWizyt();
        });

        add(tabbedPane, BorderLayout.CENTER);

        JLabel statusBar = new JLabel("  System Przyjec Pacjentow | Gotowy");
        statusBar.setBorder(new EmptyBorder(3, 5, 3, 5));
        add(statusBar, BorderLayout.SOUTH);
    }

    public void inicjalizujMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuSystem = new JMenu("System");
        JMenuItem itemRaport = new JMenuItem("Generuj raport dzienny");
        itemRaport.addActionListener(e -> {
            JTextArea area = new JTextArea(system.generujRaportDzienny());
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Raport dzienny", JOptionPane.INFORMATION_MESSAGE);
        });
        JMenuItem itemWyjdz = new JMenuItem("Wyjdz");
        itemWyjdz.addActionListener(e -> System.exit(0));
        menuSystem.add(itemRaport);
        menuSystem.addSeparator();
        menuSystem.add(itemWyjdz);

        JMenu menuPomoc = new JMenu("Pomoc");
        JMenuItem itemOProgramie = new JMenuItem("O programie");
        itemOProgramie.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "System Przyjec Pacjentow\nAutorzy:\n   Adam Jankowski\n   Cassiel Duffus",
                "O programie", JOptionPane.INFORMATION_MESSAGE));
        menuPomoc.add(itemOProgramie);

        menuBar.add(menuSystem);
        menuBar.add(menuPomoc);
        setJMenuBar(menuBar);
    }

    // -------------------------------------------------------------------------
    // PANEL PACJENTOW
    // -------------------------------------------------------------------------
    private JPanel zbudujPanelPacjentow() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"ID", "Imie", "Nazwisko", "PESEL", "Gr. krwi", "W kolejce"};
        modelTabeliPacjentow = new DefaultTableModel(kolumny, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        odswiezTabelePacjentow();

        JTable tabela = new JTable(modelTabeliPacjentow);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Dodaj nowego pacjenta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // PO — pola formularza z alergiami i chorobami
        JTextField tfImie     = new JTextField(12);
        JTextField tfNazwisko = new JTextField(12);
        JTextField tfPesel    = new JTextField(12);
        JTextField tfData     = new JTextField(12);
        tfData.setToolTipText("Format: RRRR-MM-DD");
        JTextField tfGrupa    = new JTextField(6);
        JTextField tfAlergie  = new JTextField(20);
        tfAlergie.setToolTipText("Oddziel przecinkiem, np.: Penicylina, Ibuprofen");
        JTextField tfChoroby  = new JTextField(20);
        tfChoroby.setToolTipText("Oddziel przecinkiem, np.: Cukrzyca, Nadcisnienie");

        gbc.gridx=0; gbc.gridy=0; formularz.add(new JLabel("Imie:"), gbc);
        gbc.gridx=1; formularz.add(tfImie, gbc);
        gbc.gridx=2; formularz.add(new JLabel("Nazwisko:"), gbc);
        gbc.gridx=3; formularz.add(tfNazwisko, gbc);
        gbc.gridx=0; gbc.gridy=1; formularz.add(new JLabel("PESEL:"), gbc);
        gbc.gridx=1; formularz.add(tfPesel, gbc);
        gbc.gridx=2; formularz.add(new JLabel("Data ur. (RRRR-MM-DD):"), gbc);
        gbc.gridx=3; formularz.add(tfData, gbc);
        gbc.gridx=0; gbc.gridy=2; formularz.add(new JLabel("Gr. krwi:"), gbc);
        gbc.gridx=1; formularz.add(tfGrupa, gbc);
        gbc.gridx=0; gbc.gridy=3; formularz.add(new JLabel("Alergie (opcjonalnie):"), gbc);
        gbc.gridx=1; gbc.gridwidth=3; formularz.add(tfAlergie, gbc); gbc.gridwidth=1;
        gbc.gridx=0; gbc.gridy=4; formularz.add(new JLabel("Choroby przewlekle (opcjonalnie):"), gbc);
        gbc.gridx=1; gbc.gridwidth=3; formularz.add(tfChoroby, gbc); gbc.gridwidth=1;

        JButton btnDodaj = new JButton("Dodaj pacjenta");
        btnDodaj.addActionListener(e -> {
            String imie     = tfImie.getText().trim();
            String nazwisko = tfNazwisko.getText().trim();
            String pesel    = tfPesel.getText().trim();
            String dataStr  = tfData.getText().trim();
            String grupa    = tfGrupa.getText().trim();
            if (imie.isEmpty() || nazwisko.isEmpty() || pesel.isEmpty() || dataStr.isEmpty() || grupa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wypelnij wszystkie wymagane pola.", "Blad", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Pacjent nowy = new Pacjent(imie, nazwisko, pesel, LocalDate.parse(dataStr), grupa);

                // Alergie — podziel po przecinku, dodaj każdą niepustą
                String alergieTxt = tfAlergie.getText().trim();
                if (!alergieTxt.isEmpty()) {
                    for (String alergia : alergieTxt.split(",")) {
                        String a = alergia.trim();
                        if (!a.isEmpty()) nowy.dodajAlergie(a);
                    }
                }

                // Choroby przewlekłe — analogicznie
                String chorobyTxt = tfChoroby.getText().trim();
                if (!chorobyTxt.isEmpty()) {
                    for (String choroba : chorobyTxt.split(",")) {
                        String ch = choroba.trim();
                        if (!ch.isEmpty()) nowy.dodajChorobe(ch);
                    }
                }

                system.zarejestrujPacjenta(nowy);
                odswiezTabelePacjentow();
                tfImie.setText(""); tfNazwisko.setText(""); tfPesel.setText("");
                tfData.setText(""); tfGrupa.setText(""); tfAlergie.setText(""); tfChoroby.setText("");
                JOptionPane.showMessageDialog(this, "Pacjent zostal zarejestrowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty. Uzyj RRRR-MM-DD.", "Blad", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx=2; gbc.gridy=5; formularz.add(btnDodaj, gbc);

        JButton btnKarta = new JButton("Pokaz karte");
        btnKarta.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz pacjenta.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            Pacjent p = system.wyszukajPacjenta((String) modelTabeliPacjentow.getValueAt(row, 3));
            if (p != null) {
                JTextArea area = new JTextArea(p.wyswietlInfo() + "\n\n" + p.wyswietlHistorieWizyt());
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Karta pacjenta", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnKolejka = new JButton("Dodaj do kolejki");
        btnKolejka.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz pacjenta.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            Pacjent p = system.wyszukajPacjenta((String) modelTabeliPacjentow.getValueAt(row, 3));
            if (p != null) {
                system.dodajDoKolejki(p); odswiezTabelePacjentow(); odswiezPanelKolejki();
                JOptionPane.showMessageDialog(this,"Pacjent dodany do kolejki.","Sukces",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnSzukaj = new JButton("Szukaj po PESEL");
        btnSzukaj.addActionListener(e -> {
            String pesel = JOptionPane.showInputDialog(this, "Podaj PESEL:");
            if (pesel != null && !pesel.isBlank()) {
                Pacjent zn = system.wyszukajPacjenta(pesel.trim());
                if (zn != null) {
                    JTextArea area = new JTextArea(zn.wyswietlInfo() + "\n\n" + zn.wyswietlHistorieWizyt());
                    JOptionPane.showMessageDialog(this, new JScrollPane(area), "Karta pacjenta", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,"Nie znaleziono pacjenta.","Brak wynikow",JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton btnEksport = new JButton("Eksportuj karte");
        btnEksport.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz pacjenta.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            Pacjent p = system.wyszukajPacjenta((String) modelTabeliPacjentow.getValueAt(row, 3));
            if (p != null) {
                String sciezka = "karta_" + p.toString().split("PESEL: ")[1].split(" ")[0] + ".txt";
                p.eksportujDoPliku(sciezka);
                JOptionPane.showMessageDialog(this,"Zapisano do pliku: "+sciezka,"Eksport",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel przyciskiDolne = new JPanel(new FlowLayout(FlowLayout.LEFT));
        przyciskiDolne.add(btnKarta); przyciskiDolne.add(btnKolejka);
        przyciskiDolne.add(btnSzukaj); przyciskiDolne.add(btnEksport);

        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        panel.add(formularz, BorderLayout.NORTH);
        panel.add(przyciskiDolne, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // PANEL WIZYT
    // -------------------------------------------------------------------------
    private JPanel zbudujPanelWizyt() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"ID Wizyty","Pacjent","Lekarz","Data","Status"};
        modelTabeliWizyt = new DefaultTableModel(kolumny, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        odswiezTabeleWizyt();

        JTable tabela = new JTable(modelTabeliWizyt);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Umow nowa wizyte"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.anchor = GridBagConstraints.WEST;

        cbPacjenciWizyt = new JComboBox<>();
        cbLekarzWizyt   = new JComboBox<>();
        for (Pacjent p : system.pobierzListePacjentow())    cbPacjenciWizyt.addItem(p);
        for (Pracownik pr : system.pobierzListePracownikow()) if (pr instanceof Lekarz) cbLekarzWizyt.addItem((Lekarz) pr);
        JTextField tfData = new JTextField("2026-06-01 10:00", 16);

        gbc.gridx=0; gbc.gridy=0; formularz.add(new JLabel("Pacjent:"), gbc);
        gbc.gridx=1; formularz.add(cbPacjenciWizyt, gbc);
        gbc.gridx=2; formularz.add(new JLabel("Lekarz:"), gbc);
        gbc.gridx=3; formularz.add(cbLekarzWizyt, gbc);
        gbc.gridx=0; gbc.gridy=1; formularz.add(new JLabel("Data (RRRR-MM-DD HH:mm):"), gbc);
        gbc.gridx=1; formularz.add(tfData, gbc);

        JButton btnUmow = new JButton("Umow wizyte");
        btnUmow.addActionListener(e -> {
            Pacjent pacjent = (Pacjent) cbPacjenciWizyt.getSelectedItem();
            Lekarz  lekarz  = (Lekarz)  cbLekarzWizyt.getSelectedItem();
            if (pacjent==null||lekarz==null) { JOptionPane.showMessageDialog(this,"Wybierz pacjenta i lekarza.","Blad",JOptionPane.ERROR_MESSAGE); return; }
            try {
                LocalDateTime data = LocalDateTime.parse(tfData.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                system.umowWizyte(pacjent, lekarz, data);
                odswiezTabeleWizyt();
                JOptionPane.showMessageDialog(this,"Wizyta zostala zaplanowana.","Sukces",JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,"Zly format daty. Uzyj: RRRR-MM-DD HH:mm","Blad",JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx=2; gbc.gridy=1; formularz.add(btnUmow, gbc);

        // Przyciski operacji na zaznaczonej wizycie
        JButton btnSzczegoly = new JButton("Szczegoly wizyty");
        btnSzczegoly.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz wizyte.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            List<Wizyta> wizyty = system.pobierzListeWizyt();
            if (row < wizyty.size()) {
                JTextArea area = new JTextArea(wizyty.get(row).wyswietlSzczegoly());
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Szczegoly wizyty", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnZmienStatus = new JButton("Zmien status");
        btnZmienStatus.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz wizyte.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            List<Wizyta> wizyty = system.pobierzListeWizyt();
            if (row >= wizyty.size()) return;
            Wizyta wizyta = wizyty.get(row);

            String[] opcje = {"W trakcie", "Zakonczona (wymaga diagnozy)", "Odwolana"};
            String wybrany = (String) JOptionPane.showInputDialog(this,
                    "Wybierz akcje (aktualny status: " + wizyta.pobierzStatus() + "):",
                    "Zmiana statusu", JOptionPane.QUESTION_MESSAGE, null, opcje, opcje[0]);
            if (wybrany == null) return;
            switch (wybrany) {
                case "W trakcie" -> { wizyta.rozpocznijWizyte(); odswiezTabeleWizyt(); }
                case "Zakonczona (wymaga diagnozy)" ->
                        JOptionPane.showMessageDialog(this,"Uzyj przycisku 'Wystaw diagnoze' — diagnoza automatycznie konczy wizyte.","Informacja",JOptionPane.INFORMATION_MESSAGE);
                case "Odwolana" -> {
                    String powod = JOptionPane.showInputDialog(this, "Podaj powod odwolania:");
                    if (powod != null) { wizyta.anulujWizyte(powod.isBlank() ? "Brak podanego powodu" : powod); odswiezTabeleWizyt(); }
                }
            }
        });

        JButton btnDiagnoza = new JButton("Wystaw diagnoze");
        btnDiagnoza.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz wizyte.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            List<Wizyta> wizyty = system.pobierzListeWizyt();
            if (row >= wizyty.size()) return;
            Wizyta wizyta = wizyty.get(row);

            Lekarz lekarzWizyty = null;
            for (Pracownik pr : system.pobierzListePracownikow())
                if (pr instanceof Lekarz l && l.toString().equals(wizyta.pobierzLekarza().toString())) { lekarzWizyty = l; break; }
            if (lekarzWizyty == null) { JOptionPane.showMessageDialog(this,"Nie mozna zidentyfikowac lekarza wizyty.","Blad",JOptionPane.ERROR_MESSAGE); return; }

            JTextField tfOpis=new JTextField(25), tfKodICD=new JTextField(10), tfZalecenia=new JTextField(25);
            JPanel dp = new JPanel(new GridLayout(3,2,5,5));
            dp.add(new JLabel("Opis:")); dp.add(tfOpis);
            dp.add(new JLabel("Kod ICD-10:")); dp.add(tfKodICD);
            dp.add(new JLabel("Zalecenia:")); dp.add(tfZalecenia);

            if (JOptionPane.showConfirmDialog(this, dp, "Wystaw diagnoze", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                if (tfOpis.getText().trim().isEmpty()||tfKodICD.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,"Opis i kod ICD sa wymagane.","Blad",JOptionPane.ERROR_MESSAGE); return;
                }
                lekarzWizyty.wystawDiagnoze(wizyta, tfOpis.getText().trim(), tfKodICD.getText().trim(), tfZalecenia.getText().trim());
                odswiezTabeleWizyt(); odswiezTabelePacjentow();
                JOptionPane.showMessageDialog(this,"Diagnoza wystawiona. Wizyta zostala zakonczona.","Sukces",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel przyciskiDolne = new JPanel(new FlowLayout(FlowLayout.LEFT));
        przyciskiDolne.add(btnSzczegoly); przyciskiDolne.add(btnZmienStatus); przyciskiDolne.add(btnDiagnoza);

        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        panel.add(formularz, BorderLayout.NORTH);
        panel.add(przyciskiDolne, BorderLayout.SOUTH);
        return panel;
    }

    private void odswiezKomboWizyt() {
        cbPacjenciWizyt.removeAllItems();
        for (Pacjent p : system.pobierzListePacjentow()) cbPacjenciWizyt.addItem(p);
        cbLekarzWizyt.removeAllItems();
        for (Pracownik pr : system.pobierzListePracownikow()) if (pr instanceof Lekarz) cbLekarzWizyt.addItem((Lekarz) pr);
    }

    // -------------------------------------------------------------------------
    // PANEL PERSONELU
    // -------------------------------------------------------------------------
    private JPanel zbudujPanelPersonelu() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"ID","Imie i nazwisko","Typ","Specjalizacja","Aktywny"};
        modelTabeliPersonelu = new DefaultTableModel(kolumny, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        odswiezTabelePersonelu();

        JTable tabela = new JTable(modelTabeliPersonelu);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel dolny = new JPanel(new GridBagLayout());
        dolny.setBorder(BorderFactory.createTitledBorder("Dodaj lekarza"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.anchor = GridBagConstraints.WEST;

        JTextField tfImie=new JTextField(10), tfNazwisko=new JTextField(10), tfPesel=new JTextField(11),
                tfData=new JTextField(10), tfSpec=new JTextField(10), tfPwz=new JTextField(10), tfId=new JTextField(8);

        gbc.gridx=0; gbc.gridy=0; dolny.add(new JLabel("Imie:"),gbc);          gbc.gridx=1; dolny.add(tfImie,gbc);
        gbc.gridx=2; dolny.add(new JLabel("Nazwisko:"),gbc);                    gbc.gridx=3; dolny.add(tfNazwisko,gbc);
        gbc.gridx=4; dolny.add(new JLabel("PESEL:"),gbc);                       gbc.gridx=5; dolny.add(tfPesel,gbc);
        gbc.gridx=0; gbc.gridy=1; dolny.add(new JLabel("Data ur. (RRRR-MM-DD):"),gbc); gbc.gridx=1; dolny.add(tfData,gbc);
        gbc.gridx=2; dolny.add(new JLabel("Specjalizacja:"),gbc);               gbc.gridx=3; dolny.add(tfSpec,gbc);
        gbc.gridx=4; dolny.add(new JLabel("Nr PWZ:"),gbc);                      gbc.gridx=5; dolny.add(tfPwz,gbc);
        gbc.gridx=0; gbc.gridy=2; dolny.add(new JLabel("ID pracownika:"),gbc); gbc.gridx=1; dolny.add(tfId,gbc);

        JButton btnDodajLekarza = new JButton("Dodaj lekarza");
        btnDodajLekarza.addActionListener(e -> {
            try {
                Lekarz lekarz = new Lekarz(tfImie.getText().trim(), tfNazwisko.getText().trim(),
                        tfPesel.getText().trim(), LocalDate.parse(tfData.getText().trim()),
                        tfId.getText().trim(), tfSpec.getText().trim(), tfPwz.getText().trim());
                system.dodajPracownika(lekarz);
                odswiezTabelePersonelu();
                tfImie.setText(""); tfNazwisko.setText(""); tfPesel.setText("");
                tfData.setText(""); tfSpec.setText(""); tfPwz.setText(""); tfId.setText("");
                JOptionPane.showMessageDialog(this,"Lekarz zostal dodany.","Sukces",JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,"Niepoprawny format daty.","Blad",JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnDezaktywuj = new JButton("Dezaktywuj pracownika");
        btnDezaktywuj.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz pracownika.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            List<Pracownik> lista = system.pobierzListePracownikow();
            if (row < lista.size()) { lista.get(row).dezaktywuj(); odswiezTabelePersonelu();
                JOptionPane.showMessageDialog(this,"Pracownik zostal dezaktywowany.","Sukces",JOptionPane.INFORMATION_MESSAGE); }
        });

        JButton btnInfo = new JButton("Informacje");
        btnInfo.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz pracownika.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            List<Pracownik> lista = system.pobierzListePracownikow();
            if (row < lista.size()) {
                JTextArea area = new JTextArea(lista.get(row).wyswietlInfo());
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Karta pracownika", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridx=2; gbc.gridy=2; dolny.add(btnDodajLekarza,gbc);
        gbc.gridx=3; dolny.add(btnDezaktywuj,gbc);
        gbc.gridx=4; dolny.add(btnInfo,gbc);

        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        panel.add(dolny, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // PANEL KOLEJKI
    // -------------------------------------------------------------------------
    private JPanel zbudujPanelKolejki() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea areaKolejka = new JTextArea();
        areaKolejka.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaKolejka.setText(system.wyswietlKolejke());

        JPanel gora = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gora.setBorder(BorderFactory.createTitledBorder("Zarzadzanie kolejka"));

        JButton btnOdswierz = new JButton("Odswiez kolejke");
        btnOdswierz.addActionListener(e -> areaKolejka.setText(system.wyswietlKolejke()));

        JButton btnWywolaj = new JButton("Wywolaj nastepnego pacjenta");
        btnWywolaj.addActionListener(e -> {
            Pacjent nastepny = system.wywolajNastepnego();
            if (nastepny != null) {
                JOptionPane.showMessageDialog(this,"Wywolano pacjenta:\n"+nastepny,"Nastepny pacjent",JOptionPane.INFORMATION_MESSAGE);
                areaKolejka.setText(system.wyswietlKolejke());
                odswiezTabelePacjentow();
            } else {
                JOptionPane.showMessageDialog(this,"Kolejka jest pusta.","Kolejka",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gora.add(btnWywolaj); gora.add(btnOdswierz);
        panel.add(gora, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaKolejka), BorderLayout.CENTER);
        return panel;
    }

    // -------------------------------------------------------------------------
    // PANEL ODDZIALOW — smart add/remove
    // -------------------------------------------------------------------------
    private JPanel zbudujPanelOddzialow() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"Nazwa oddzialu","Numer","Max lozek","Wolne lozka","Liczba pacjentow"};
        modelTabeliOddzialow = new DefaultTableModel(kolumny, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        odswiezTabeleOddzialow();

        JTable tabela = new JTable(modelTabeliOddzialow);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- Formularz dodawania oddzialu ---
        JPanel fDodaj = new JPanel(new GridBagLayout());
        fDodaj.setBorder(BorderFactory.createTitledBorder("Dodaj nowy oddzial"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4); gbc.anchor = GridBagConstraints.WEST;

        JTextField tfNazwa=new JTextField(15), tfNumer=new JTextField(8), tfLozka=new JTextField(5);
        gbc.gridx=0; gbc.gridy=0; fDodaj.add(new JLabel("Nazwa:"),gbc);    gbc.gridx=1; fDodaj.add(tfNazwa,gbc);
        gbc.gridx=2; fDodaj.add(new JLabel("Numer:"),gbc);                  gbc.gridx=3; fDodaj.add(tfNumer,gbc);
        gbc.gridx=4; fDodaj.add(new JLabel("Max lozek:"),gbc);              gbc.gridx=5; fDodaj.add(tfLozka,gbc);

        JButton btnDodajOddzial = new JButton("Dodaj oddzial");
        btnDodajOddzial.addActionListener(e -> {
            try {
                Oddzial o = new Oddzial(tfNazwa.getText().trim(), tfNumer.getText().trim(), Integer.parseInt(tfLozka.getText().trim()));
                system.dodajOddzial(o); odswiezTabeleOddzialow();
                tfNazwa.setText(""); tfNumer.setText(""); tfLozka.setText("");
                JOptionPane.showMessageDialog(this,"Oddzial zostal dodany.","Sukces",JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"Podaj poprawna liczbe lozek.","Blad",JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx=0; gbc.gridy=1; fDodaj.add(btnDodajOddzial, gbc);

        // --- Formularz zarzadzania oddzialem (smart add/remove) ---
        JPanel fZarzadzaj = new JPanel(new GridBagLayout());
        fZarzadzaj.setBorder(BorderFactory.createTitledBorder("Zarzadzaj zaznaczonym oddzialem"));
        GridBagConstraints g2 = new GridBagConstraints();
        g2.insets = new Insets(4,4,4,4); g2.anchor = GridBagConstraints.WEST;

        JComboBox<Pracownik> cbPracownicy = new JComboBox<>();
        JComboBox<Pacjent>   cbPacjenci   = new JComboBox<>();
        for (Pracownik pr : system.pobierzListePracownikow()) cbPracownicy.addItem(pr);
        for (Pacjent   p  : system.pobierzListePacjentow())   cbPacjenci.addItem(p);

        JButton btnPracownik = new JButton("Dodaj pracownika");
        JButton btnPacjent   = new JButton("Przyjmij pacjenta");
        JButton btnStatus    = new JButton("Pokaz status oddzialu");

        // Pomocnicza metoda aktualizujaca etykiety przyciskow
        Runnable odswiezPrzyciski = () -> {
            int row = tabela.getSelectedRow();
            if (row < 0 || row >= system.pobierzListeOddzialow().size()) {
                btnPracownik.setText("Dodaj pracownika");
                btnPacjent.setText("Przyjmij pacjenta");
                return;
            }
            Oddzial od = system.pobierzListeOddzialow().get(row);

            Pracownik wybrPr = (Pracownik) cbPracownicy.getSelectedItem();
            if (wybrPr != null && od.pobierzPracownikow().contains(wybrPr))
                btnPracownik.setText("Usun pracownika z oddzialu");
            else
                btnPracownik.setText("Dodaj pracownika do oddzialu");

            Pacjent wybrPac = (Pacjent) cbPacjenci.getSelectedItem();
            if (wybrPac != null && od.pobierzPacjentow().contains(wybrPac))
                btnPacjent.setText("Wypisz pacjenta z oddzialu");
            else
                btnPacjent.setText("Przyjmij pacjenta na oddzial");
        };

        // Listenery zmieniajace etykiety na biezaco
        tabela.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) odswiezPrzyciski.run(); });
        cbPracownicy.addActionListener(e -> odswiezPrzyciski.run());
        cbPacjenci.addActionListener(e -> odswiezPrzyciski.run());

        // Akcja przycisku pracownika — dodaj lub usun
        btnPracownik.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz oddzial w tabeli.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            Pracownik wybrany = (Pracownik) cbPracownicy.getSelectedItem();
            if (wybrany == null) return;
            Oddzial od = system.pobierzListeOddzialow().get(row);
            if (od.pobierzPracownikow().contains(wybrany)) {
                od.usunPracownika(wybrany);
                JOptionPane.showMessageDialog(this,"Usunieto: "+wybrany+" z oddzialu "+od.pobierzNazwe(),"Sukces",JOptionPane.INFORMATION_MESSAGE);
            } else {
                od.dodajPracownika(wybrany);
                JOptionPane.showMessageDialog(this,"Dodano: "+wybrany+" do oddzialu "+od.pobierzNazwe(),"Sukces",JOptionPane.INFORMATION_MESSAGE);
            }
            odswiezTabeleOddzialow(); odswiezPrzyciski.run();
        });

        // Akcja przycisku pacjenta — przyjmij lub wypisz
        btnPacjent.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz oddzial w tabeli.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            Pacjent wybrany = (Pacjent) cbPacjenci.getSelectedItem();
            if (wybrany == null) return;
            Oddzial od = system.pobierzListeOddzialow().get(row);
            if (od.pobierzPacjentow().contains(wybrany)) {
                od.wypiszPacjenta(wybrany);
                JOptionPane.showMessageDialog(this,"Wypisano: "+wybrany+" z oddzialu "+od.pobierzNazwe(),"Sukces",JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (!od.przyjmijPacjenta(wybrany))
                    JOptionPane.showMessageDialog(this,"Brak wolnych miejsc na oddziale.","Blad",JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this,"Przyjeto: "+wybrany+" na oddzial "+od.pobierzNazwe(),"Sukces",JOptionPane.INFORMATION_MESSAGE);
            }
            odswiezTabeleOddzialow(); odswiezPrzyciski.run();
        });

        // PO
        btnStatus.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this,"Zaznacz oddzial.","Informacja",JOptionPane.INFORMATION_MESSAGE); return; }
            Oddzial od = system.pobierzListeOddzialow().get(row);

            StringBuilder sb = new StringBuilder();
            sb.append(od.wyswietlStatus());
            sb.append("\n\n");
            sb.append(od.wyswietlPracownikow());
            sb.append("\n\nPacjenci na oddziale (").append(od.pobierzPacjentow().size()).append("):\n");
            if (od.pobierzPacjentow().isEmpty()) {
                sb.append("Brak pacjentow na oddziale.\n");
            } else {
                int i = 1;
                for (Pacjent p : od.pobierzPacjentow()) {
                    sb.append(i++).append(". ")
                            .append(p.pobierzImie()).append(" ").append(p.pobierzNazwisko())
                            .append("  |  ID: ").append(p.pobierzIdPacjenta())
                            .append("  |  Gr. krwi: ").append(p.pobierzGrupeKrwi())
                            .append("\n");
                }
            }

            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Status oddzialu", JOptionPane.INFORMATION_MESSAGE);
        });

        // Odswiez combo przy wejsciu na zakladke Oddzialy (index 4)
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 4) {
                cbPracownicy.removeAllItems();
                for (Pracownik pr : system.pobierzListePracownikow()) cbPracownicy.addItem(pr);
                cbPacjenci.removeAllItems();
                for (Pacjent p : system.pobierzListePacjentow()) cbPacjenci.addItem(p);
                odswiezPrzyciski.run();
            }
        });

        g2.gridx=0; g2.gridy=0; fZarzadzaj.add(new JLabel("Pracownik:"),g2); g2.gridx=1; fZarzadzaj.add(cbPracownicy,g2); g2.gridx=2; fZarzadzaj.add(btnPracownik,g2);
        g2.gridx=0; g2.gridy=1; fZarzadzaj.add(new JLabel("Pacjent:"),g2);   g2.gridx=1; fZarzadzaj.add(cbPacjenci,g2);   g2.gridx=2; fZarzadzaj.add(btnPacjent,g2);
        g2.gridx=0; g2.gridy=2; fZarzadzaj.add(btnStatus,g2);

        JPanel poludnie = new JPanel(new BorderLayout());
        poludnie.add(fDodaj, BorderLayout.NORTH);
        poludnie.add(fZarzadzaj, BorderLayout.CENTER);

        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        panel.add(poludnie, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // METODY ODSWIEZAJACE
    // -------------------------------------------------------------------------
    private void odswiezTabelePacjentow() {
        modelTabeliPacjentow.setRowCount(0);
        for (Pacjent p : system.pobierzListePacjentow()) {
            modelTabeliPacjentow.addRow(new Object[]{
                    p.pobierzIdPacjenta(), p.pobierzImie(), p.pobierzNazwisko(),
                    p.toString().contains("PESEL: ") ? p.toString().split("PESEL: ")[1].split(" ")[0] : "",
                    p.pobierzGrupeKrwi(), p.czyWPoczekalni() ? "Tak" : "Nie"
            });
        }
    }

    private void odswiezTabeleWizyt() {
        modelTabeliWizyt.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Wizyta w : system.pobierzListeWizyt()) {
            modelTabeliWizyt.addRow(new Object[]{
                    w.pobierzIdWizyty(), w.pobierzPacjenta().toString(),
                    w.pobierzLekarza().toString(), w.pobierzDate().format(fmt), w.pobierzStatus().toString()
            });
        }
    }

    private void odswiezTabelePersonelu() {
        modelTabeliPersonelu.setRowCount(0);
        for (Pracownik p : system.pobierzListePracownikow()) {
            String typ = p instanceof Lekarz ? "Lekarz" : p instanceof Pielegniarka ? "Pielegniarka" : p instanceof Recepcjonista ? "Recepcjonista" : "Pracownik";
            modelTabeliPersonelu.addRow(new Object[]{ p.idPracownika, p.imie+" "+p.nazwisko, typ, p.specjalizacja, p.aktywny ? "Tak" : "Nie" });
        }
    }

    private void odswiezTabeleOddzialow() {
        modelTabeliOddzialow.setRowCount(0);
        for (Oddzial o : system.pobierzListeOddzialow()) {
            modelTabeliOddzialow.addRow(new Object[]{
                    o.pobierzNazwe(), o.pobierzNumer(), o.pobierzMaxLozek(), o.dostepneLozka(), o.pobierzPacjentow().size()
            });
        }
    }

    private void odswiezPanelKolejki() {
        Component kComp = tabbedPane.getComponentAt(3);
        if (kComp instanceof JPanel kPanel) {
            for (Component c : kPanel.getComponents())
                if (c instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextArea area)
                    area.setText(system.wyswietlKolejke());
        }
    }

    public void pokazPanelPacjentow() { tabbedPane.setSelectedIndex(0); }
    public void pokazPanelWizyt()     { tabbedPane.setSelectedIndex(1); }
    public void pokazPanelKolejki()   { tabbedPane.setSelectedIndex(3); }
    public void odswiezWidok()        { odswiezTabelePacjentow(); odswiezTabeleWizyt(); odswiezTabelePersonelu(); odswiezTabeleOddzialow(); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { System.out.println("Nie udalo sie ustawic wygladu systemowego."); }
            new GlowneOkno().setVisible(true);
        });
    }
}