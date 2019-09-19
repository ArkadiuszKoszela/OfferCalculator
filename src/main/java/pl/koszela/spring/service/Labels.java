package pl.koszela.spring.service;

import com.vaadin.flow.component.html.Label;
import org.springframework.stereotype.Service;

@Service
public class Labels {

    public static final String TASMA_KELNICOWA = "Taśma kalenicowa";
    public static final String WSPORNIK_LATY_KALENICOWEJ = "Wspornik łaty kalenicowej";
    public static final String TASMA_DO_OBROBKI_KOMINA = "Taśma do obróbki komina";
    public static final String LISTWA_WYKONCZENIOWA_ALUMINIOWA = "Listwa wykończeniowa aluminiowa";
    public static final String KOSZ_DACHOWY_ALUMINIOWY_2MB = "Kosz dachowy aluminiowy 2mb";
    public static final String KLAMRA_DO_MOCOWANIA_KOSZA = "Klamra do mocowania kosza";
    public static final String KLIN_USZCZELNIAJACY_KOSZ = "Klin uszczelniający kosz";
    public static final String GRZEBIEN_OKAPOWY = "Grzebień okapowy";
    public static final String KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM = "Kratka zabezpieczająca przed ptactwem";
    public static final String PAS_OKAPOWY = "Pas okapowy";
    public static final String KLAMRA_DO_GASIORA = "Klamra do gąsiora";
    public static final String SPINKA_DO_DACHOWKI = "Spinka do dachówki";
    public static final String SPINKA_DO_DACHOWKI_CIETEJ = "Spinka do dachówki ciętej";
    public static final String LAWA_KOMINIARSKA = "Ława Kominiarska";
    public static final String STOPIEN_KOMINIARSKI = "Stopień kominiarski";
    public static final String PLOTEK_PRZECIWSNIEGOWY_155MMX2MB = "Płotek przeciwśniegowy 155mm x 2mb";
    public static final String PLOTEK_PRZECIWSNIEGOWY_155MMX3MB = "Płotek przeciwśniegowy 155mm x 3mb";
    public static final String MEMBRANA_DACHOWA = "Membrana dachowa";
    public static final String TASMA_DO_LACZENIA_MEMBRAN_I_FOLII = "Taśma do łączenia membran i folii";
    public static final String TASMA_REPARACYJNA = "Taśma reparacyjna";
    public static final String BLACHA_ALUMINIOWA = "Blacha aluminiowa";
    public static final String CEGLA_KLINKIEROWA = "Cegła klinkierowa";
    public static final String LATA = "Łata";
    public static final String KONTRLATA = "Kontrłata";
    public static final String WYLAZ_DACHOWY = "Wyłaz dachowy";

    public static Label getLabel(String name){
        return new Label(name);
    }
}
