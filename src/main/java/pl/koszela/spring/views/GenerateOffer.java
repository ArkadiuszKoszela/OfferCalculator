package pl.koszela.spring.views;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.Category;
import pl.koszela.spring.entities.Tiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.vaadin.flow.server.VaadinServletRequest.getCurrent;

class GenerateOffer {

    private static final String FILE_NAME = "src/main/resources/templates/itext.pdf";
    private static final String TABLE = "src/main/resources/templates/Tabela.png";
    private static final String PREMIUM = "src/main/resources/templates/premium.png";
    private static final String LUX_PLUS = "src/main/resources/templates/luxPlus.png";
    private static final String STANDARD = "src/main/resources/templates/standard.png";


    static void writeUsingIText() {

        Document document = new Document();

        try {
            EntityPersonalData user = (EntityPersonalData) VaadinSession.getCurrent().getAttribute("personalData");
            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

            //open
            document.open();

            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

            Font font = new Font(baseFont, 8);
            if (user != null) {
                Paragraph p = new Paragraph("www.nowoczesnebudowanie.pl\n" +
                        "ul. Chemiczna 2\n" +
                        "65-713 Zielona Góra                                                              OFERTA HANDLOWA\n" +
                        "robert@nowoczesnebudowanie.pl\n" +
                        "tel. 502680330", font);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);
            }

            Font font12 = new Font(baseFont, 12);
            assert user != null;
            Paragraph informacje = new Paragraph("\n\n\nInformacje handlowe przygotowane dla: " + user.getName() + " " + user.getSurname(), font12);

            document.add(informacje);

            Font font10 = new Font(baseFont, 10);
            Paragraph producent = new Paragraph("\n\nDachówki ceramiczne Nelskamrubp produkowane są z najwyższej jakości surowców w nowoczesnej technologii." +
                    "Sprawdzone na przestrzeni wieków – dachówki ceramiczne zalicza się do najstarszych pokryć dachowych " +
                    "–po dziś dzień stanowią synonim piękna, naturalności i bezpieczeństwa. Dachówki ceramiczne Braas " +
                    "powstają z gliny, której wysoka jakość porównywalna jest z zaletami glinek leczniczych. Dachówki " +
                    "te dodają dachom klasycznego uroku i ciepła. Zachwycają mnogością barw, dzięki którym mogą Państwo " +
                    "nadać swojemu dachowi indywidualny charakter - zgodnie z własnym smakiem i pomysłem. " +
                    "Rubin11V(K) to połączenie klasycznego piękna z innowacyjnymi rozwiązaniami technologicznymi opracowanymi " +
                    "wraz z dekarskimi mistrzami. Model ten należy do grupy przesuwnych dachówek dużego formatu. " +
                    "Godne zwrócenia uwagi są kolory bukowy i szary kryształ – dostępny tylko dla tego modelu.\n\n\n", font10);

            document.add(producent);

            Image img = Image.getInstance("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png");
            img.scaleAbsolute(80f, 50f);
            img.setAbsolutePosition(450f, 750f);
            document.add(img);

            PdfPTable table = new PdfPTable(6);

            Set<Tiles> resultTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");

            List<List<Tiles>> resultTiles = (List<List<Tiles>>) VaadinSession.getCurrent().getAttribute("resultTiles");

            BaseColor baseColor = new BaseColor(224, 224, 224);
            float[] width = new float[]{320f, 85f, 85f, 85f, 85f, 85f};

            List<String> priceListName = new ArrayList<>();

            PdfPCell header1 = new PdfPCell(new Phrase("Sprawdzam", font12));
            header1.setBackgroundColor(baseColor);
            table.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Phrase("Ilość", font12));
            header2.setBackgroundColor(baseColor);
            table.addCell(header2);

            PdfPCell header3 = new PdfPCell(new Phrase("Cena detal", font12));
            header3.setBackgroundColor(baseColor);
            table.addCell(header3);

            PdfPCell header4 = new PdfPCell(new Phrase("Cena zakupu", font12));
            header4.setBackgroundColor(baseColor);
            table.addCell(header4);

            PdfPCell header5 = new PdfPCell(new Phrase("Cena po rabacie", font12));
            header5.setBackgroundColor(baseColor);
            table.addCell(header5);

            PdfPCell header6 = new PdfPCell(new Phrase("Zysk", font12));
            header6.setBackgroundColor(baseColor);
            table.addCell(header6);
            table.setWidths(width);
            table.setHeaderRows(1);

            if (resultTilesFromRepo != null) {
                getTable(document, font12, font10, table, resultTiles, priceListName);
            }else {
                getTable(document, font12, font10, table, resultTiles, priceListName);
            }
            Paragraph paragraph = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n                   DODATKI DACHOWE:  jakaś cena\n" +
                    "                   CEGŁA KLINKIEROWA + zaprawa:  jakaś cena\n" +
                    "                   Łata i kontrłata:  jakaś cena\n" +
                    "                   Okna dachowe + kołnierze:  jakaś cena\n" +
                    "                   System rynnowy:  jakaś cena\n" +
                    "                   Podbitka:  jakaś cena\n" +
                    "                   System odgromowy:  jakaś cena\n" +
                    "                   Elementy montażowe:  jakaś cena\n" +
                    "                   Elementy dachówkowe:  jakaś cena\n" +
                    "                   RAZEM netto:  jakaś cena\n" +
                    "                   Wykonastwo:  jakaś cena\n" +
                    "                   Transport:  jakaś cena", font10);

            document.add(paragraph);

            Paragraph pakiety = new Paragraph("\n\nPakiety, które przygotowaliśmy dla Państawa: ", font10);
            Image image = Image.getInstance(TABLE);
            image.scaleAbsolute(400f, 200f);
            Image luxPlus = Image.getInstance(LUX_PLUS);
            luxPlus.scaleAbsolute(50f, 50f);
            luxPlus.setAbsolutePosition(200f, 0f);
            Image premium = Image.getInstance(PREMIUM);
            premium.scaleAbsolute(60f, 60f);
            premium.setAbsolutePosition(270f, 0f);
            Image standard = Image.getInstance(STANDARD);
            standard.scaleAbsolute(50f, 50f);
            standard.setAbsolutePosition(340f, 0f);

            document.add(pakiety);
            document.add(standard);
            document.add(premium);
            document.add(luxPlus);
            document.add(image);

            //close
            document.close();

            System.out.println("Done");

        } catch (DocumentException |
                IOException e) {
            e.printStackTrace();
        }

    }

    private static void getTable(Document document, Font font12, Font font10, PdfPTable table, List<List<Tiles>> resultTiles, List<String> priceListName) throws DocumentException {
        resultTiles.get(0).forEach(e -> {
            if (e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())) {
                priceListName.add(e.getPriceListName());
            }
        });


        String totalPrice = "";
        for (List<Tiles> list : resultTiles) {
            for (Tiles tile : list) {
                table.addCell(new Phrase(StringUtils.capitalize(tile.getName().replace('_', ' ').toLowerCase()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getQuantity()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getPrice()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getPricePurchase()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getPriceAfterDiscount()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getProfit()), font10));
                if (tile.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())) {
                    totalPrice = String.valueOf(tile.getTotalPrice());
                }
            }
        }
        document.add(table);

        Paragraph suma = new Paragraph("\n\n\t\t\t\tElementy dachówkowe: " + totalPrice, font12);
        document.add(suma);
    }
}
