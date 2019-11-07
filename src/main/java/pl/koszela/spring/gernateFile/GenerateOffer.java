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

    public static void writeUsingIText(String location) {
        PersonalData userfromRepo = (PersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        Set<Tiles> tilesSet = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
        Set<Accesories> accesoriesSet = (Set<Accesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
        List<Gutter> gutterList = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
        Set<Collar> setCollars = (Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar");
        Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
        Set<AccesoriesWindows> setAccesoriesWindows = (Set<AccesoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows");

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(location)));

            document.open();

            final BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            final BaseColor baseColor = new BaseColor(224, 224, 224);
            final Font font = new Font(baseFont, 8);
            final Font font10 = new Font(baseFont, 10);
            final Font font12 = new Font(baseFont, 12);

            Paragraph info = new Paragraph(TileDescriptions.INFO.description, font);
            info.setAlignment(Element.ALIGN_LEFT);
            document.add(info);

            Paragraph userInfo = new Paragraph("\n\n\nInformacje handlowe przygotowane dla: " + userfromRepo.getName() + " " + userfromRepo.getSurname(), font12);

            document.add(userInfo);

            Paragraph tileManufacturer = new Paragraph("\n\n" + TileDescriptions.NELSKAMRUBP.description + "\n\n\n", font10);

            document.add(tileManufacturer);

            Image img = Image.getInstance("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png");
            img.scaleAbsolute(80f, 50f);
            img.setAbsolutePosition(450f, 750f);
            document.add(img);

            List<Tiles> allTiles = new ArrayList<>(tilesSet);

            PdfPTable tilesTable = createTable();

            Optional<Tiles> mainParent = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).findFirst();
            Optional<Tiles> optionParent = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isOption()).findAny();

            if (mainParent.isPresent()) {
                Paragraph mainOffer = new Paragraph("\n\n\t\t\t\tElementy dachówkowe dla dachu " + mainParent.get().getPriceListName() + " : " + mainParent.get().getTotalPrice() + "\n\n\n", font12);
                document.add(mainOffer);
//                Image image = Image.getInstance(mainParent.get().getImageUrl());
//                image.scaleAbsolute(80f, 50f);
//                document.add(image);

                columnHeader(baseColor, font12, tilesTable, mainParent.get().getPriceListName());
                tilesTable.setHeaderRows(1);
                Set<Tiles> parents = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).collect(Collectors.toSet());
                Set<Tiles> childrens = allTiles.stream().filter(e -> e.getPriceListName().equals(parents.iterator().next().getPriceListName())).collect(Collectors.toSet());
                childrens.addAll(parents);
                table(childrens, font10, tilesTable);
                document.add(tilesTable);
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

            PdfPTable tableAccesories = createTable();
            columnHeader(baseColor, font12, tableAccesories, "Nazwa");
            Set<Accesories> accesories = accesoriesSet.stream().filter(Accesories::isOffer).collect(Collectors.toSet());
            table(accesories, font10, tableAccesories);
            addSpacesAndTableToDocument(document, tableAccesories);

            PdfPTable tableGutter = createTable();
            Optional<Gutter> mainGutter = gutterList.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isMain()).findFirst();

            if (mainGutter.isPresent()) {
                columnHeader(baseColor, font12, tableGutter, mainGutter.get().getCategory());
                Set<Gutter> parent = gutterList.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isMain()).collect(Collectors.toSet());
                table(parent, font10, tableGutter);
                addSpacesAndTableToDocument(document, tableGutter);
            }

            PdfPTable tableWindows = createTable();
            columnHeader(baseColor, font12, tableWindows, "Nazwa");
            table(setWindows, font10, tableWindows);
            addSpacesAndTableToDocument(document, tableWindows);

            PdfPTable tableCollars = createTable();
            columnHeader(baseColor, font12, tableCollars, "Nazwa");
            table(setCollars, font10, tableCollars);
            addSpacesAndTableToDocument(document, tableCollars);

            PdfPTable tableAccesoriesWindows = createTable();
            columnHeader(baseColor, font12, tableAccesoriesWindows, "Nazwa");
            table(setAccesoriesWindows, font10, tableAccesoriesWindows);
            addSpacesAndTableToDocument(document, tableAccesoriesWindows);

            document.close();

            logger.info("Create offer for - " + userfromRepo.getName() + " " + userfromRepo.getSurname());
        } catch (DocumentException |
                IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSpacesAndTableToDocument(Document document, PdfPTable tableAccesories) throws DocumentException {
        Paragraph spaces = new Paragraph("\n\n\n\n");
        document.add(spaces);
        document.add(tableAccesories);
    }

    private static PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        float[] widthToTable = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
        table.setWidths(widthToTable);
        return table;
    }

    private static void columnHeader(BaseColor baseColor, Font font12, PdfPTable tableAccesories, String firstColumnHeader) {
        cell(font12, tableAccesories, baseColor, firstColumnHeader);
        cell(font12, tableAccesories, baseColor, "Ilość");
        cell(font12, tableAccesories, baseColor, "Cena zakupu");
        cell(font12, tableAccesories, baseColor, "Cena detal");
        cell(font12, tableAccesories, baseColor, "Cena razem netto");
        cell(font12, tableAccesories, baseColor, "Cena razem zakup");
        cell(font12, tableAccesories, baseColor, "Zysk");
    }

    private static void table(Set<? extends BaseEntity> accesoriesSet, Font font10, PdfPTable pdfPTable) {
        for (BaseEntity baseEntity : accesoriesSet) {
            if (!baseEntity.getUnitDetalPrice().equals(0d)) {
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getName()), font10));
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getQuantity()), font10));
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getUnitPurchasePrice()), font10));
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getUnitDetalPrice()), font10));
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getAllpriceAfterDiscount()), font10));
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getAllpricePurchase()), font10));
                pdfPTable.addCell(new Phrase(String.valueOf(baseEntity.getAllprofit()), font10));
            }
        }
    }

    private static void cell(Font font, PdfPTable table, BaseColor baseColor, String priceListName) {
        PdfPCell header1 = new PdfPCell(new Phrase(priceListName, font));
        header1.setBackgroundColor(baseColor);
        table.addCell(header1);
    }
}