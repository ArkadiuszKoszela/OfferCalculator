package app.entities;

import javax.persistence.*;

@Entity
@Table(name = "input_data")
public class EntityInputData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Double powierzchniaPolaci;
    private Double dlugoscKalenic;
    private Double dlugoscKalenicSkosnych;
    private Double dlugoscKalenicProstych;
    private Double dlugoscKoszy;
    private Double dlugoscKrawedziLewych;
    private Double dlugoscKrawedziPrawych;
    private Double obwodKomina;
    private Double dlugoscOkapu;
    private Double dachowkaWentylacyjna;
    private Double kompletKominkaWentylacyjnego;
    private Double gasiarPoczatkowyKalenicaProsta;
    private Double gasiarKoncowyKalenicaProsta;
    private Double gasiarZaokraglony;
    private Double trojnik;
    private Double czwornik;
    private Double gasiarZPodwojnaMufa;
    private Double dachowkaDwufalowa;
    private Double oknoPolaciowe;

    public EntityInputData() {
    }

    public static EntityInputData.Builder builder() {
        return new EntityInputData.Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPowierzchniaPolaci() {
        return powierzchniaPolaci;
    }

    public void setPowierzchniaPolaci(Double powierzchniaPolaci) {
        this.powierzchniaPolaci = powierzchniaPolaci;
    }

    public Double getDlugoscKalenic() {
        return dlugoscKalenic;
    }

    public void setDlugoscKalenic(Double dlugoscKalenic) {
        this.dlugoscKalenic = dlugoscKalenic;
    }

    public Double getDlugoscKalenicSkosnych() {
        return dlugoscKalenicSkosnych;
    }

    public void setDlugoscKalenicSkosnych(Double dlugoscKalenicSkosnych) {
        this.dlugoscKalenicSkosnych = dlugoscKalenicSkosnych;
    }

    public Double getDlugoscKalenicProstych() {
        return dlugoscKalenicProstych;
    }

    public void setDlugoscKalenicProstych(Double dlugoscKalenicProstych) {
        this.dlugoscKalenicProstych = dlugoscKalenicProstych;
    }

    public Double getDlugoscKoszy() {
        return dlugoscKoszy;
    }

    public void setDlugoscKoszy(Double dlugoscKoszy) {
        this.dlugoscKoszy = dlugoscKoszy;
    }

    public Double getDlugoscKrawedziLewych() {
        return dlugoscKrawedziLewych;
    }

    public void setDlugoscKrawedziLewych(Double dlugoscKrawedziLewych) {
        this.dlugoscKrawedziLewych = dlugoscKrawedziLewych;
    }

    public Double getDlugoscKrawedziPrawych() {
        return dlugoscKrawedziPrawych;
    }

    public void setDlugoscKrawedziPrawych(Double dlugoscKrawedziPrawych) {
        this.dlugoscKrawedziPrawych = dlugoscKrawedziPrawych;
    }

    public Double getObwodKomina() {
        return obwodKomina;
    }

    public void setObwodKomina(Double obwodKomina) {
        this.obwodKomina = obwodKomina;
    }

    public Double getDlugoscOkapu() {
        return dlugoscOkapu;
    }

    public void setDlugoscOkapu(Double dlugoscOkapu) {
        this.dlugoscOkapu = dlugoscOkapu;
    }

    public Double getDachowkaWentylacyjna() {
        return dachowkaWentylacyjna;
    }

    public void setDachowkaWentylacyjna(Double dachowkaWentylacyjna) {
        this.dachowkaWentylacyjna = dachowkaWentylacyjna;
    }

    public Double getKompletKominkaWentylacyjnego() {
        return kompletKominkaWentylacyjnego;
    }

    public void setKompletKominkaWentylacyjnego(Double kompletKominkaWentylacyjnego) {
        this.kompletKominkaWentylacyjnego = kompletKominkaWentylacyjnego;
    }

    public Double getGasiarPoczatkowyKalenicaProsta() {
        return gasiarPoczatkowyKalenicaProsta;
    }

    public void setGasiarPoczatkowyKalenicaProsta(Double gasiarPoczatkowyKalenicaProsta) {
        this.gasiarPoczatkowyKalenicaProsta = gasiarPoczatkowyKalenicaProsta;
    }

    public Double getGasiarKoncowyKalenicaProsta() {
        return gasiarKoncowyKalenicaProsta;
    }

    public void setGasiarKoncowyKalenicaProsta(Double gasiarKoncowyKalenicaProsta) {
        this.gasiarKoncowyKalenicaProsta = gasiarKoncowyKalenicaProsta;
    }

    public Double getGasiarZaokraglony() {
        return gasiarZaokraglony;
    }

    public void setGasiarZaokraglony(Double gasiarZaokraglony) {
        this.gasiarZaokraglony = gasiarZaokraglony;
    }

    public Double getTrojnik() {
        return trojnik;
    }

    public void setTrojnik(Double trojnik) {
        this.trojnik = trojnik;
    }

    public Double getCzwornik() {
        return czwornik;
    }

    public void setCzwornik(Double czwornik) {
        this.czwornik = czwornik;
    }

    public Double getGasiarZPodwojnaMufa() {
        return gasiarZPodwojnaMufa;
    }

    public void setGasiarZPodwojnaMufa(Double gasiarZPodwojnaMufa) {
        this.gasiarZPodwojnaMufa = gasiarZPodwojnaMufa;
    }

    public Double getDachowkaDwufalowa() {
        return dachowkaDwufalowa;
    }

    public void setDachowkaDwufalowa(Double dachowkaDwufalowa) {
        this.dachowkaDwufalowa = dachowkaDwufalowa;
    }

    public Double getOknoPolaciowe() {
        return oknoPolaciowe;
    }

    public void setOknoPolaciowe(Double oknoPolaciowe) {
        this.oknoPolaciowe = oknoPolaciowe;
    }

    public static final class Builder {
        private Double powierzchniaPolaci;
        private Double dlugoscKalenic;
        private Double dlugoscKalenicSkosnych;
        private Double dlugoscKalenicProstych;
        private Double dlugoscKoszy;
        private Double dlugoscKrawedziLewych;
        private Double dlugoscKrawedziPrawych;
        private Double obwodKomina;
        private Double dlugoscOkapu;
        private Double dachowkaWentylacyjna;
        private Double kompletKominkaWentylacyjnego;
        private Double gasiarPoczatkowyKalenicaProsta;
        private Double gasiarKoncowyKalenicaProsta;
        private Double gasiarZaokraglony;
        private Double trojnik;
        private Double czwornik;
        private Double gasiarZPodwojnaMufa;
        private Double dachowkaDwufalowa;
        private Double oknoPolaciowe;

        public EntityInputData build() {
            EntityInputData entityInputData = new EntityInputData();
            entityInputData.powierzchniaPolaci = this.powierzchniaPolaci;
            entityInputData.dlugoscKalenic = this.dlugoscKalenic;
            entityInputData.dlugoscKalenicSkosnych = this.dlugoscKalenicSkosnych;
            entityInputData.dlugoscKalenicProstych = this.dlugoscKalenicProstych;
            entityInputData.dlugoscKoszy = this.dlugoscKoszy;
            entityInputData.dlugoscKrawedziLewych = this.dlugoscKrawedziLewych;
            entityInputData.dlugoscKrawedziPrawych = this.dlugoscKrawedziPrawych;
            entityInputData.obwodKomina = this.obwodKomina;
            entityInputData.dlugoscOkapu = this.dlugoscOkapu;
            entityInputData.dachowkaWentylacyjna = this.dachowkaWentylacyjna;
            entityInputData.kompletKominkaWentylacyjnego = this.kompletKominkaWentylacyjnego;
            entityInputData.gasiarPoczatkowyKalenicaProsta = this.gasiarPoczatkowyKalenicaProsta;
            entityInputData.gasiarKoncowyKalenicaProsta = this.gasiarKoncowyKalenicaProsta;
            entityInputData.gasiarZaokraglony = this.gasiarZaokraglony;
            entityInputData.trojnik = this.trojnik;
            entityInputData.czwornik = this.czwornik;
            entityInputData.gasiarZPodwojnaMufa = this.gasiarZPodwojnaMufa;
            entityInputData.dachowkaDwufalowa = this.dachowkaDwufalowa;
            entityInputData.oknoPolaciowe = this.oknoPolaciowe;
            return entityInputData;
        }

        public Builder powierzchniaPolaci(Double powierzchniaPolaci) {
            this.powierzchniaPolaci = powierzchniaPolaci;
            return this;
        }

        public Builder dlugoscKalenic(Double dlugoscKalenic) {
            this.dlugoscKalenic = dlugoscKalenic;
            return this;
        }

        public Builder dlugoscKalenicSkosnych(Double dlugoscKalenicSkosnych) {
            this.dlugoscKalenicSkosnych = dlugoscKalenicSkosnych;
            return this;
        }

        public Builder dlugoscKalenicProstych(Double dlugoscKalenicProstych) {
            this.dlugoscKalenicProstych = dlugoscKalenicProstych;
            return this;
        }

        public Builder dlugoscKoszy(Double dlugoscKoszy) {
            this.dlugoscKoszy = dlugoscKoszy;
            return this;
        }

        public Builder dlugoscKrawedziLewych(Double dlugoscKrawedziLewych) {
            this.dlugoscKrawedziLewych = dlugoscKrawedziLewych;
            return this;
        }

        public Builder dlugoscKrawedziPrawych(Double dlugoscKrawedziPrawych) {
            this.dlugoscKrawedziPrawych = dlugoscKrawedziPrawych;
            return this;
        }

        public Builder obwodKomina(Double obwodKomina) {
            this.obwodKomina = obwodKomina;
            return this;
        }

        public Builder dlugoscOkapu(Double dlugoscOkapu) {
            this.dlugoscOkapu = dlugoscOkapu;
            return this;
        }

        public Builder dachowkaWentylacyjna(Double dachowkaWentylacyjna) {
            this.dachowkaWentylacyjna = dachowkaWentylacyjna;
            return this;
        }

        public Builder kompletKominkaWentylacyjnego(Double kompletKominkaWentylacyjnego) {
            this.kompletKominkaWentylacyjnego = kompletKominkaWentylacyjnego;
            return this;
        }

        public Builder gasiarPoczatkowyKalenicaProsta(Double gasiarPoczatkowyKalenicaProsta) {
            this.gasiarPoczatkowyKalenicaProsta = gasiarPoczatkowyKalenicaProsta;
            return this;
        }

        public Builder gasiarKoncowyKalenicaProsta(Double gasiarKoncowyKalenicaProsta) {
            this.gasiarKoncowyKalenicaProsta = gasiarKoncowyKalenicaProsta;
            return this;
        }

        public Builder gasiarZaokraglony(Double gasiarZaokraglony) {
            this.gasiarZaokraglony = gasiarZaokraglony;
            return this;
        }

        public Builder trojnik(Double trojnik) {
            this.trojnik = trojnik;
            return this;
        }

        public Builder czwornik(Double czwornik) {
            this.czwornik = czwornik;
            return this;
        }

        public Builder gasiarZPodwojnaMufa(Double gasiarZPodwojnaMufa) {
            this.gasiarZPodwojnaMufa = gasiarZPodwojnaMufa;
            return this;
        }

        public Builder dachowkaDwufalowa(Double dachowkaDwufalowa) {
            this.dachowkaDwufalowa = dachowkaDwufalowa;
            return this;
        }
        public Builder oknoPolaciowe(Double oknoPolaciowe) {
            this.oknoPolaciowe = oknoPolaciowe;
            return this;
        }

    }
}
