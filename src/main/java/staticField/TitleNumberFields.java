package staticField;

public enum TitleNumberFields {
    POWIERZCHNIA_POLACI("Powierzchnia połaci"),
    DLUGOSC_KALENIC("Długość kalenic"),
    DLUGOSC_KALENIC_SKOSNYCH("Długość kalenic skośnych"),
    DLUGOSC_KALENIC_PROSTYCH("Długość kalenic prostych"),
    DLUGOSC_KOSZY("Długość koszy"),
    DLUGOSC_KRAWEDZI_LEWYCH("Długość krawędzi lewych"),
    DLUGOSC_KRAWEDZI_PRAWYCH("Długość krawędzi prawych"),
    OBWOD_OKAPU("Obwód komina"),
    DLUGOSC_OKAPU("Długość okapu"),
    DACHOWKA_WENTYLACYJNA("Dachówka wentylacyjna"),
    KOMPLET_KOMINKA_WENTYLACYJNEGO("Komplet kominka wentylacyjnego"),
    GASIAR_POCZATKOWY_KALENICA_PROSTA("Gąsior początkowy kalenica prosta"),
    GASIAR_KONCOWY_KALENICA_PROSTA("Gąsior końcowy kalenica prosta"),
    GASIAR_ZAOKRAGLONY("Gąsior zaokrąglony"),
    TROJNIK("Trójnik"),
    CZWRONIK("Czwórnik"),
    GASIAR_Z_PODWOJNA_MUFA("Gąsior z podwójną mufą"),
    DACHOWKA_DWUFALOWA("Dachówka dwufalowa"),
    OKNO_POLACIOWE("Okno połaciowe"),
    ODCINEK_1("Odcinek 1"),
    ODCINEK_2("Odcinek 2"),
    ODCINEK_3("Odcinek 3"),
    ODCINEK_4("Odcinek 4"),
    ODCINEK_5("Odcinek 5"),
    ODCINEK_6("Odcinek 6"),
    ODCINEK_7("Odcinek 7"),
    ODCINEK_8("Odcinek 8"),
    ODCINEK_9("Odcinek 9"),
    ODCINEK_10("Odcinek 10"),
    ODCINEK_11("Odcinek 11"),
    ODCINEK_12("Odcinek 12"),
    RYNNA_3MB_ODC_1("Rynna 3mb odc. 1"),
    RYNNA_3MB_ODC_2("Rynna 3mb odc. 2"),
    RYNNA_3MB_ODC_3("Rynna 3mb odc. 3"),
    RYNNA_3MB_ODC_4("Rynna 3mb odc. 4"),
    RYNNA_3MB_ODC_5("Rynna 3mb odc. 5"),
    RYNNA_3MB_ODC_6("Rynna 3mb odc. 6"),
    RYNNA_3MB_ODC_7("Rynna 3mb odc. 7"),
    RYNNA_3MB_ODC_8("Rynna 3mb odc. 8"),
    RYNNA_3MB_ODC_9("Rynna 3mb odc. 9"),
    RYNNA_3MB_ODC_10("Rynna 3mb odc. 10"),
    RYNNA_3MB_ODC_11("Rynna 3mb odc. 11"),
    RYNNA_3MB_ODC_12("Rynna 3mb odc. 12"),
    RYNNA_4MB_ODC_1("Rynna 4mb odc. 1"),
    RYNNA_4MB_ODC_2("Rynna 4mb odc. 2"),
    RYNNA_4MB_ODC_3("Rynna 4mb odc. 3"),
    RYNNA_4MB_ODC_4("Rynna 4mb odc. 4"),
    RYNNA_4MB_ODC_5("Rynna 4mb odc. 5"),
    RYNNA_4MB_ODC_6("Rynna 4mb odc. 6"),
    RYNNA_4MB_ODC_7("Rynna 4mb odc. 7"),
    RYNNA_4MB_ODC_8("Rynna 4mb odc. 8"),
    RYNNA_4MB_ODC_9("Rynna 4mb odc. 9"),
    RYNNA_4MB_ODC_10("Rynna 4mb odc. 10"),
    RYNNA_4MB_ODC_11("Rynna 4mb odc. 11"),
    RYNNA_4MB_ODC_12("Rynna 4mb odc. 12"),
    SZTUCER("Sztucer"),
    DENKO("Denko"),
    LACZNIK_RYNNY("łącznik rynny"),
    NAROZNIK_WEW("Narożnik wew"),
    NAROZNIK_ZEW("Narożnik zew");

    String name;

    TitleNumberFields(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
