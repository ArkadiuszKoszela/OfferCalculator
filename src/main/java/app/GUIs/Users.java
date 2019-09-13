package app.GUIs;

import app.controllers.ControllerVaadin;
import app.entities.EntityUser;
import app.repositories.Accesories;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import app.repositories.UsersRepo;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Route(value = Users.USERS)
public class Users extends SplitLayout {

    public static final String USERS = "Users";

    private Tiles tiles;
    private Accesories accesories;
    private UsersRepo usersRepo;
    private ResultTiles resultTiles;
    private ControllerVaadin controllerVaadin;

    private Crud<EntityUser> crud;
    private ListDataProvider<EntityUser> listDataProvider;

    Users() {
    }

    @Autowired
    public Users(Tiles tiles, UsersRepo usersRepo, ResultTiles resultTiles, Accesories accesories, ControllerVaadin controllerVaadin) {
        this.tiles = Objects.requireNonNull(tiles);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.accesories = Objects.requireNonNull(accesories);
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);

        setOrientation(Orientation.VERTICAL);

        addToPrimary(controllerVaadin.routerLink());

        listDataProvider = new ListDataProvider<>(allUser(usersRepo));

        crud = new Crud<>(EntityUser.class, createGrid(), createCompanyEditor(usersRepo));
        crud.setWidth("1200px");

        crud.setHeight("400px");

        crud.setDataProvider(listDataProvider);
        addToSecondary(crud);

        CrudI18n customI18n = CrudI18n.createDefault();
        customI18n.setEditItem("Update User - edit");
        customI18n.setNewItem("New User");
        crud.setI18n(customI18n);

        saveListener(usersRepo);
        newListener(usersRepo);
        deleteListener(usersRepo);

