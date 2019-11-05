package pl.koszela.spring.gernateFile;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.service.NotificationInterface;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateOffer {
    private final static Logger logger = Logger.getLogger(GenerateOffer.class);

    private static final String FILE_NAME = "src/main/resources/templates/offer.pdf";

    public static void writeUsingIText() {
        PersonalData userfromRepo = (PersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        Set<Tiles> tilesSet = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
        Set<Accesories> accesoriesSet = (Set<Accesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
        List<Gutter> gutterList = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
        Set<Collar> setCollars = (Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar");
        Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
        Set<AccesoriesWindows> setAccesoriesWindows = (Set<AccesoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows");

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

            document.open();

            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            BaseColor baseColor = new BaseColor(224, 224, 224);
            Font font = new Font(baseFont, 8);
            Font font10 = new Font(baseFont, 10);
            Font font12 = new Font(baseFont, 12);

            Paragraph info = new Paragraph("www.nowoczesnebudowanie.pl\n" +
                    "ul. Chemiczna 2\n" +
                    "65-713 Zielona Góra                                                              OFERTA HANDLOWA\n" +
                    "robert@nowoczesnebudowanie.pl\n" +
                    "tel. 502680330", font);
            info.setAlignment(Element.ALIGN_LEFT);
            document.add(info);

            Paragraph userInfo = new Paragraph("\n\n\nInformacje handlowe przygotowane dla: " + userfromRepo.getName() + " " + userfromRepo.getSurname(), font12);

            document.add(userInfo);

            Paragraph tileManufacturer = new Paragraph("\n\nDachówki ceramiczne Nelskamrubp produkowane są z najwyższej jakości surowców w nowoczesnej technologii." +
                    "Sprawdzone na przestrzeni wieków – dachówki ceramiczne zalicza się do najstarszych pokryć dachowych " +
                    "–po dziś dzień stanowią synonim piękna, naturalności i bezpieczeństwa. Dachówki ceramiczne Braas " +
                    "powstają z gliny, której wysoka jakość porównywalna jest z zaletami glinek leczniczych. Dachówki " +
                    "te dodają dachom klasycznego uroku i ciepła. Zachwycają mnogością barw, dzięki którym mogą Państwo " +
                    "nadać swojemu dachowi indywidualny charakter - zgodnie z własnym smakiem i pomysłem. " +
                    "Rubin11V(K) to połączenie klasycznego piękna z innowacyjnymi rozwiązaniami technologicznymi opracowanymi " +
                    "wraz z dekarskimi mistrzami. Model ten należy do grupy przesuwnych dachówek dużego formatu. " +
                    "Godne zwrócenia uwagi są kolory bukowy i szary kryształ – dostępny tylko dla tego modelu.\n\n\n", font10);

            document.add(tileManufacturer);

            Image img = Image.getInstance("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png");
            img.scaleAbsolute(80f, 50f);
            img.setAbsolutePosition(450f, 750f);
            document.add(img);

            PdfPTable tilesTable = new PdfPTable(6);

            List<Tiles> allTiles = new ArrayList<>(tilesSet);

            float[] widthToTableTiles = new float[]{320f, 85f, 85f, 85f, 85f, 85f};

            Optional<Tiles> mainParent = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).findFirst();
            Optional<Tiles> optionParent = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isOption()).findAny();

            if (mainParent.isPresent()) {
                Paragraph mainOffer = new Paragraph("\n\n\t\t\t\tElementy dachówkowe dla dachu " + mainParent.get().getPriceListName() + " : " + mainParent.get().getTotalPrice() + "\n\n\n", font12);
                document.add(mainOffer);
                Image image = Image.getInstance(mainParent.get().getImageUrl());
                image.scaleAbsolute(80f, 50f);
                document.add(image);

                cell(font12, tilesTable, baseColor, mainParent.get().getPriceListName());
                cell(font12, tilesTable, baseColor, "Ilość");
                cell(font12, tilesTable, baseColor, "Cena detal");
                cell(font12, tilesTable, baseColor, "Cena zakupu");
                cell(font12, tilesTable, baseColor, "Cena po rabacie");
                cell(font12, tilesTable, baseColor, "Zysk");
                tilesTable.setWidths(widthToTableTiles);
                tilesTable.setHeaderRows(1);

                getTableTiles(document, font10, tilesTable, allTiles);
            } else {
                NotificationInterface.notificationOpen("Nie został wybrany główny dach", NotificationVariant.LUMO_ERROR);
                logger.debug("No option has been selected in the tile table");
            }

            if (optionParent.isPresent()) {
                String string = " \n" + optionParent.get().getPriceListName() + " z ceną: " + optionParent.get().getTotalPrice();
                Paragraph optionalOffers = new Paragraph("\n\n\t\t\t\tOferty Opcjonalne:" +
                        string, font12);
                document.add(optionalOffers);
            }

            PdfPTable tableAccesories = new PdfPTable(7);
            float[] widthToTableAccesories = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
            tableAccesories.setWidths(widthToTableAccesories);
            cell(font12, tableAccesories, baseColor, "Nazwa");
            cell(font12, tableAccesories, baseColor, "Ilość");
            cell(font12, tableAccesories, baseColor, "Cena zakupu");
            cell(font12, tableAccesories, baseColor, "Cena detal");
            cell(font12, tableAccesories, baseColor, "Cena razem netto");
            cell(font12, tableAccesories, baseColor, "Cena razem zakup");
            cell(font12, tableAccesories, baseColor, "Zysk");

            for (Accesories accesories : accesoriesSet) {
                if (accesories.isOffer()) {
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getName()), font10));
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getQuantity()), font10));
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getUnitPurchasePrice()), font10));
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getUnitDetalPrice()), font10));
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getAllpriceAfterDiscount()), font10));
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getAllpricePurchase()), font10));
                    tableAccesories.addCell(new Phrase(String.valueOf(accesories.getAllprofit()), font10));
                }
            }
            Paragraph spaces = new Paragraph("\n\n\n\n");
            document.add(spaces);
            document.add(tableAccesories);
            Paragraph spaces1 = new Paragraph("\n\n\n\n");
            document.add(spaces1);

            PdfPTable tableGutter = new PdfPTable(6);
            float[] widthToTableGutter = new float[]{320f, 85f, 85f, 85f, 85f, 85f};
            tableGutter.setWidths(widthToTableGutter);

            Optional<Gutter> mainGutter = gutterList.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isMain()).findFirst();
            Optional<Gutter> optionGutter = gutterList.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isOption()).findFirst();

            if (mainGutter.isPresent()) {
                cell(font12, tableGutter, baseColor, mainGutter.get().getCategory());
                cell(font12, tableGutter, baseColor, "Ilość");
                cell(font12, tableGutter, baseColor, "Cena zakupu");
                cell(font12, tableGutter, baseColor, "Cena detal");
                cell(font12, tableGutter, baseColor, "Cena razem netto");
                cell(font12, tableGutter, baseColor, "Zysk");

                createTableGutter(document, font10, tableGutter, gutterList);
            }

            PdfPTable tableWindows = new PdfPTable(7);
            float[] widthToTableWindows = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
            tableWindows.setWidths(widthToTableWindows);

            cell(font12, tableWindows, baseColor, "Nazwa");
            cell(font12, tableWindows, baseColor, "Ilość");
            cell(font12, tableWindows, baseColor, "Cena zakupu");
            cell(font12, tableWindows, baseColor, "Cena detal");
            cell(font12, tableWindows, baseColor, "Cena razem netto");
            cell(font12, tableWindows, baseColor, "Cena razem zakup");
            cell(font12, tableWindows, baseColor, "Zysk");

            for (Windows windows : setWindows) {
                if (windows.isOffer()) {
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getName()), font10));
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getQuantity()), font10));
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getUnitPurchasePrice()), font10));
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getUnitDetalPrice()), font10));
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getAllpriceAfterDiscount()), font10));
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getAllpricePurchase()), font10));
                    tableWindows.addCell(new Phrase(String.valueOf(windows.getAllprofit()), font10));
                }
            }

            Paragraph spaces2 = new Paragraph("\n\n\n\n");
            document.add(spaces2);
            document.add(tableWindows);

            PdfPTable tableCollars = new PdfPTable(7);
            float[] widthToTableCollars = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
            tableCollars.setWidths(widthToTableCollars);

            cell(font12, tableCollars, baseColor, "Nazwa");
            cell(font12, tableCollars, baseColor, "Ilość");
            cell(font12, tableCollars, baseColor, "Cena zakupu");
            cell(font12, tableCollars, baseColor, "Cena detal");
            cell(font12, tableCollars, baseColor, "Cena razem netto");
            cell(font12, tableCollars, baseColor, "Cena razem zakup");
            cell(font12, tableCollars, baseColor, "Zysk");

            for (Collar collar : setCollars) {
                if (collar.isOffer()) {
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getName()), font10));
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getQuantity()), font10));
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getUnitPurchasePrice()), font10));
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getUnitDetalPrice()), font10));
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getAllpriceAfterDiscount()), font10));
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getAllpricePurchase()), font10));
                    tableCollars.addCell(new Phrase(String.valueOf(collar.getAllprofit()), font10));
                }
            }

            Paragraph spaces3 = new Paragraph("\n\n\n\n");
            document.add(spaces3);
            document.add(tableCollars);

            PdfPTable tableAccesoriesWindows = new PdfPTable(7);
            float[] widthToTableAccesoriesWindows = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
            tableAccesoriesWindows.setWidths(widthToTableAccesoriesWindows);

            cell(font12, tableAccesoriesWindows, baseColor, "Nazwa");
            cell(font12, tableAccesoriesWindows, baseColor, "Ilość");
            cell(font12, tableAccesoriesWindows, baseColor, "Cena zakupu");
            cell(font12, tableAccesoriesWindows, baseColor, "Cena detal");
            cell(font12, tableAccesoriesWindows, baseColor, "Cena razem netto");
            cell(font12, tableAccesoriesWindows, baseColor, "Cena razem zakup");
            cell(font12, tableAccesoriesWindows, baseColor, "Zysk");

            for (AccesoriesWindows accesoriesWindows : setAccesoriesWindows) {
                if (accesoriesWindows.isOffer()) {
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getName()), font10));
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getQuantity()), font10));
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getUnitPurchasePrice()), font10));
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getUnitDetalPrice()), font10));
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getAllpriceAfterDiscount()), font10));
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getAllpricePurchase()), font10));
                    tableAccesoriesWindows.addCell(new Phrase(String.valueOf(accesoriesWindows.getAllprofit()), font10));
                }
            }

            Paragraph spaces4 = new Paragraph("\n\n\n\n");
            document.add(spaces4);
            document.add(tableAccesoriesWindows);

            document.close();

            logger.info("Create offer for - " + userfromRepo.getName() + " " + userfromRepo.getSurname());
        } catch (DocumentException |
                IOException e) {
            e.printStackTrace();
        }
    }

    private static void createTableGutter(Document document, Font font, PdfPTable gutterTable, List<Gutter> listGutter) throws DocumentException {
        List<Gutter> parent = listGutter.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isMain()).collect(Collectors.toList());

        for (Gutter gutter : listGutter) {
            if (gutter.getCategory().equals(parent.get(0).getCategory()) && gutter.getUnitDetalPrice() != 0) {
                gutterTable.addCell(new Phrase(gutter.getName(), font));
                gutterTable.addCell(new Phrase(String.valueOf(gutter.getQuantity()), font));
                gutterTable.addCell(new Phrase(String.valueOf(gutter.getUnitDetalPrice()), font));
                gutterTable.addCell(new Phrase(String.valueOf(gutter.getAllpricePurchase()), font));
                gutterTable.addCell(new Phrase(String.valueOf(gutter.getAllpriceAfterDiscount()), font));
                gutterTable.addCell(new Phrase(String.valueOf(gutter.getAllprofit()), font));
            }
        }
        document.add(gutterTable);
    }

    private static void cell(Font font, PdfPTable table, BaseColor baseColor, String priceListName) {
        PdfPCell header1 = new PdfPCell(new Phrase(priceListName, font));
        header1.setBackgroundColor(baseColor);
        table.addCell(header1);
    }

    private static void getTableTiles(Document document, Font font10, PdfPTable
            tilesTable, List<Tiles> resultTiles) throws DocumentException {

        List<Tiles> parents = resultTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).collect(Collectors.toList());

        for (Tiles tile : resultTiles) {
            if (tile.getPriceListName().equals(parents.get(0).getPriceListName())) {
                tilesTable.addCell(new Phrase(StringUtils.capitalize(tile.getName().replace('_', ' ').toLowerCase()), font10));
                tilesTable.addCell(new Phrase(String.valueOf(tile.getQuantity()), font10));
                tilesTable.addCell(new Phrase(String.valueOf(tile.getUnitDetalPrice()), font10));
                tilesTable.addCell(new Phrase(String.valueOf(tile.getAllpricePurchase()), font10));
                tilesTable.addCell(new Phrase(String.valueOf(tile.getAllpriceAfterDiscount()), font10));
                tilesTable.addCell(new Phrase(String.valueOf(tile.getAllprofit()), font10));
            }
        }
        document.add(tilesTable);
    }
}