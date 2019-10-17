package pl.koszela.spring.GernateFile;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.Category;
import pl.koszela.spring.entities.EntityResultAccesories;
import pl.koszela.spring.entities.Tiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vaadin.flow.server.VaadinServletRequest.getCurrent;

public class GenerateOffer {

    private static final String FILE_NAME = "src/main/resources/templates/itext.pdf";
    private static final String TABLE = "src/main/resources/templates/Tabela.png";
    private static final String PREMIUM = "src/main/resources/templates/premium.png";
    private static final String LUX_PLUS = "src/main/resources/templates/luxPlus.png";
    private static final String STANDARD = "src/main/resources/templates/standard.png";


    public static void writeUsingIText() {

        Document document = new Document();

        try {
            EntityPersonalData userfromRepo = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
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
            Paragraph informacje = new Paragraph("\n\n\nInformacje handlowe przygotowane dla: " + userfromRepo.getName() + " " + userfromRepo.getSurname(), font12);

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

            Set<Tiles> resultSetTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");
            List<Tiles> resultListTilesFromRepo = new ArrayList<>(resultSetTilesFromRepo);

            BaseColor baseColor = new BaseColor(224, 224, 224);
            float[] width = new float[]{320f, 85f, 85f, 85f, 85f, 85f};

            String priceListName = "";

            List<Tiles> parents = resultListTilesFromRepo.stream().filter(e -> e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).collect(Collectors.toList());
            priceListName = parents.get(0).getPriceListName();

            cell(font12, table, baseColor, priceListName);
            cell(font12, table, baseColor, "Ilość");
            cell(font12, table, baseColor, "Cena detal");
            cell(font12, table, baseColor, "Cena zakupu");
            cell(font12, table, baseColor, "Cena po rabacie");
            cell(font12, table, baseColor, "Zysk");
            table.setWidths(width);
            table.setHeaderRows(1);

            getTable(document, font12, font10, table, resultListTilesFromRepo, priceListName);

            Set<EntityResultAccesories> set = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

            PdfPTable accesories = new PdfPTable(7);
            float[] width2 = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
            accesories.setWidths(width2);
            cell(font12, accesories, baseColor, "Nazwa");
            cell(font12, accesories, baseColor, "Ilość");
            cell(font12, accesories, baseColor, "Cena zakupu");
            cell(font12, accesories, baseColor, "Cena detal");
            cell(font12, accesories, baseColor, "Cena razem netto");
            cell(font12, accesories, baseColor, "Cena razem zakup");
            cell(font12, accesories, baseColor, "Zysk");

            for (EntityResultAccesories resultAccesories : set) {
                if (resultAccesories.isOffer()) {
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getName()), font10));
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getQuantity()), font10));
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getPricePurchase()), font10));
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getPriceRetail()), font10));
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getAllPriceRetail()), font10));
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getAllPricePurchase()), font10));
                    accesories.addCell(new Phrase(String.valueOf(resultAccesories.getProfit()), font10));
                }
            }
            document.add(accesories);

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

    private static void cell(Font font12, PdfPTable table, BaseColor baseColor, String priceListName) {
        PdfPCell header1 = new PdfPCell(new Phrase(priceListName, font12));
        header1.setBackgroundColor(baseColor);
        table.addCell(header1);
    }

    private static void getTable(Document document, Font font12, Font font10, PdfPTable
            table, List<Tiles> resultTiles, String priceListName) throws DocumentException {

        List<Tiles> parents = resultTiles.stream().filter(e -> e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).collect(Collectors.toList());
        priceListName = parents.get(0).getPriceListName();

        String totalPrice = "";
        List<Tiles> parentsOptional = resultTiles.stream().filter(e -> e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString()) && e.isOption()).collect(Collectors.toList());
        for (Tiles tile : resultTiles) {
            if (tile.getPriceListName().equals(parents.get(0).getPriceListName())) {
                table.addCell(new Phrase(StringUtils.capitalize(tile.getName().replace('_', ' ').toLowerCase()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getQuantity()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getPrice()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getPricePurchase()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getPriceAfterDiscount()), font10));
                table.addCell(new Phrase(String.valueOf(tile.getProfit()), font10));

                totalPrice = String.valueOf(parents.get(0).getTotalPrice());
            }
        }

        document.add(table);

        Paragraph suma = new Paragraph("\n\n\t\t\t\tElementy dachówkowe dla dachu " + parents.get(0).getPriceListName() + " : " + totalPrice + "\n\n\n", font12);

        Paragraph opcjonalne = new Paragraph("\n\n\t\t\t\tOferty Opcjonalne:" +
                " \n" + parentsOptional.get(0).getPriceListName() + " z ceną: " + parentsOptional.get(0).getTotalPrice() +
                "\n" + parentsOptional.get(1).getPriceListName() + " z ceną: " + parentsOptional.get(0).getTotalPrice() + "\n\n\n", font12);
        document.add(suma);

        document.add(opcjonalne);
    }
}
