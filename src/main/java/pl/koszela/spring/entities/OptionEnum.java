package pl.koszela.spring.entities;

public enum OptionEnum {
    GLOWNA("GŁÓWNA"),
    OPCJONALNA("OPCJONALNA"),
    BRAK("BRAK");

    String name;

    OptionEnum(String name) {
        this.name = name;
    }
}
