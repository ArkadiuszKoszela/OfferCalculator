package pl.koszela.spring.gernateFile;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.apache.log4j.Logger;
import pl.koszela.spring.entities.main.*;
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
        Set<Accessories> accessoriesSet = (Set<Accessories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
        List<Gutter> gutterList = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
        Set<Collar> setCollars = (Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar");
        Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
        Set<AccessoriesWindows> setAccessoriesWindows = (Set<AccessoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows");
        List<Fireside> listFireside = (List<Fireside>) VaadinSession.getCurrent().getSession().getAttribute("fireside");

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
            Set<Accessories> accesories1 = accessoriesSet.stream().filter(Accessories::isOffer).collect(Collectors.toSet());
            for (Accessories accessories : accesories1) {
                if (!accessories.getUrlToDownloadFile().equals("")) {
                    Phrase phrase = new Phrase();
                    phrase.add("W celu pobrania instrukcji ");
                    Font red = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
                    Chunk chunk = new Chunk("kliknij tutaj", red);
                    chunk.setAnchor(accessories.getUrlToDownloadFile());
                    phrase.add(chunk);
                    document.add(phrase);
                }
            }

            Image img = Image.getInstance("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png");
            img.scaleAbsolute(80f, 50f);
            img.setAbsolutePosition(450f, 750f);
            document.add(img);

            List<Tiles> allTiles = new ArrayList<>(tilesSet);

            PdfPTable tilesTable = createTable();

            Optional<Tiles> mainParent = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isMain()).findFirst();
            Optional<Tiles> optionParent = allTiles.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString()) && e.isOption()).findAny();

            if (mainParent.isPresent()) {
                Paragraph mainOffer = new Paragraph("\n\n\t\t\t\tElementy dachówkowe dla dachu " + mainParent.get().getManufacturer() + " : " + mainParent.get().getTotalPrice() + "\n\n\n", font12);
                document.add(mainOffer);
                Set<Tiles> childrens = allTiles.stream().filter(e -> e.getManufacturer().equals(mainParent.get().getManufacturer())).collect(Collectors.toSet());
                childrens.add(mainParent.get());
//                Image image = Image.getInstance(mainParent.get().getImageUrl());
//                image.scaleAbsolute(80f, 50f);
//                document.add(image);

                columnHeader(baseColor, font12, tilesTable, mainParent.get().getManufacturer());
                table(new ArrayList<>(childrens), font10, tilesTable);
                document.add(tilesTable);
                addSpacesAndTableToDocument(document, tilesTable);
            } else {
                NotificationInterface.notificationOpen("Nie został wybrany główny dach", NotificationVariant.LUMO_ERROR);
                logger.debug("No option has been selected in the tile table");
            }

            if (optionParent.isPresent()) {
                String string = " \n" + optionParent.get().getManufacturer() + " z ceną: " + optionParent.get().getTotalPrice();
                Paragraph optionalOffers = new Paragraph("\n\n\t\t\t\tOferty Opcjonalne:" +
                        string, font12);
                document.add(optionalOffers);
            }

            PdfPTable tableAccessories = createTable();
            columnHeader(baseColor, font12, tableAccessories, "Nazwa");
            Set<Accessories> accesories = accessoriesSet.stream().filter(Accessories::isOffer).collect(Collectors.toSet());
            table(new ArrayList<>(accesories), font10, tableAccessories);
            addSpacesAndTableToDocument(document, tableAccessories);

            PdfPTable tableGutter = createTable();
            Optional<Gutter> mainGutter = gutterList.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isMain()).findFirst();

            if (mainGutter.isPresent()) {
                columnHeader(baseColor, font12, tableGutter, mainGutter.get().getCategory());
                Set<Gutter> parent = gutterList.stream().filter(e -> e.getName().equals("rynna 3mb") && e.isMain()).collect(Collectors.toSet());
                table(new ArrayList<>(parent), font10, tableGutter);
                addSpacesAndTableToDocument(document, tableGutter);
            }

            PdfPTable tableWindows = createTable();
            columnHeader(baseColor, font12, tableWindows, "Nazwa");
            table(new ArrayList<>(setWindows), font10, tableWindows);
            addSpacesAndTableToDocument(document, tableWindows);

            PdfPTable tableCollars = createTable();
            columnHeader(baseColor, font12, tableCollars, "Nazwa");
            table(new ArrayList<>(setCollars), font10, tableCollars);
            addSpacesAndTableToDocument(document, tableCollars);

            PdfPTable tableAccessoriesWindows = createTable();
            columnHeader(baseColor, font12, tableAccessoriesWindows, "Nazwa");
            table(new ArrayList<>(setAccessoriesWindows), font10, tableAccessoriesWindows);
            addSpacesAndTableToDocument(document, tableAccessoriesWindows);

            PdfPTable tableFireside = createTable();
            columnHeader(baseColor, font12, tableFireside, "Nazwa");
            table(listFireside, font10, tableFireside);
            addSpacesAndTableToDocument(document, tableFireside);

            document.close();

            logger.info("Create offer for - " + userfromRepo.getName() + " " + userfromRepo.getSurname());
        } catch (DocumentException |
                IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSpacesAndTableToDocument(Document document, PdfPTable table) throws DocumentException {
        Paragraph spaces = new Paragraph("\n\n\n\n");
        document.add(spaces);
        document.add(table);
    }

    private static PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        float[] widthToTable = new float[]{320f, 85f, 85f, 85f, 85f, 85f, 85f};
        table.setWidths(widthToTable);
        return table;
    }

    private static void columnHeader(BaseColor baseColor, Font font12, PdfPTable table, String firstColumnHeader) {
        cell(font12, table, baseColor, firstColumnHeader);
        cell(font12, table, baseColor, "Ilość");
        cell(font12, table, baseColor, "Cena zakupu");
        cell(font12, table, baseColor, "Cena detal");
        cell(font12, table, baseColor, "Cena razem netto");
        cell(font12, table, baseColor, "Cena razem zakup");
        cell(font12, table, baseColor, "Zysk");
    }

    private static void table(List<? extends BaseEntity> list, Font font10, PdfPTable table) {
        for (BaseEntity baseEntity : list) {
            if (!baseEntity.getUnitDetalPrice().equals(0d) || !baseEntity.getQuantity().equals(0d)) {
                table.addCell(new Phrase(String.valueOf(baseEntity.getName()), font10));
                table.addCell(new Phrase(String.valueOf(baseEntity.getQuantity()), font10));
                table.addCell(new Phrase(String.valueOf(baseEntity.getUnitPurchasePrice()), font10));
                table.addCell(new Phrase(String.valueOf(baseEntity.getUnitDetalPrice()), font10));
                table.addCell(new Phrase(String.valueOf(baseEntity.getAllpriceAfterDiscount()), font10));
                table.addCell(new Phrase(String.valueOf(baseEntity.getAllpricePurchase()), font10));
                table.addCell(new Phrase(String.valueOf(baseEntity.getAllprofit()), font10));
            }
        }
    }

    private static void cell(Font font, PdfPTable table, BaseColor baseColor, String header) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(baseColor);
        table.addCell(cell);
    }
}