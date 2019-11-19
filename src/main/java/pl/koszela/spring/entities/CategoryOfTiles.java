package pl.koszela.spring.entities;

public enum CategoryOfTiles {

    DACHOWKA_PODSTAWOWA("Dachówka podstawowa"),
    DACHOWKA_SKRAJNA_LEWA("Dachówka skrajna lewa"),
    DACHOWKA_SKRAJNA_PRAWA("Dachówka skrajna prawa"),
    DACHOWKA_POLOWKOWA("Dachówka połówkowa"),
    DACHOWKA_WENTYLACYJNA("Dachówka wentylacyjna"),
    KOMPLET_KOMINKA_WENTYLACYJNEGO("Komplet kominka wentylacyjnego"),
    GASIOR_PODSTAWOWY("Gąsior podstawowy"),
    GASIOR_POCZATKOWY_KALENICA_PROSTA("Gąsior początkowy kal. prosta"),
    GASIOR_KONCOWY_KALENICA_PROSTA("Gąsior końcowy kal. prosta"),
    PLYTKA_POCZATKOWA("Płytka początkowa"),
    PLYTKA_KONCOWA("Płytka końcowa"),
    TROJNIK("Trójnik"),
    GASIAR_ZAOKRAGLONY("Gąsior zaokrąglony"),
    BRAK("Brak"),
    BRAK1("Brak"),
    BRAK2("Brak"),
    BRAK3("Brak"),
    BRAK4("Brak"),
    BRAK5("Brak");

    private String name;

    CategoryOfTiles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}