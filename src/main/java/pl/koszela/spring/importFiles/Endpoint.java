package pl.koszela.spring.importFiles;

public enum Endpoint {
    FILE_BOGEN_INNOVO_10_CZERWONA_ANGOBA_URL("src/main/resources/assets/Bogen Innovo 10 czerwona angoba.csv"),
    FILE_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA_URL("src/main/resources/assets/Bogen Innovo 10 miedziano-brazowa angoba.csv"),
    FILE_BOGEN_INNOVO_12_CZERWONA_ANGOBA_URL("src/main/resources/assets/Bogen Innovo 12 czerwona angoba.csv"),
    FILE_AKCESORIA_URL("src/main/resources/assets/akcesoria.csv"),
    FILE_OKNA_FAKRO_DAKEA_URL("src/main/resources/assets/Fakro.csv"),
    FILE_OKNA_VELUX_URL("src/main/resources/assets/Velux.csv"),
    FILE_KOLNIERZ_OKPOL_DAKEA_URL("src/main/resources/assets/KolnierzOkpolDakea.csv"),
    FILE_FLAMINGO_125x100_URL("src/main/resources/assets/Flamingo 125x100.csv"),
    FILE_FLAMINGO_125x90_URL("src/main/resources/assets/Flamingo 125x90.csv"),
    FILE_BRYZA_125x90_URL("src/main/resources/assets/Bryza 125x90.csv"),
    FILE_BRYZA_150x100_URL("src/main/resources/assets/Bryza 150x100.csv");

    private String location;

    Endpoint(String location) {
        this.location = location;
    }

    public String location() {
        return location;
    }
}
