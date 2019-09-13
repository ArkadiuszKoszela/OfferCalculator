package app.service;

import app.entities.EntityResultAccesories;
import app.inputFields.ServiceDataCustomer;
import app.inputFields.ServiceNumberFiled;
import app.repositories.Accesories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AllFields {

    private ServiceNumberFiled serviceNumberFiled;
    private ServiceDataCustomer serviceDataCustomer;
    private Accesories accesories;

    @Autowired
    AllFields(ServiceNumberFiled serviceNumberFiled,
              ServiceDataCustomer serviceDataCustomer,
              Accesories accesories) {
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.accesories = Objects.requireNonNull(accesories);
    }

    /*private TextField priceListName;
    private TextField type;
    private TextField category;
    private TextField unitRetailPrice;
    private TextField profit;
    private TextField basicDiscount;
    private TextField supplierDiscount;
    private TextField additionalDiscount;
    private TextField skontoDiscount;*/

    /*private CheckboxGroup<String> checkboxes;*/

    /*private NumberField customerDiscount;*/

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

    /*private Button calculateAccesories = new Button("Oblicz Accesories");*/

    /*private Map<ComboBox<String>, NumberField> akcesoriaComboBoxITextField;*/
    /*public Map<ComboBox<String>, String> comboBoxesWithLabels;*/

    /*private Button createComboboxes = new Button("utworz te jebane comboboxy");*/

   /* public FormLayout formLayoutAccesories() {
        FormLayout board = new FormLayout();
        serviceComboBox.createValueComboBoxes();
        createNamesComboBox();
        Label label = new Label(" ");
        board.add(calculateAccesories, label);
        for (Map.Entry<ComboBox<String>, String> map : comboBoxesWithLabels.entrySet()) {
            map.getKey().setLabel(map.getValue());
        }
        createMap();
        for (Map.Entry<ComboBox<String>, NumberField> mapa : getAkcesoriaComboBoxITextField().entrySet()) {
            board.add(mapa.getKey(), mapa.getValue());
        }
        calculateAccesories.addClickListener(click -> wstawDoWartosciAkcesoria(accesories));
        return board;
    }*/

    /*private void createNamesComboBox() {
        comboBoxesWithLabels = new LinkedHashMap<>();
        for (int i = 0; i < serviceComboBox.listComboboxWithoutName.size(); i++) {
            comboBoxesWithLabels.put(serviceComboBox.listComboboxWithoutName.get(i), serviceComboBox.allLabelsToComboBox.get(i));
        }
    }*/

    /*public Board createCheckboxes() {
        Board board = new Board();
        setCheckboxes(new CheckboxGroup<>());
        serviceComboBox.createListOfLabels();
        getCheckboxes().setItems(serviceComboBox.allLabelsToComboBox);
        getCheckboxes().setLabel(TASMA_KELNICOWA);
        board.add(clearButton());
        createComboboxes.addClickListener(click -> disabledComboBox());
        board.add(createComboboxes);
        board.add(getCheckboxes());
        return board;
    }
*/
    /*public void wstawDoWartosciAkcesoria() {
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<EntityAccesories> list = new ArrayList<>();
        iterable.forEach(list::add);

        for (Map.Entry<ComboBox<String>, NumberField> mapa : getAkcesoriaComboBoxITextField().entrySet()) {
            for (EntityAccesories entityAccesories : list) {
                if (mapa.getKey() != null) {
                    if (entityAccesories.getName().equals(mapa.getKey().getValue())) {
                        BigDecimal cena = entityAccesories.getPurchasePrice();
                        Double pierwszy = entityAccesories.getFirstMultiplier();
                        Double drugi = entityAccesories.getSecondMultiplier();
                        double wynik = pierwszy * drugi;
                        Double wynikOstateczny = cena.doubleValue() * wynik;
                        mapa.getValue().setValue(wynikOstateczny);
                    }
                }
            }
        }
    }*/

    private List<EntityResultAccesories> listaWynikowAkcesoria = new ArrayList<>();

    /*public void wstawWszystkieWartosciAkcesoria(Accesories accesories, ResultAccesories resultAccesories) {
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<EntityAccesories> list = new ArrayList<>();
        iterable.forEach(list::add);

        *//*Set<String> check = getCheckboxes().getSelectedItems();*//*

        Set<ComboBox<String>> listaWszytskichCheckboxwo = comboBoxesWithLabels.keySet();

        Set<ComboBox<String>> listaZaznoczonych = new HashSet<>();

        for (ComboBox<String> comboBox : listaWszytskichCheckboxwo) {
            if (!comboBox.getValue().isEmpty()) {
                listaZaznoczonych.add(comboBox);
            } else {
                throw new NullPointerException("Brak");
            }
        }

        for (NumberField numberField : serviceNumberFiled.getListOfNumberFields()) {
            for (EntityAccesories entityAccesories : list) {
                for (ComboBox<String> comboBox : listaZaznoczonych) {
                    if (comboBox.getValue().equalsIgnoreCase(entityAccesories.getName())) {
                        if (numberField.getTitle().contains(entityAccesories.getName())) {
                            BigDecimal cena = entityAccesories.getPurchasePrice();
                            Double pierwszy = entityAccesories.getFirstMultiplier();
                            Double drugi = entityAccesories.getSecondMultiplier();
                            double wynik = pierwszy * drugi;
                            double wynikOstateczny = cena.doubleValue() * wynik;
                            EntityResultAccesories akcesoria1 = new EntityResultAccesories();
                            akcesoria1.setName(entityAccesories.getName());
                            akcesoria1.setQuantity(numberField.getValue() * pierwszy * drugi);
                            akcesoria1.setUnitPurchasePrice(BigDecimal.valueOf(wynikOstateczny).setScale(2, RoundingMode.HALF_UP));
                            akcesoria1.setUnitRetailPrice(akcesoria1.getUnitPurchasePrice().multiply(BigDecimal.valueOf(200).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP)).add(akcesoria1.getUnitPurchasePrice()));
                            akcesoria1.setTotalPurchase(akcesoria1.getUnitPurchasePrice().multiply(BigDecimal.valueOf(akcesoria1.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));
                            akcesoria1.setTotalRetail(akcesoria1.getUnitRetailPrice().multiply(BigDecimal.valueOf(akcesoria1.getQuantity())).setScale(2, RoundingMode.HALF_UP));

                            listaWynikowAkcesoria.add(akcesoria1);
                            resultAccesories.saveAll(listaWynikowAkcesoria);
                        }
                    }
                }
            }
        }
    }*/


    /*public Board showTableResultAccesories(ResultAccesories resultAccesories) {
        Board board = new Board();
        Grid<EntityResultAccesories> grid = new Grid<>(EntityResultAccesories.class);
        grid.getColumnByKey("name").setHeader("Nazwa").setWidth("300px");
        grid.getColumnByKey("quantity").setHeader("Ilosc");
        grid.getColumnByKey("unitRetailPrice").setHeader("Cena detaliczna");
        grid.getColumnByKey("totalRetail").setHeader("Razem netto");
        grid.getColumnByKey("unitPurchasePrice").setHeader("Cena Zakupu");
        grid.getColumnByKey("totalPurchase").setHeader("Razem zakup");
        grid.setColumns("name", "quantity", "unitRetailPrice", "totalRetail", "unitPurchasePrice", "totalPurchase");
        Iterable<EntityResultAccesories> resultAccesoriesFromRepository = resultAccesories.findAll();
        List<EntityResultAccesories> allAccesories = new ArrayList<>();
        resultAccesoriesFromRepository.forEach(allAccesories::add);
        grid.setItems(allAccesories);
        board.addRow(grid);
        return board;
    }*/

    /*private void disabledComboBox() {
        HashSet<String> zaznaczone = new HashSet<String>(getCheckboxes().getSelectedItems());
        HashSet<String> nizaznaczone = new HashSet<String>(serviceComboBox.allLabelsToComboBox);
        nizaznaczone.removeAll(zaznaczone);
        if (zaznaczone.size() > 0) {
            for (Map.Entry<ComboBox<String>, NumberField> mapa : getAkcesoriaComboBoxITextField().entrySet()) {
                for (String cos : zaznaczone) {
                    if (mapa.getKey().getLabel().equals(cos)) {
                        mapa.getKey().setEnabled(true);
                        mapa.getValue().setEnabled(true);
                    }
                }
            }
            for (Map.Entry<ComboBox<String>, NumberField> mapa : getAkcesoriaComboBoxITextField().entrySet()) {
                for (String cos : nizaznaczone) {
                    if (mapa.getKey().getLabel().equals(cos)) {
                        mapa.getKey().setEnabled(false);
                        mapa.getValue().setEnabled(false);
                        mapa.getValue().setValue(0.0);
                    }
                }
            }
        }
    }*/

    public void createMap() {
        /*setAkcesoriaComboBoxITextField(new LinkedHashMap<>());*/
       /* getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxtasmaKalenicowa, serviceNumberFiled.fieldTasmaKalenicowa);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxwspornikLatyKalenicowej, serviceNumberFiled.fieldWspornikLaty);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxtasmaDoObrobkiKomina, serviceNumberFiled.fieldTasmaDoObrobkiKomina);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxlistwaWykonczeniowaAluminiowa, serviceNumberFiled.fieldListwaWykonczeniowaAluminiowa);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxkoszDachowyAluminiowy2mb, serviceNumberFiled.fieldKoszDachowyAluminiowy);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxklamraDoMocowaniaKosza, serviceNumberFiled.fieldKlamraDoMocowaniaKosza);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxklinUszczelniajacyKosz, serviceNumberFiled.fieldKLIN_USZCZELNIAJACY_KOSZ);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxgrzebienOkapowy, serviceNumberFiled.fieldGRZEBIEN_OKAPOWY);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxkratkaZabezpieczajacaPrzedPtactwem, serviceNumberFiled.fieldKRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxpasOkapowy, serviceNumberFiled.fieldPAS_OKAPOWY);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxklamraDoGasiora, serviceNumberFiled.fieldKLAMRA_DO_GASIORA);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxspinkaDoDachowki, serviceNumberFiled.fieldSPINKA_DO_DACHOWKI);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxspinkaDoDachowkiCietej, serviceNumberFiled.fieldSPINKA_DO_DACHOWKI_CIETEJ);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxlawaKominiarska, serviceNumberFiled.fieldLAWA_KOMINIARSKA);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxstopienKominiarski, serviceNumberFiled.fieldSTOPIEN_KOMINIARSKI);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxplotekPrzeciwsniegowy155mmx2mb, serviceNumberFiled.fieldPLOTEK_PRZECIWSNIEGOWY_155MMX2MB);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxplotekPrzeciwsniegowy155mmx3mb, serviceNumberFiled.fieldPLOTEK_PRZECIWSNIEGOWY_155MMX3MB);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxmembranaDachowa, serviceNumberFiled.fieldMEMBRANA_DACHOWA);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxtasmaDoLaczeniaMembarnIFolii, serviceNumberFiled.fieldTASMA_DO_LACZENIA_MEMBRAN_I_FOLII);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxtasmaReparacyjna, serviceNumberFiled.fieldTASMA_REPARACYJNA);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxblachaAluminiowa, serviceNumberFiled.fieldBLACHA_ALUMINIOWA);
        getAkcesoriaComboBoxITextField().put(serviceComboBox.comboBoxceglaKlinkierowa, serviceNumberFiled.fieldCEGLA_KLINKIEROWA);*/
    }

    /*private Button clearButton() {
        Button button = new Button("Wyczyść zaznaczone");
        button.addClickListener(click -> getCheckboxes().clear());
        return button;
    }*/

    /*public void createTextFieldsForGrid() {
        setPriceListName(new TextField("priceListName"));
        getPriceListName().setRequiredIndicatorVisible(true);
        setColspan(getPriceListName(), 2);

        setType(new TextField("Typ dachówki"));
        setColspan(getType(), 2);
        setCategory(new TextField("category"));
        setColspan(getCategory(), 4);
        setUnitRetailPrice(new TextField("unitRetailPrice"));
        setColspan(getUnitRetailPrice(), 2);

        setProfit(new TextField("marża"));
        setColspan(getProfit(), 2);
        setBasicDiscount(new TextField("rabatPodstawy"));
        setColspan(getBasicDiscount(), 2);
        setSupplierDiscount(new TextField("supplierDiscount"));
        setColspan(getSupplierDiscount(), 2);
        setAdditionalDiscount(new TextField("rabatdodatkowy"));
        setColspan(getAdditionalDiscount(), 2);
        setSkontoDiscount(new TextField("skontoDiscount"));
        setColspan(getSkontoDiscount(), 2);
    }
*/
    /*private void setColspan(Component component, int colspan) {
        component.getElement().setAttribute("colspan", Integer.toString(colspan));
    }*/

    /*public FormLayout createLayoutForUser() {
        FormLayout board = new FormLayout();
        serviceDataCustomer.createTextFieldsForUser();
        board.add(serviceDataCustomer.getName(), serviceDataCustomer.getSurname(), serviceDataCustomer.getAdress(), serviceDataCustomer.getTelephoneNumber());
        board.add(serviceDataCustomer.getDateOfMeeting());
        return board;
    }*/

  /*  public Map<ComboBox<String>, NumberField> getAkcesoriaComboBoxITextField() {
        return akcesoriaComboBoxITextField;
    }

    private void setAkcesoriaComboBoxITextField
            (Map<ComboBox<String>, NumberField> akcesoriaComboBoxITextField) {
        this.akcesoriaComboBoxITextField = akcesoriaComboBoxITextField;
    }
*/
   /* public TextField getPriceListName() {
        return priceListName;
    }

    private void setPriceListName(TextField priceListName) {
        this.priceListName = priceListName;
    }*/

    /*public TextField getType() {
        return type;
    }

    private void setType(TextField type) {
        this.type = type;
    }

    public TextField getCategory() {
        return category;
    }

    private void setCategory(TextField category) {
        this.category = category;
    }

    public TextField getUnitRetailPrice() {
        return unitRetailPrice;
    }

    private void setUnitRetailPrice(TextField unitRetailPrice) {
        this.unitRetailPrice = unitRetailPrice;
    }

    public TextField getProfit() {
        return profit;
    }

    private void setProfit(TextField profit) {
        this.profit = profit;
    }

    public TextField getBasicDiscount() {
        return basicDiscount;
    }

    private void setBasicDiscount(TextField basicDiscount) {
        this.basicDiscount = basicDiscount;
    }

    public TextField getSupplierDiscount() {
        return supplierDiscount;
    }

    private void setSupplierDiscount(TextField supplierDiscount) {
        this.supplierDiscount = supplierDiscount;
    }

    public TextField getAdditionalDiscount() {
        return additionalDiscount;
    }

    private void setAdditionalDiscount(TextField additionalDiscount) {
        this.additionalDiscount = additionalDiscount;
    }

    public TextField getSkontoDiscount() {
        return skontoDiscount;
    }

    private void setSkontoDiscount(TextField skontoDiscount) {
        this.skontoDiscount = skontoDiscount;
    }*/

    /*public NumberField getCustomerDiscount() {
        return customerDiscount;
    }

    public void setCustomerDiscount(NumberField customerDiscount) {
        this.customerDiscount = customerDiscount;
    }*/

   /* private CheckboxGroup<String> getCheckboxes() {
        return checkboxes;
    }

    private void setCheckboxes(CheckboxGroup<String> checkboxes) {
        this.checkboxes = checkboxes;
    }*/


}
