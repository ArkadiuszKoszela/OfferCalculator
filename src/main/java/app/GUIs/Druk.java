package app.GUIs;

import com.vaadin.flow.component.Tag;

@Tag("image")
/*@Route(value = Druk.DRUK)*/
public class Druk {

    /*public static final String DRUK = "druk";
    private UsersRepo repositoryUsersRepo;
    private ResultTiles repositoryResultTiles;

    private Crud<EntityResultTiles> crud;
    ListDataProvider<EntityResultTiles> listDataProvider;

    public Druk() {
    }

    @Autowired
    public Druk(ResultTiles repositoryResultTiles, UsersRepo repositoryUsersRepo) {
        this.repositoryResultTiles = repositoryResultTiles;
        this.repositoryUsersRepo = repositoryUsersRepo;

        Iterable<EntityResultTiles> wynikiEntityIterable = repositoryResultTiles.findAll();
        List<EntityResultTiles> entityResultTilesList = new ArrayList<>();
        wynikiEntityIterable.forEach(entityResultTilesList::add);

        setOrientation(Orientation.VERTICAL);
        TextField filterText = new TextField();
        filterText.setPlaceholder("Filter by nazwa cennika");
        filterText.setClearButtonVisible(true);


        crud = new Crud<>(EntityResultTiles.class, createGrid(), createCompanyEditor());

        listDataProvider = new ListDataProvider<>(entityResultTilesList);
        crud.setHeightFull();

        crud.setDataProvider(listDataProvider);

        addToSecondary(filterText, crud);

        CrudI18n customI18n = CrudI18n.createDefault();
        customI18n.setEditItem("Update User");
        customI18n.setNewItem("New User");
        crud.setI18n(customI18n);

        crud.addEditListener(editEvent -> {
            EntityResultTiles toEdit = editEvent.getItem();
            Optional<EntityResultTiles> wyniki = repositoryResultTiles.findById(toEdit.getId());
            EntityResultTiles entityResultTiles = new EntityResultTiles();
            entityResultTiles.setPriceListName(wyniki.get().getPriceListName());
            entityResultTiles.setName(wyniki.get().getName());
            entityResultTiles.setPriceAfterDiscount(wyniki.get().getPriceAfterDiscount());
            entityResultTiles.setPurchasePrice(wyniki.get().getPurchasePrice());
            entityResultTiles.setProfit(wyniki.get().getProfit());

            entityResultTiles.setPriceListName(toEdit.getPriceListName());
            entityResultTiles.setName(toEdit.getName());
            entityResultTiles.setPriceAfterDiscount(toEdit.getPriceAfterDiscount());
            entityResultTiles.setPurchasePrice(toEdit.getPurchasePrice());
            entityResultTiles.setProfit(toEdit.getProfit());


            repositoryResultTiles.save(entityResultTiles);
        });


        Board lewyGornyrog = new Board();
        Label label = new Label("www.nowoczesnebudowanie.pl");
        Label label1 = new Label("ul. Chemiczna 2");
        Label label2 = new Label("65-713 Zielona Góra");
        Label label3 = new Label("robert@nowoczesnebudowanie.pl");
        Label label4 = new Label("tel. 502 680 330");
        lewyGornyrog.addRow(label);
        lewyGornyrog.addRow(label1);
        lewyGornyrog.addRow(label2);
        lewyGornyrog.addRow(label3);
        lewyGornyrog.addRow(label4);
        lewyGornyrog.setMaxWidth("250px");

        Label label5 = new Label("OFERTA HANDLOWA");
        Image image = new Image();
        image.setSrc("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\logo.png");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        label5.setWidth("300px");
        label5.getElement().getStyle().set("text-align", "center");
        Label label6 = new Label("Dachówki ceramiczne Nelskamrubp produkowane są z najwyższej jakości surowców " +
                "w nowoczesnej technologii. Sprawdzone na przestrzeni wieków – dachówki ceramiczne zalicza się " +
                "do najstarszych pokryć dachowych – po dziś dzień stanowią synonim piękna, naturalności i bezpieczeństwa. " +
                "Dachówki ceramiczne Braas powstają z gliny, której wysoka jakość porównywalna jest z zaletami " +
                "glinek leczniczych. Dachówki te dodają dachom klasycznego uroku i ciepła. Zachwycają mnogością barw, " +
                "dzięki którym mogą Państwo nadać swojemu dachowi indywidualny charakter - zgodnie z własnym smakiem " +
                "i pomysłem. Rubin 11V (K) to połączenie klasycznego piękna z innowacyjnymi rozwiązaniami " +
                "technologicznymi opracowanymi wraz z dekarskimi mistrzami. Model ten należy do grupy przesuwnych " +
                "dachówek dużego formatu. Godne zwrócenia uwagi są kolory bukowy i szary kryształ – dostępny tylko " +
                "dla tego modelu.");
        label6.getElement().getStyle().set("width", "750px");
        horizontalLayout.add(lewyGornyrog, label5, image);
        Layout splitLayout1 = new Layout();
        splitLayout1.setOrientation(Orientation.VERTICAL);
        splitLayout1.addToPrimary(horizontalLayout);
        splitLayout1.addToSecondary(label6);
        splitLayout1.setSecondaryStyle("minWidht", "750px");
        splitLayout1.setSecondaryStyle("maxWidht", "750px");
        addToPrimary(splitLayout1);

        Image image1 = new Image();
        image1.setSrc("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\oferta.pdf");

        setSecondaryStyle("minWidth", "750px");
        setSecondaryStyle("maxWidth", "750px");
        setSecondaryStyle("minHeight", "5000px");
        setSecondaryStyle("maxHeight", "5000px");
    }

    private Grid<EntityResultTiles> createGrid() {
        Grid<EntityResultTiles> grid = new Grid<>();
        grid.addColumn(EntityResultTiles::getPriceListName).setHeader("Nazwa cennika");
        grid.addColumn(EntityResultTiles::getName).setHeader("Nazwa");
        grid.addColumn(EntityResultTiles::getPriceAfterDiscount).setHeader("Cena Netto po rabacie");
        grid.addColumn(EntityResultTiles::getPurchasePrice).setHeader("Cena zakupu");
        grid.addColumn(EntityResultTiles::getProfit).setHeader("Zysk");
        Crud.addEditColumn(grid);
        return grid;
    }

    private CrudEditor<EntityResultTiles> createCompanyEditor() {
        TextField nazwaCennika = new TextField("nazwa");
        setColspan(nazwaCennika, 4);
        TextField nazwa = new TextField("nazwa");
        nazwa.setRequiredIndicatorVisible(true);
        setColspan(nazwa, 2);
        TextField cenaNettoPoRabacie = new TextField("cenaNettoPoRabacie");
        cenaNettoPoRabacie.setRequiredIndicatorVisible(true);
        setColspan(cenaNettoPoRabacie, 2);
        TextField cenaZakupu = new TextField("cenaZakupu");
        cenaZakupu.setRequiredIndicatorVisible(true);
        setColspan(cenaZakupu, 2);
        TextField zysk = new TextField("zysk");
        zysk.setRequiredIndicatorVisible(true);
        setColspan(zysk, 2);

        Binder<EntityResultTiles> binder = new Binder<>(EntityResultTiles.class);
        binder.bind(nazwaCennika, EntityResultTiles::getPriceListName, EntityResultTiles::setPriceListName);
        binder.bind(nazwa, EntityResultTiles::getName, EntityResultTiles::setName);
        binder.bind(cenaNettoPoRabacie, EntityResultTiles::getPriceAfterDiscount, EntityResultTiles::setPriceAfterDiscount);
        binder.bind(cenaZakupu, EntityResultTiles::getPurchasePrice, EntityResultTiles::setPurchasePrice);
        binder.bind(zysk, EntityResultTiles::getProfit, EntityResultTiles::setProfit);
        Board tablica = new Board();
        tablica.addRow(nazwaCennika, nazwa, cenaNettoPoRabacie);
        tablica.addRow(cenaZakupu, zysk);
        return new BinderCrudEditor<>(binder, tablica);
    }

    private void setColspan(Layout component, int colspan) {
        component.getElement().setAttribute("colspan", Integer.toString(colspan));
    }*/
}
