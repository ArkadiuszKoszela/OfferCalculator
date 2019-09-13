package app.calculate;

import app.service.AllFields;

public class CalculateAccesories {

    private AllFields allFields;

    /*public FormLayout formLayoutAccesories(Accesories accesories) {
        FormLayout board = new FormLayout();
        utworzListePustychComboBoxow();
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<EntityAccesories> list = new ArrayList<>();
        iterable.forEach(list::add);
        List<String> listaNazw = new ArrayList<>();
        list.forEach(e -> listaNazw.add(e.getName()));
        if (list.size() > 0) {
            fields.comboBoxtasmaKalenicowa.setItems(getSubList(listaNazw, 0, 9));
            comboBoxwspornikLatyKalenicowej.setItems(getSubList(listaNazw, 9, 14));
            comboBoxtasmaDoObrobkiKomina.setItems(getSubList(listaNazw, 14, 20));
            comboBoxlistwaWykonczeniowaAluminiowa.setItems(getSubList(listaNazw, 20, 21));
            comboBoxkoszDachowyAluminiowy2mb.setItems(getSubList(listaNazw, 21, 23));
            comboBoxklamraDoMocowaniaKosza.setItems(getSubList(listaNazw, 24, 25));
            comboBoxklinUszczelniajacyKosz.setItems(getSubList(listaNazw, 25, 27));
            comboBoxgrzebienOkapowy.setItems(getSubList(listaNazw, 27, 31));
            comboBoxkratkaZabezpieczajacaPrzedPtactwem.setItems(getSubList(listaNazw, 31, 33));
            comboBoxpasOkapowy.setItems(getSubList(listaNazw, 33, 35));
            comboBoxklamraDoGasiora.setItems(getSubList(listaNazw, 35, 39));
            comboBoxspinkaDoDachowki.setItems(getSubList(listaNazw, 39, 44));
            comboBoxspinkaDoDachowkiCietej.setItems(getSubList(listaNazw, 44, 46));
            comboBoxlawaKominiarska.setItems(getSubList(listaNazw, 46, 54));
            comboBoxstopienKominiarski.setItems(getSubList(listaNazw, 55, 56));
            comboBoxplotekPrzeciwsniegowy155mmx2mb.setItems(getSubList(listaNazw, 56, 57));
            comboBoxplotekPrzeciwsniegowy155mmx3mb.setItems(getSubList(listaNazw, 57, 59));
            comboBoxmembranaDachowa.setItems(getSubList(listaNazw, 59, 64));
            comboBoxtasmaDoLaczeniaMembarnIFolii.setItems(getSubList(listaNazw, 64, 67));
            comboBoxtasmaReparacyjna.setItems(getSubList(listaNazw, 67, 69));
            comboBoxblachaAluminiowa.setItems(getSubList(listaNazw, 69, 72));
            comboBoxceglaKlinkierowa.setItems(getSubList(listaNazw, 72, 73));
        }
        Label label = new Label(" ");
        board.add(calculateAccesories, label);
        for (Map.Entry<ComboBox<String>, String> mapa : getMapa().entrySet()) {
            mapa.getKey().setLabel(mapa.getValue());
        }
        utworzMapeAkcesoriaComboBoxITextField();
        for (Map.Entry<ComboBox<String>, NumberField> mapa : getAkcesoriaComboBoxITextField().entrySet()) {
            board.add(mapa.getKey(), mapa.getValue());
        }

        calculateAccesories.addClickListener(click -> wstawDoWartosciAkcesoria(accesories));
        return board;
    }*/
}