        ustawieniaStrony1();
    }

    private void deleteListener(UsersRepo usersRepo) {
        crud.addDeleteListener(deleteEvent -> {
            EntityUser toDelete = deleteEvent.getItem();
            allUser(usersRepo);
            usersRepo.delete(toDelete);
        });
    }

    private void newListener(UsersRepo usersRepo) {
        crud.addNewListener(newEvent -> {
            EntityUser toNew = newEvent.getItem();
            if (!allUser(usersRepo).contains(toNew)) {
                usersRepo.save(toNew);
            }
        });
    }

    private void saveListener(UsersRepo usersRepo) {
        crud.addSaveListener(saveEvent -> {
            EntityUser toSave = saveEvent.getItem();
            if (!allUser(usersRepo).contains(toSave)) {
                usersRepo.save(toSave);
                Notification notification = new Notification("Zamieniono dane usera", 4000);
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.open();
            }
        });
    }

    private List<EntityUser> allUser(UsersRepo usersRepo) {
        Iterable<EntityUser> iterable = usersRepo.findAll();
        List<EntityUser> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    private void ustawieniaStrony1() {
        Board board = new Board();
        board.addRow(controllerVaadin.routerLink());
        board.getStyle().set("background", "#DCDCDC");
        addToPrimary(board);
        setPrimaryStyle("minWidth", "1280px");
        setPrimaryStyle("maxWidth", "1280px");
        setPrimaryStyle("minHeight", "70px");
        setPrimaryStyle("maxHeight", "700px");
        setSecondaryStyle("minWidth", "1280px");
        setSecondaryStyle("maxWidth", "1280px");
        setSecondaryStyle("minHeight", "500px");
        setSecondaryStyle("maxHeight", "500px");
    }

    private Grid<EntityUser> createGrid() {
        Grid<EntityUser> grid = new Grid<>();
        Grid.Column<EntityUser> name = grid.addColumn(EntityUser::getName).setHeader("Imię").setWidth("80px");
        Grid.Column<EntityUser> surname = grid.addColumn(EntityUser::getSurname).setHeader("Nazwisko").setWidth("80px");
        Grid.Column<EntityUser> adres = grid.addColumn(EntityUser::getAdress).setHeader("Adres").setWidth("50px");
        Grid.Column<EntityUser> numer = grid.addColumn(EntityUser::getTelephoneNumber).setHeader("Numer tel.").setWidth("50px");
        Grid.Column<EntityUser> date = grid.addColumn(EntityUser::getDateOfMeeting).setHeader("Data Spotkania").setWidth("50px");
        Grid.Column<EntityUser> nazwaCennika = grid.addColumn(EntityUser::getPriceListName).setHeader("Nazwa Cennika").setWidth("450px");

        createFilterRows(grid, name, surname);
        Crud.addEditColumn(grid);
        return grid;
    }

    private void createFilterRows(Grid<EntityUser> grid, Grid.Column<EntityUser> name, Grid.Column<EntityUser> surname) {
        HeaderRow filterRow = grid.appendHeaderRow();
        TextField filterName = new TextField();
        filterName.addValueChangeListener(event -> listDataProvider.addFilter(
                person -> StringUtils.containsIgnoreCase(person.getName(),
                        filterName.getValue())));

        filterName.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(name).setComponent(filterName);

        TextField filterSurname = new TextField();
        filterSurname.addValueChangeListener(event -> listDataProvider.addFilter(
                person -> StringUtils.containsIgnoreCase(person.getSurname(),
                        filterSurname.getValue())));

        filterSurname.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(surname).setComponent(filterSurname);
        filterSurname.setSizeFull();
        filterSurname.setPlaceholder("Nazwisko");
        filterName.setSizeFull();
        filterName.setPlaceholder("Imię");
    }

    private CrudEditor<EntityUser> createCompanyEditor(UsersRepo usersRepo) {
        TextField name = new TextField("Imię");
        /*setColspan(name, 4);*/
        TextField surname = new TextField("Nazwisko");
        surname.setRequiredIndicatorVisible(true);
        /*setColspan(surname, 2);*/
        TextField adres = new TextField("Adres");
        /*setColspan(adres, 2);*/
        adres.setRequiredIndicatorVisible(true);
        TextField numer = new TextField("Numer");
        /*setColspan(numer, 2);*/
        DatePicker date = new DatePicker("Data spotkania");

        TextField powierzchniaPolaci = new TextField("Powierzchnia połaci: ");
        powierzchniaPolaci.setRequiredIndicatorVisible(true);
        /*setColspan(powierzchniaPolaci, 2);*/

        TextField dlugoscKalenic = new TextField("Długość kalenic");
        /*setColspan(dlugoscKalenic, 2);*/
        TextField dlugoscKalenicSkosnych = new TextField("Długość kalenic skośnych");
        /*setColspan(dlugoscKalenicSkosnych, 2);*/
        TextField dlugoscKalenicProstych = new TextField("Długość kalenic prostych");
        /*setColspan(dlugoscKalenicProstych, 2);*/
        TextField dlugoscKoszy = new TextField("Długość koszy");
        /*setColspan(dlugoscKoszy, 2);*/
        TextField dlugoscKrawedziLewych = new TextField("Długość krawędzi lewych");
        /*setColspan(dlugoscKrawedziLewych, 2);*/
        TextField dlugoscKrawedziPrawych = new TextField("Długość krawędzi prawych");
        /*setColspan(dlugoscKrawedziPrawych, 2);*/
        TextField obwodKomina = new TextField("Obwód komina");
        /*setColspan(obwodKomina, 2);*/
        TextField dlugoscOkapu = new TextField("Długość okapu");
        /*setColspan(dlugoscOkapu, 2);*/
        TextField dachowkaWentylacyjna = new TextField("Dachówka wentylacyjna");
        /*setColspan(dachowkaWentylacyjna, 2);*/
        TextField kompletKominkaWentylacyjnego = new TextField("Komplet kominka wentylacyjnego");
        /*setColspan(kompletKominkaWentylacyjnego, 2);*/
        TextField gasiorPoczatkowyKalenicaProsta = new TextField("Gąsior początkowy kalenica prosta ");
        /*setColspan(gasiorPoczatkowyKalenicaProsta, 2);*/
        TextField gasiorKoncowyKalenicaProsta = new TextField("Gąsior końcowy kalenica prosta");
        /*setColspan(gasiorKoncowyKalenicaProsta, 2);*/
        TextField gasiorZaokraglony = new TextField("Gąsior zaokrąglony");
        /*setColspan(gasiorZaokraglony, 2);*/
        TextField trojnik = new TextField("Trójnik");
        /*setColspan(trojnik, 2);*/
        TextField czwornik = new TextField("Czwórnik");
        /*setColspan(czwornik, 2);*/
        TextField gasiorZPodwojnaMufa = new TextField("Gąsior z podwójną mufą");
        /*setColspan(gasiorZPodwojnaMufa, 2);*/
        TextField dachowkaDwufalowa = new TextField("Dachówka dwufalowa");
        /*setColspan(dachowkaDwufalowa, 2);*/
        TextField oknoPolaciowe = new TextField("Okno połaciowe");
        /*setColspan(oknoPolaciowe, 2);*/

        TextField nazwaCennika = new TextField("Nazwa Cennika");
        /*setColspan(nazwaCennika, 2);*/


        Binder<EntityUser> binder = new Binder<>(EntityUser.class);
        binder.bind(name, EntityUser::getName, EntityUser::setName);
        binder.bind(surname, EntityUser::getSurname, EntityUser::setSurname);
        binder.bind(adres, EntityUser::getAdress, EntityUser::setAdress);
        binder.bind(numer, EntityUser::getTelephoneNumber, EntityUser::setTelephoneNumber);
        binder.bind(date, EntityUser::getDateOfMeeting, EntityUser::setDateOfMeeting);
        binder.bind(powierzchniaPolaci,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getPowierzchniaPolaci();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setPowierzchniaPolaci(name);
                    }
                });
        binder.bind(dlugoscKalenic,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscKalenic();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscKalenic(name);
                    }
                });
        binder.bind(dlugoscKalenicProstych,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscKalenicProstych();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscKalenicProstych(name);
                    }
                });
        binder.bind(dlugoscKalenicSkosnych,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscKalenicSkosnych();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscKalenicSkosnych(name);
                    }
                });
        binder.bind(dlugoscKoszy,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscKoszy();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscKoszy(name);
                    }
                });
        binder.bind(dlugoscKrawedziLewych,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscKrawedziLewych();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscKrawedziLewych(name);
                    }
                });
        binder.bind(dlugoscKrawedziPrawych,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscKrawedziPrawych();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscKrawedziPrawych(name);
                    }
                });
        binder.bind(obwodKomina,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getObwodKomina();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setObwodKomina(name);
                    }
                });
        binder.bind(dlugoscOkapu,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDlugoscOkapu();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDlugoscOkapu(name);
                    }
                });
        binder.bind(dachowkaWentylacyjna,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDachowkaWentylacyjna();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDachowkaWentylacyjna(name);
                    }
                });
        binder.bind(kompletKominkaWentylacyjnego,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getKompletKominkaWentylacyjnego();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setKompletKominkaWentylacyjnego(name);
                    }
                });
        binder.bind(gasiorPoczatkowyKalenicaProsta,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getGasiarPoczatkowyKalenicaProsta();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setGasiarPoczatkowyKalenicaProsta(name);
                    }
                });
        binder.bind(gasiorKoncowyKalenicaProsta,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getGasiarKoncowyKalenicaProsta();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setGasiarKoncowyKalenicaProsta(name);
                    }
                });
        binder.bind(gasiorZaokraglony,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getGasiarZaokraglony();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setGasiarZaokraglony(name);
                    }
                });
        binder.bind(trojnik,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getTrojnik();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setTrojnik(name);
                    }
                });
        binder.bind(czwornik,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getCzwornik();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setCzwornik(name);
                    }
                });
        binder.bind(gasiorZPodwojnaMufa,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getGasiarZPodwojnaMufa();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setGasiarZPodwojnaMufa(name);
                    }
                });
        binder.bind(dachowkaDwufalowa,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getDachowkaDwufalowa();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setDachowkaDwufalowa(name);
                    }
                });
        binder.bind(oknoPolaciowe,
                new ValueProvider<EntityUser, String>() {
                    @Override
                    public String apply(EntityUser person) {
                        return person.getEntityInputData().iterator().next().getOknoPolaciowe();
                    }
                },
                new Setter<EntityUser, String>() {
                    @Override
                    public void accept(EntityUser person, String name) {
                        person.getEntityInputData().iterator().next().setOknoPolaciowe(name);
                    }
                });
        binder.bind(nazwaCennika, EntityUser::getPriceListName, EntityUser::setPriceListName);

        Board board = new Board();
        board.addRow(name, surname, adres);
        board.addRow(numer, date);
        board.addRow(powierzchniaPolaci, dlugoscKalenic, dlugoscKalenicProstych, dlugoscKalenicSkosnych);
        board.addRow(dlugoscKoszy, dlugoscKrawedziLewych, dlugoscKrawedziPrawych, obwodKomina);
        board.addRow(dlugoscOkapu, dachowkaWentylacyjna, kompletKominkaWentylacyjnego, gasiorPoczatkowyKalenicaProsta);
        board.addRow(gasiorKoncowyKalenicaProsta, gasiorZaokraglony, trojnik, czwornik);
        board.addRow(gasiorZPodwojnaMufa, dachowkaDwufalowa, oknoPolaciowe);
        return new BinderCrudEditor<>(binder, board);
    }

    /*private void setColspan(Component component, int colspan) {
        component.getElement().setAttribute("colspan", Integer.toString(colspan));
    }*/

    /*private void csv() {
        if (tiles.count() == 0 && accesories.count() == 0) {
            DaoTiles daoTiles = new DaoTiles(this.tiles);
            DaoAccesories daoAccesories = new DaoAccesories(this.accesories);
            daoTiles.save("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 10 czerwona angoba.csv");
            daoTiles.save("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 10 miedziano-brązowa angoba.csv");
            daoTiles.save("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 12 czerwona angoba.csv");
            daoAccesories.save("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\akcesoria.csv");
            getAvailablePriceList(this.tiles);
            Notification zaimportowano = new Notification("Zaimportowano cenniki", 3000);
            zaimportowano.setPosition(Notification.Position.TOP_CENTER);
            zaimportowano.open();
            *//*History history = UI.getCurrent().getPage().getHistory();
            history.getUI().navigate(LoadUser.class) ;*//*
        } else {
            Notification zaimportowano = new Notification("Cenniki są już zaimportowane", 3000);
            zaimportowano.setPosition(Notification.Position.TOP_CENTER);
            zaimportowano.open();
        }

    }*/
}
