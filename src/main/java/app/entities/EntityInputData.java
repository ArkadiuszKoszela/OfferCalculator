package app.entities;

import javax.persistence.*;

@Entity
@Table(name = "input_data")
public class EntityInputData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String powierzchniaPolaci;
    private String dlugoscKalenic;
    private String dlugoscKalenicSkosnych;
    private String dlugoscKalenicProstych;
    private String dlugoscKoszy;
    private String dlugoscKrawedziLewych;
    private String dlugoscKrawedziPrawych;
    private String obwodKomina;
    private String dlugoscOkapu;
    private String dachowkaWentylacyjna;
    private String kompletKominkaWentylacyjnego;
    private String gasiarPoczatkowyKalenicaProsta;
    private String gasiarKoncowyKalenicaProsta;
    private String gasiarZaokraglony;
    private String trojnik;
    private String czwornik;
    private String gasiarZPodwojnaMufa;
    private String dachowkaDwufalowa;
    private String oknoPolaciowe;

    public EntityInputData() {
    }

    public EntityInputData(String powierzchniaPolaci, String dlugoscKalenic, String dlugoscKalenicSkosnych, String dlugoscKalenicProstych, String dlugoscKoszy, String dlugoscKrawedziLewych, String dlugoscKrawedziPrawych, String obwodKomina, String dlugoscOkapu, String dachowkaWentylacyjna, String kompletKominkaWentylacyjnego, String gasiarPoczatkowyKalenicaProsta, String gasiarKoncowyKalenicaProsta, String gasiarZaokraglony, String trojnik, String czwornik, String gasiarZPodwojnaMufa, String dachowkaDwufalowa, String oknoPolaciowe) {
        this.powierzchniaPolaci = powierzchniaPolaci;
        this.dlugoscKalenic = dlugoscKalenic;
        this.dlugoscKalenicSkosnych = dlugoscKalenicSkosnych;
        this.dlugoscKalenicProstych = dlugoscKalenicProstych;
        this.dlugoscKoszy = dlugoscKoszy;
        this.dlugoscKrawedziLewych = dlugoscKrawedziLewych;
        this.dlugoscKrawedziPrawych = dlugoscKrawedziPrawych;
        this.obwodKomina = obwodKomina;
        this.dlugoscOkapu = dlugoscOkapu;
        this.dachowkaWentylacyjna = dachowkaWentylacyjna;
        this.kompletKominkaWentylacyjnego = kompletKominkaWentylacyjnego;
        this.gasiarPoczatkowyKalenicaProsta = gasiarPoczatkowyKalenicaProsta;
        this.gasiarKoncowyKalenicaProsta = gasiarKoncowyKalenicaProsta;
        this.gasiarZaokraglony = gasiarZaokraglony;
        this.trojnik = trojnik;
        this.czwornik = czwornik;
        this.gasiarZPodwojnaMufa = gasiarZPodwojnaMufa;
        this.dachowkaDwufalowa = dachowkaDwufalowa;
        this.oknoPolaciowe = oknoPolaciowe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPowierzchniaPolaci() {
        return powierzchniaPolaci;
    }

    public void setPowierzchniaPolaci(String powierzchniaPolaci) {
        this.powierzchniaPolaci = powierzchniaPolaci;
    }

    public String getDlugoscKalenic() {
        return dlugoscKalenic;
    }

    public void setDlugoscKalenic(String dlugoscKalenic) {
        this.dlugoscKalenic = dlugoscKalenic;
    }

    public String getDlugoscKalenicSkosnych() {
        return dlugoscKalenicSkosnych;
    }

    public void setDlugoscKalenicSkosnych(String dlugoscKalenicSkosnych) {
        this.dlugoscKalenicSkosnych = dlugoscKalenicSkosnych;
    }

    public String getDlugoscKalenicProstych() {
        return dlugoscKalenicProstych;
    }

    public void setDlugoscKalenicProstych(String dlugoscKalenicProstych) {
        this.dlugoscKalenicProstych = dlugoscKalenicProstych;
    }

    public String getDlugoscKoszy() {
        return dlugoscKoszy;
    }

    public void setDlugoscKoszy(String dlugoscKoszy) {
        this.dlugoscKoszy = dlugoscKoszy;
    }

    public String getDlugoscKrawedziLewych() {
        return dlugoscKrawedziLewych;
    }

    public void setDlugoscKrawedziLewych(String dlugoscKrawedziLewych) {
        this.dlugoscKrawedziLewych = dlugoscKrawedziLewych;
    }

    public String getDlugoscKrawedziPrawych() {
        return dlugoscKrawedziPrawych;
    }

    public void setDlugoscKrawedziPrawych(String dlugoscKrawedziPrawych) {
        this.dlugoscKrawedziPrawych = dlugoscKrawedziPrawych;
    }

    public String getObwodKomina() {
        return obwodKomina;
    }

    public void setObwodKomina(String obwodKomina) {
        this.obwodKomina = obwodKomina;
    }

    public String getDlugoscOkapu() {
        return dlugoscOkapu;
    }

    public void setDlugoscOkapu(String dlugoscOkapu) {
        this.dlugoscOkapu = dlugoscOkapu;
    }

    public String getDachowkaWentylacyjna() {
        return dachowkaWentylacyjna;
    }

    public void setDachowkaWentylacyjna(String dachowkaWentylacyjna) {
        this.dachowkaWentylacyjna = dachowkaWentylacyjna;
    }

    public String getKompletKominkaWentylacyjnego() {
        return kompletKominkaWentylacyjnego;
    }

    public void setKompletKominkaWentylacyjnego(String kompletKominkaWentylacyjnego) {
        this.kompletKominkaWentylacyjnego = kompletKominkaWentylacyjnego;
    }

    public String getGasiarPoczatkowyKalenicaProsta() {
        return gasiarPoczatkowyKalenicaProsta;
    }

    public void setGasiarPoczatkowyKalenicaProsta(String gasiarPoczatkowyKalenicaProsta) {
        this.gasiarPoczatkowyKalenicaProsta = gasiarPoczatkowyKalenicaProsta;
    }

    public String getGasiarKoncowyKalenicaProsta() {
        return gasiarKoncowyKalenicaProsta;
    }

    public void setGasiarKoncowyKalenicaProsta(String gasiarKoncowyKalenicaProsta) {
        this.gasiarKoncowyKalenicaProsta = gasiarKoncowyKalenicaProsta;
    }

    public String getGasiarZaokraglony() {
        return gasiarZaokraglony;
    }

    public void setGasiarZaokraglony(String gasiarZaokraglony) {
        this.gasiarZaokraglony = gasiarZaokraglony;
    }

    public String getTrojnik() {
        return trojnik;
    }

    public void setTrojnik(String trojnik) {
        this.trojnik = trojnik;
    }

    public String getCzwornik() {
        return czwornik;
    }

    public void setCzwornik(String czwornik) {
        this.czwornik = czwornik;
    }

    public String getGasiarZPodwojnaMufa() {
        return gasiarZPodwojnaMufa;
    }

    public void setGasiarZPodwojnaMufa(String gasiarZPodwojnaMufa) {
        this.gasiarZPodwojnaMufa = gasiarZPodwojnaMufa;
    }

    public String getDachowkaDwufalowa() {
        return dachowkaDwufalowa;
    }

    public void setDachowkaDwufalowa(String dachowkaDwufalowa) {
        this.dachowkaDwufalowa = dachowkaDwufalowa;
    }

    public String getOknoPolaciowe() {
        return oknoPolaciowe;
    }

    public void setOknoPolaciowe(String oknoPolaciowe) {
        this.oknoPolaciowe = oknoPolaciowe;
    }

    @Override
    public String toString() {
        return "EntityInputData{" +
                "id=" + id +
                ", powierzchniaPolaci=" + powierzchniaPolaci +
                ", dlugoscKalenic=" + dlugoscKalenic +
                ", dlugoscKalenicSkosnych=" + dlugoscKalenicSkosnych +
                ", dlugoscKalenicProstych=" + dlugoscKalenicProstych +
                ", dlugoscKoszy=" + dlugoscKoszy +
                ", dlugoscKrawedziLewych=" + dlugoscKrawedziLewych +
                ", dlugoscKrawedziPrawych=" + dlugoscKrawedziPrawych +
                ", obwodKomina=" + obwodKomina +
                ", dlugoscOkapu=" + dlugoscOkapu +
                ", dachowkaWentylacyjna=" + dachowkaWentylacyjna +
                ", kompletKominkaWentylacyjnego=" + kompletKominkaWentylacyjnego +
                ", gasiarPoczatkowyKalenicaProsta=" + gasiarPoczatkowyKalenicaProsta +
                ", gasiarKoncowyKalenicaProsta=" + gasiarKoncowyKalenicaProsta +
                ", gasiarZaokraglony=" + gasiarZaokraglony +
                ", trojnik=" + trojnik +
                ", czwornik=" + czwornik +
                ", gasiarZPodwojnaMufa=" + gasiarZPodwojnaMufa +
                ", dachowkaDwufalowa=" + dachowkaDwufalowa +
                ", oknoPolaciowe=" + oknoPolaciowe +
                '}';
    }
}
