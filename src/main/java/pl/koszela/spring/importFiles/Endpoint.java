package pl.koszela.spring.importFiles;

public enum Endpoint {
    FILE_BOGEN_INNOVO_10_CZERWONA_ANGOBA_URL("src/main/resources/assets/Bogen Innovo 10 czerwona angoba.csv"),
    FILE_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA_URL("src/main/resources/assets/Bogen Innovo 10 miedziano-brazowa angoba.csv"),
    FILE_BOGEN_INNOVO_12_CZERWONA_ANGOBA_URL("src/main/resources/assets/Bogen Innovo 12 czerwona angoba.csv"),
    FILE_AKCESORIA_URL("src/main/resources/assets/akcesoria.csv"),
    FILE_OKNA_FAKRO_DAKEA_URL("src/main/resources/assets/Windows/Fakro.csv"),
    FILE_OKNA_VELUX_URL("src/main/resources/assets/Windows/Velux.csv"),
    FILE_COLLAR_FAKRO_URL("src/main/resources/assets/Collars/Fakro.csv"),
    FILE_COLLAR_VELUX_URL("src/main/resources/assets/Collars/Velux.csv"),
    FILE_ACCESORIES_WINDOWS_FAKRO_URL("src/main/resources/assets/accesoriesFromWindows/Fakro.csv"),
    FILE_ACCESORIES_WINDOWS_VELUX_URL("src/main/resources/assets/accesoriesFromWindows/Fakro.csv"),
    FILE_PLEWA_URL("src/main/resources/assets/Plewa.csv"),
    FILE_SYSTEM_PROTECTION_URL("src/main/resources/assets/System odgromowy.csv"),
    FILE_FLAMINGO_125x100_URL("src/main/resources/assets/Flamingo 125x100.csv"),
    FILE_FLAMINGO_125x90_URL("src/main/resources/assets/Flamingo 125x90.csv"),
    FILE_BRYZA_125x90_URL("src/main/resources/assets/Bryza 125x90.csv"),
    FILE_BRYZA_150x100_URL("src/main/resources/assets/Bryza 150x100.csv"),
    FILE_TO_GENERATE_OFFER_URL("src/main/resources/templates/offer.pdf");

    private String location;

    Endpoint(String location) {
        this.location = location;
    }

    public String location() {
        return location;
    }
}
