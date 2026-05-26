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

    public GlowneOkno() {
        system = SystemPrzyjec.getInstance();
        zaladujDanePrzykladowe();
        inicjalizujKomponenty();
        inicjalizujMenu();
        setTitle("System Przyjec Pacjentow - Szpital");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void zaladujDanePrzykladowe() {
        Lekarz l1 = new Lekarz("Jan", "Kowalski", "70010112345", LocalDate.of(1970, 1, 1),
                "L001", "Kardiologia", "PWZ123456");
        Lekarz l2 = new Lekarz("Anna", "Nowak", "80020223456", LocalDate.of(1980, 2, 2),
                "L002", "Neurologia", "PWZ654321");
        Pielegniarka p1 = new Pielegniarka("Maria", "Wiśniewska", "85030334567", LocalDate.of(1985, 3, 3),
                "P001", "Ogólna", 101);
        Recepcjonista r1 = new Recepcjonista("Ewa", "Zielinska", "90040445678", LocalDate.of(1990, 4, 4),
                "R001", 1);

        system.dodajPracownika(l1);
        system.dodajPracownika(l2);
        system.dodajPracownika(p1);
        system.dodajPracownika(r1);

        Pacjent pac1 = new Pacjent("Tomasz", "Adamski", "95050556789", LocalDate.of(1995, 5, 5), "A+");
        pac1.dodajAlergie("Penicylina");
        pac1.dodajChorobe("Nadcisnienie");
        Pacjent pac2 = new Pacjent("Karolina", "Brzezinska", "88060667890", LocalDate.of(1988, 6, 6), "B-");

        system.zarejestrujPacjenta(pac1);
        system.zarejestrujPacjenta(pac2);

        Oddzial o1 = new Oddzial("Kardiologia", "ODD-K1", 20);
        Oddzial o2 = new Oddzial("Neurologia", "ODD-N1", 15);
        o1.dodajPracownika(l1);
        o1.dodajPracownika(p1);
        o2.dodajPracownika(l2);
        o1.przyjmijPacjenta(pac1);

        system.dodajOddzial(o1);
        system.dodajOddzial(o2);

        Wizyta w = system.umowWizyte(pac1, l1, LocalDateTime.now().minusHours(2));
        l1.wystawDiagnoze(w, "Nadcisnienie tetnicze", "I10", "Odpoczynek, dieta niskosodowa");
    }

    public void inicjalizujKomponenty() {
        tabbedPane = new JTabbedPane();

        panelPacjentow = zbudujPanelPacjentow();
        panelWizyt = zbudujPanelWizyt();
        panelLekarzy = zbudujPanelPersonelu();
        panelKolejki = zbudujPanelKolejki();
        JPanel panelOddzialow = zbudujPanelOddzialow();

        tabbedPane.addTab("Pacjenci", new ImageIcon(), panelPacjentow);
        tabbedPane.addTab("Wizyty", new ImageIcon(), panelWizyt);
        tabbedPane.addTab("Personel", new ImageIcon(), panelLekarzy);
        tabbedPane.addTab("Kolejka", new ImageIcon(), panelKolejki);
        tabbedPane.addTab("Oddzialy", new ImageIcon(), panelOddzialow);

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
            String raport = system.generujRaportDzienny();
            JTextArea area = new JTextArea(raport);
            area.setEditable(false);
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

    private JPanel zbudujPanelPacjentow() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"ID", "Imie", "Nazwisko", "PESEL", "Gr. krwi", "W kolejce"};
        modelTabeliPacjentow = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        odswiezTabelePacjentow();

        JTable tabela = new JTable(modelTabeliPacjentow);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Dodaj nowego pacjenta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField tfImie = new JTextField(12);
        JTextField tfNazwisko = new JTextField(12);
        JTextField tfPesel = new JTextField(12);
        JTextField tfData = new JTextField(12);
        tfData.setToolTipText("Format: RRRR-MM-DD");
        JTextField tfGrupa = new JTextField(6);

        gbc.gridx = 0; gbc.gridy = 0; formularz.add(new JLabel("Imie:"), gbc);
        gbc.gridx = 1; formularz.add(tfImie, gbc);
        gbc.gridx = 2; formularz.add(new JLabel("Nazwisko:"), gbc);
        gbc.gridx = 3; formularz.add(tfNazwisko, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formularz.add(new JLabel("PESEL:"), gbc);
        gbc.gridx = 1; formularz.add(tfPesel, gbc);
        gbc.gridx = 2; formularz.add(new JLabel("Data ur. (RRRR-MM-DD):"), gbc);
        gbc.gridx = 3; formularz.add(tfData, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formularz.add(new JLabel("Gr. krwi:"), gbc);
        gbc.gridx = 1; formularz.add(tfGrupa, gbc);

        JButton btnDodaj = new JButton("Dodaj pacjenta");
        btnDodaj.addActionListener(e -> {
            String imie = tfImie.getText().trim();
            String nazwisko = tfNazwisko.getText().trim();
            String pesel = tfPesel.getText().trim();
            String dataStr = tfData.getText().trim();
            String grupa = tfGrupa.getText().trim();

            if (imie.isEmpty() || nazwisko.isEmpty() || pesel.isEmpty() || dataStr.isEmpty() || grupa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wypelnij wszystkie pola.", "Blad", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                LocalDate data = LocalDate.parse(dataStr);
                Pacjent nowyPacjent = new Pacjent(imie, nazwisko, pesel, data, grupa);
                system.zarejestrujPacjenta(nowyPacjent);
                odswiezTabelePacjentow();
                tfImie.setText(""); tfNazwisko.setText(""); tfPesel.setText("");
                tfData.setText(""); tfGrupa.setText("");
                JOptionPane.showMessageDialog(this, "Pacjent zostal zarejestrowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty. Uzyj RRRR-MM-DD.", "Blad", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnKolejka = new JButton("Dodaj do kolejki");
        btnKolejka.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Zaznacz pacjenta.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String pesel = (String) modelTabeliPacjentow.getValueAt(row, 3);
            Pacjent p = system.wyszukajPacjenta(pesel);
            if (p != null) {
                system.dodajDoKolejki(p);
                odswiezTabelePacjentow();
                odswiezPanelKolejki();
                JOptionPane.showMessageDialog(this, "Pacjent dodany do kolejki.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnSzukaj = new JButton("Szukaj po PESEL");
        btnSzukaj.addActionListener(e -> {
            String pesel = JOptionPane.showInputDialog(this, "Podaj PESEL:");
            if (pesel != null && !pesel.isBlank()) {
                Pacjent znaleziony = system.wyszukajPacjenta(pesel.trim());
                if (znaleziony != null) {
                    JTextArea area = new JTextArea(znaleziony.wyswietlInfo() + "\n\n" + znaleziony.wyswietlHistorieWizyt());
                    area.setEditable(false);
                    area.setPreferredSize(new Dimension(800, 400));
                    JOptionPane.showMessageDialog(this, new JScrollPane(area), "Karta pacjenta", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nie znaleziono pacjenta o podanym PESEL.", "Brak wynikow", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton btnEksport = new JButton("Eksportuj karte");
        btnEksport.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Zaznacz pacjenta.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String pesel = (String) modelTabeliPacjentow.getValueAt(row, 3);
            Pacjent p = system.wyszukajPacjenta(pesel);
            if (p != null) {
                String sciezka = "karta_" + pesel + ".txt";
                p.eksportujDoPliku(sciezka);
                JOptionPane.showMessageDialog(this, "Zapisano do pliku: " + sciezka, "Eksport", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridx = 2; gbc.gridy = 2; formularz.add(btnDodaj, gbc);
        gbc.gridx = 3; formularz.add(btnSzukaj, gbc);

        JPanel przyciskiDolne = new JPanel(new FlowLayout(FlowLayout.LEFT));
        przyciskiDolne.add(btnKolejka);
        przyciskiDolne.add(btnEksport);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(formularz, BorderLayout.NORTH);
        panel.add(przyciskiDolne, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel zbudujPanelWizyt() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"ID Wizyty", "Pacjent", "Lekarz", "Data", "Status"};
        modelTabeliWizyt = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        odswiezTabeleWizyt();

        JTable tabela = new JTable(modelTabeliWizyt);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Umow nowa wizyte"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<Pacjent> cbPacjenci = new JComboBox<>();
        JComboBox<Lekarz> cbLekarze = new JComboBox<>();
        for (Pacjent p : system.pobierzListePacjentow()) cbPacjenci.addItem(p);
        for (Pracownik pr : system.pobierzListePracownikow()) {
            if (pr instanceof Lekarz) cbLekarze.addItem((Lekarz) pr);
        }
        JTextField tfData = new JTextField("2025-06-01 10:00", 16);

        gbc.gridx = 0; gbc.gridy = 0; formularz.add(new JLabel("Pacjent:"), gbc);
        gbc.gridx = 1; formularz.add(cbPacjenci, gbc);
        gbc.gridx = 2; formularz.add(new JLabel("Lekarz:"), gbc);
        gbc.gridx = 3; formularz.add(cbLekarze, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formularz.add(new JLabel("Data (RRRR-MM-DD HH:mm):"), gbc);
        gbc.gridx = 1; formularz.add(tfData, gbc);

        JButton btnUmow = new JButton("Umow wizyte");
        btnUmow.addActionListener(e -> {
            Pacjent pacjent = (Pacjent) cbPacjenci.getSelectedItem();
            Lekarz lekarz = (Lekarz) cbLekarze.getSelectedItem();
            if (pacjent == null || lekarz == null) {
                JOptionPane.showMessageDialog(this, "Wybierz pacjenta i lekarza.", "Blad", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime data = LocalDateTime.parse(tfData.getText().trim(), fmt);
                system.umowWizyte(pacjent, lekarz, data);
                odswiezTabeleWizyt();
                JOptionPane.showMessageDialog(this, "Wizyta zostala zaplanowana.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Zly format daty. Uzyj: RRRR-MM-DD HH:mm", "Blad", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnSzczegoly = new JButton("Szczegoly wizyty");
        btnSzczegoly.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Zaznacz wizyte.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Wizyta> wizyty = system.pobierzListeWizyt();
            if (row < wizyty.size()) {
                JTextArea area = new JTextArea(wizyty.get(row).wyswietlSzczegoly());
                area.setEditable(false);
                area.setPreferredSize(new Dimension(800, 400));
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Szczegoly wizyty", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridx = 2; gbc.gridy = 1; formularz.add(btnUmow, gbc);
        gbc.gridx = 3; formularz.add(btnSzczegoly, gbc);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(formularz, BorderLayout.NORTH);
        return panel;
    }

    private JPanel zbudujPanelPersonelu() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"ID", "Imie i nazwisko", "Typ", "Specjalizacja", "Aktywny"};
        modelTabeliPersonelu = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        odswiezTabelePersonelu();

        JTable tabela = new JTable(modelTabeliPersonelu);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel dolny = new JPanel(new GridBagLayout());
        dolny.setBorder(BorderFactory.createTitledBorder("Dodaj lekarza"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField tfImie = new JTextField(10);
        JTextField tfNazwisko = new JTextField(10);
        JTextField tfPesel = new JTextField(11);
        JTextField tfData = new JTextField(10);
        JTextField tfSpec = new JTextField(10);
        JTextField tfPwz = new JTextField(10);
        JTextField tfId = new JTextField(8);

        gbc.gridx = 0; gbc.gridy = 0; dolny.add(new JLabel("Imie:"), gbc);
        gbc.gridx = 1; dolny.add(tfImie, gbc);
        gbc.gridx = 2; dolny.add(new JLabel("Nazwisko:"), gbc);
        gbc.gridx = 3; dolny.add(tfNazwisko, gbc);
        gbc.gridx = 4; dolny.add(new JLabel("PESEL:"), gbc);
        gbc.gridx = 5; dolny.add(tfPesel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dolny.add(new JLabel("Data ur.:"), gbc);
        gbc.gridx = 1; dolny.add(tfData, gbc);
        gbc.gridx = 2; dolny.add(new JLabel("Specjalizacja:"), gbc);
        gbc.gridx = 3; dolny.add(tfSpec, gbc);
        gbc.gridx = 4; dolny.add(new JLabel("Nr PWZ:"), gbc);
        gbc.gridx = 5; dolny.add(tfPwz, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dolny.add(new JLabel("ID pracownika:"), gbc);
        gbc.gridx = 1; dolny.add(tfId, gbc);

        JButton btnDodajLekarza = new JButton("Dodaj lekarza");
        btnDodajLekarza.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(tfData.getText().trim());
                Lekarz lekarz = new Lekarz(tfImie.getText().trim(), tfNazwisko.getText().trim(),
                        tfPesel.getText().trim(), data, tfId.getText().trim(),
                        tfSpec.getText().trim(), tfPwz.getText().trim());
                system.dodajPracownika(lekarz);
                odswiezTabelePersonelu();
                JOptionPane.showMessageDialog(this, "Lekarz zostal dodany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty.", "Blad", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnDezaktywuj = new JButton("Dezaktywuj pracownika");
        btnDezaktywuj.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Zaznacz pracownika.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Pracownik> lista = system.pobierzListePracownikow();
            if (row < lista.size()) {
                lista.get(row).dezaktywuj();
                odswiezTabelePersonelu();
                JOptionPane.showMessageDialog(this, "Pracownik zostal dezaktywowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnInfo = new JButton("Informacje");
        btnInfo.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Zaznacz pracownika.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Pracownik> lista = system.pobierzListePracownikow();
            if (row < lista.size()) {
                JTextArea area = new JTextArea(lista.get(row).wyswietlInfo());
                area.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Karta pracownika", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridx = 2; gbc.gridy = 2; dolny.add(btnDodajLekarza, gbc);
        gbc.gridx = 3; dolny.add(btnDezaktywuj, gbc);
        gbc.gridx = 4; dolny.add(btnInfo, gbc);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(dolny, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel zbudujPanelKolejki() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea areaKolejka = new JTextArea();
        areaKolejka.setEditable(false);
        areaKolejka.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaKolejka.setText(system.wyswietlKolejke());
        JScrollPane scroll = new JScrollPane(areaKolejka);

        JPanel gora = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gora.setBorder(BorderFactory.createTitledBorder("Zarzadzanie kolejka"));

        JButton btnOdswierz = new JButton("Odswiez kolejke");
        btnOdswierz.addActionListener(e -> {
            areaKolejka.setText(system.wyswietlKolejke());
        });

        JButton btnWywolaj = new JButton("Wywolaj nastepnego pacjenta");
        btnWywolaj.addActionListener(e -> {
            Pacjent nastepny = system.wywolajNastepnego();
            if (nastepny != null) {
                JOptionPane.showMessageDialog(this,
                        "Wywolano pacjenta:\n" + nastepny.toString(),
                        "Nastepny pacjent", JOptionPane.INFORMATION_MESSAGE);
                areaKolejka.setText(system.wyswietlKolejke());
                odswiezTabelePacjentow();
            } else {
                JOptionPane.showMessageDialog(this, "Kolejka jest pusta.", "Kolejka", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gora.add(btnWywolaj);
        gora.add(btnOdswierz);

        panel.add(gora, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel zbudujPanelOddzialow() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] kolumny = {"Nazwa oddzialu", "Numer", "Max lozek", "Wolne lozka", "Personel"};
        modelTabeliOddzialow = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        odswiezTabeleOddzialow();

        JTable tabela = new JTable(modelTabeliOddzialow);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Dodaj nowy oddzial"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField tfNazwa = new JTextField(15);
        JTextField tfNumer = new JTextField(8);
        JTextField tfLozka = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; formularz.add(new JLabel("Nazwa:"), gbc);
        gbc.gridx = 1; formularz.add(tfNazwa, gbc);
        gbc.gridx = 2; formularz.add(new JLabel("Numer:"), gbc);
        gbc.gridx = 3; formularz.add(tfNumer, gbc);
        gbc.gridx = 4; formularz.add(new JLabel("Max lozek:"), gbc);
        gbc.gridx = 5; formularz.add(tfLozka, gbc);

        JButton btnDodaj = new JButton("Dodaj oddzial");
        btnDodaj.addActionListener(e -> {
            try {
                int maxLozek = Integer.parseInt(tfLozka.getText().trim());
                Oddzial oddzial = new Oddzial(tfNazwa.getText().trim(), tfNumer.getText().trim(), maxLozek);
                system.dodajOddzial(oddzial);
                odswiezTabeleOddzialow();
                JOptionPane.showMessageDialog(this, "Oddzial zostal dodany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                tfNazwa.setText(""); tfNumer.setText(""); tfLozka.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Podaj poprawna liczbe lozek.", "Blad", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnStatus = new JButton("Pokaz status");
        btnStatus.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Zaznacz oddzial.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Oddzial> lista = system.pobierzListeOddzialow();
            if (row < lista.size()) {
                JTextArea area = new JTextArea(lista.get(row).wyswietlStatus() + "\n\n" + lista.get(row).wyswietlPracownikow());
                area.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Status oddzialu", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 1; formularz.add(btnDodaj, gbc);
        gbc.gridx = 1; formularz.add(btnStatus, gbc);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(formularz, BorderLayout.SOUTH);
        return panel;
    }

    private void odswiezTabelePacjentow() {
        modelTabeliPacjentow.setRowCount(0);
        for (Pacjent p : system.pobierzListePacjentow()) {
            modelTabeliPacjentow.addRow(new Object[]{
                    p.pobierzIdPacjenta(),
                    p.pobierzImie(),
                    p.pobierzNazwisko(),
                    p.toString().contains("PESEL: ") ? p.toString().split("PESEL: ")[1].split(" ")[0] : "",
                    p.pobierzGrupeKrwi(),
                    p.czyWPoczekalni() ? "Tak" : "Nie"
            });
        }
    }

    private void odswiezTabeleWizyt() {
        modelTabeliWizyt.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Wizyta w : system.pobierzListeWizyt()) {
            modelTabeliWizyt.addRow(new Object[]{
                    w.pobierzIdWizyty(),
                    w.pobierzPacjenta().toString(),
                    w.pobierzLekarza().toString(),
                    w.pobierzDate().format(fmt),
                    w.pobierzStatus().toString()
            });
        }
    }

    private void odswiezTabelePersonelu() {
        modelTabeliPersonelu.setRowCount(0);
        for (Pracownik p : system.pobierzListePracownikow()) {
            String typ;
            if (p instanceof Lekarz) typ = "Lekarz";
            else if (p instanceof Pielegniarka) typ = "Pielęgniarka";
            else if (p instanceof Recepcjonista) typ = "Recepcjonista";
            else typ = "Pracownik";

            modelTabeliPersonelu.addRow(new Object[]{
                    p.idPracownika,
                    p.imie + " " + p.nazwisko,
                    typ,
                    p.specjalizacja,
                    p.aktywny ? "Tak" : "Nie"
            });
        }
    }

    private void odswiezTabeleOddzialow() {
        modelTabeliOddzialow.setRowCount(0);
        for (Oddzial o : system.pobierzListeOddzialow()) {
            modelTabeliOddzialow.addRow(new Object[]{
                    o.pobierzNazwe(),
                    o.pobierzNumer(),
                    o.pobierzMaxLozek(),
                    o.dostepneLozka(),
                    o.pobierzPacjentow().size()
            });
        }
    }

    private void odswiezPanelKolejki() {
        Component kolejkaComp = tabbedPane.getComponentAt(3);
        if (kolejkaComp instanceof JPanel kPanel) {
            for (Component c : kPanel.getComponents()) {
                if (c instanceof JScrollPane sp) {
                    if (sp.getViewport().getView() instanceof JTextArea area) {
                        area.setText(system.wyswietlKolejke());
                    }
                }
            }
        }
    }

    public void pokazPanelPacjentow() {
        tabbedPane.setSelectedIndex(0);
    }

    public void pokazPanelWizyt() {
        tabbedPane.setSelectedIndex(1);
    }

    public void pokazPanelKolejki() {
        tabbedPane.setSelectedIndex(3);
    }

    public void odswiezWidok() {
        odswiezTabelePacjentow();
        odswiezTabeleWizyt();
        odswiezTabelePersonelu();
        odswiezTabeleOddzialow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Nie udalo sie ustawic wygladu systemowego.");
            }
            GlowneOkno okno = new GlowneOkno();
            okno.setVisible(true);
        });
    }
}