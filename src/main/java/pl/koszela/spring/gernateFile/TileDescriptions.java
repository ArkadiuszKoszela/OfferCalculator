package pl.koszela.spring.gernateFile;

public enum TileDescriptions {

    INFO("www.nowoczesnebudowanie.pl" +
            "ul. Chemiczna 2" +
            "65-713 Zielona Góra" +
            "robert@nowoczesnebudowanie.pl" +
            "tel. 502680330"),

    NELSKAMRUBP("Dachówki ceramiczne Nelskamrubp produkowane są z najwyższej jakości surowców w nowoczesnej technologii. " +
            "Sprawdzone na przestrzeni wieków – dachówki ceramiczne zalicza się do najstarszych pokryć dachowych" +
            "–po dziś dzień stanowią synonim piękna, naturalności i bezpieczeństwa. Dachówki ceramiczne Braas " +
            "powstają z gliny, której wysoka jakość porównywalna jest z zaletami glinek leczniczych. Dachówki " +
            "te dodają dachom klasycznego uroku i ciepła. Zachwycają mnogością barw, dzięki którym mogą Państwo " +
            "nadać swojemu dachowi indywidualny charakter - zgodnie z własnym smakiem i pomysłem. " +
            "Rubin11V(K) to połączenie klasycznego piękna z innowacyjnymi rozwiązaniami technologicznymi opracowanymi " +
            "wraz z dekarskimi mistrzami. Model ten należy do grupy przesuwnych dachówek dużego formatu. " +
            "Godne zwrócenia uwagi są kolory bukowy i szary kryształ – dostępny tylko dla tego modelu.");

    String description;

    TileDescriptions(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
