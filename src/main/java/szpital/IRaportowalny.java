package szpital;

// Interfejs dla obiektów, które można raportować i eksportować
public interface IRaportowalny {
    String generujRaport();
    void eksportujDoPliku(String sciezka);
}