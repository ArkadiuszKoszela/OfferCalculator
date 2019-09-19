package pl.koszela.spring.entities;

import javax.persistence.*;

@Entity
@Table(name = "input_data_tiles")
public class EntityInputDataTiles {

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

    public Double getPowierzchniaPolaci() {
        return powierzchniaPolaci;
    }

    public Double getDlugoscKalenic() {
        return dlugoscKalenic;
    }

    public Double getDlugoscKalenicSkosnych() {
        return dlugoscKalenicSkosnych;
    }

    public Double getDlugoscKalenicProstych() {
        return dlugoscKalenicProstych;
    }

    public Double getDlugoscKoszy() {
        return dlugoscKoszy;
    }

    public Double getDlugoscKrawedziLewych() {
        return dlugoscKrawedziLewych;
    }

    public Double getDlugoscKrawedziPrawych() {
        return dlugoscKrawedziPrawych;
    }

    public Double getObwodKomina() {
        return obwodKomina;
    }

    public Double getDlugoscOkapu() {
        return dlugoscOkapu;
    }

    public Double getDachowkaWentylacyjna() {
        return dachowkaWentylacyjna;
    }

    public Double getKompletKominkaWentylacyjnego() {
        return kompletKominkaWentylacyjnego;
    }

    public Double getGasiarPoczatkowyKalenicaProsta() {
        return gasiarPoczatkowyKalenicaProsta;
    }

    public Double getGasiarKoncowyKalenicaProsta() {
        return gasiarKoncowyKalenicaProsta;
    }

    public Double getGasiarZaokraglony() {
        return gasiarZaokraglony;
    }

    public Double getTrojnik() {
        return trojnik;
    }

    public Double getCzwornik() {
        return czwornik;
    }

    public Double getGasiarZPodwojnaMufa() {
        return gasiarZPodwojnaMufa;
    }

    public Double getDachowkaDwufalowa() {
        return dachowkaDwufalowa;
    }

    public Double getOknoPolaciowe() {
        return oknoPolaciowe;
    }

    public EntityInputDataTiles() {
    }

    public static EntityInputDataTiles.Builder builder() {
        return new EntityInputDataTiles.Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        public EntityInputDataTiles build() {
            EntityInputDataTiles entityInputDataTiles = new EntityInputDataTiles();
            entityInputDataTiles.powierzchniaPolaci = this.powierzchniaPolaci;
            entityInputDataTiles.dlugoscKalenic = this.dlugoscKalenic;
            entityInputDataTiles.dlugoscKalenicSkosnych = this.dlugoscKalenicSkosnych;
            entityInputDataTiles.dlugoscKalenicProstych = this.dlugoscKalenicProstych;
            entityInputDataTiles.dlugoscKoszy = this.dlugoscKoszy;
            entityInputDataTiles.dlugoscKrawedziLewych = this.dlugoscKrawedziLewych;
            entityInputDataTiles.dlugoscKrawedziPrawych = this.dlugoscKrawedziPrawych;
            entityInputDataTiles.obwodKomina = this.obwodKomina;
            entityInputDataTiles.dlugoscOkapu = this.dlugoscOkapu;
            entityInputDataTiles.dachowkaWentylacyjna = this.dachowkaWentylacyjna;
            entityInputDataTiles.kompletKominkaWentylacyjnego = this.kompletKominkaWentylacyjnego;
            entityInputDataTiles.gasiarPoczatkowyKalenicaProsta = this.gasiarPoczatkowyKalenicaProsta;
            entityInputDataTiles.gasiarKoncowyKalenicaProsta = this.gasiarKoncowyKalenicaProsta;
            entityInputDataTiles.gasiarZaokraglony = this.gasiarZaokraglony;
            entityInputDataTiles.trojnik = this.trojnik;
            entityInputDataTiles.czwornik = this.czwornik;
            entityInputDataTiles.gasiarZPodwojnaMufa = this.gasiarZPodwojnaMufa;
            entityInputDataTiles.dachowkaDwufalowa = this.dachowkaDwufalowa;
            entityInputDataTiles.oknoPolaciowe = this.oknoPolaciowe;
            return entityInputDataTiles;
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
