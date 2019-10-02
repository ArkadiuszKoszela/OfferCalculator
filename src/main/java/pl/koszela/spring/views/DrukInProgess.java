package pl.koszela.spring.views;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrukInProgess {

    private static final String FILE_NAME = "src/main/resources/templates/itext.pdf";
    private static final String TABLE = "src/main/resources/templates/Tabela.png";
    private static final String PREMIUM = "src/main/resources/templates/premium.png";
    private static final String LUX_PLUS = "src/main/resources/templates/luxPlus.png";
    private static final String STANDARD = "src/main/resources/templates/standard.png";

    public static void main(String[] args) {
        writeUsingIText();
    }

    private static void writeUsingIText() {

        Document document = new Document();

        /*EntityUser user = (EntityUser) VaadinSession.getCurrent().getAttribute("user");*/
        /*EntityTiles entityTiles = new EntityTiles();
        entityTiles.setPriceListName("Bogen Innovo 10");
        entityTiles.setType("Czerwona Angoba");
        entityTiles.setName("Dachówka podstawowa");
        entityTiles.setUnitRetailPrice(new BigDecimal(3.45));
        entityTiles.setProfit(20);
        entityTiles.setBasicDiscount(40);
        entityTiles.setSupplierDiscount(34);
        entityTiles.setAdditionalDiscount(23);
        entityTiles.setSkontoDiscount(4);
        entityTiles.setPriceAfterDiscount("po rabacie");
        entityTiles.setPurchasePrice("zakup");
        entityTiles.setProfitCalculate("zysk");*/

        try {

            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

            //open
            document.open();

            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

            Font font = new Font(baseFont, 8);
            Paragraph p = new Paragraph("www.nowoczesnebudowanie.pl\n" +
                    "ul. Chemiczna 2\n" +
                    "65-713 Zielona Góra                                                              OFERTA HANDLOWA\n" +
                    "robert@nowoczesnebudowanie.pl\n" +
                    "tel. 502680330", font);
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            Font font12 = new Font(baseFont, 12);
            Paragraph informacje = new Paragraph("\n\n\nInformacje handlowe przygotowane dla: ", font12);

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
                    "Godne zwrócenia uwagi są kolory bukowy i szary kryształ – dostępny tylko dla tego modelu.", font10);

            document.add(producent);

            Image img = Image.getInstance("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png");
            img.scaleAbsolute(80f,50f);
            img.setAbsolutePosition(450f, 750f);
            document.add(img);

            float [] pointColumnWidths = {250F, 100F, 100F,100F,100F,100F};
            PdfPTable table = new PdfPTable(pointColumnWidths);
            
            /*table.addCell(new Paragraph(entityTiles.getType()));
            table.addCell(new Paragraph(entityTiles.getName()));
            table.addCell(new Paragraph(entityTiles.getPriceListName()));
            table.addCell(new Paragraph(entityTiles.getPriceAfterDiscount()));
            table.addCell(new Paragraph(entityTiles.getPurchasePrice()));
            table.addCell(new Paragraph(entityTiles.getProfitCalculate()));*/

            document.add(table);

            Paragraph paragraph = new Paragraph("\n\n                   DODATKI DACHOWE:  jakaś cena\n" +
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

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }
}
