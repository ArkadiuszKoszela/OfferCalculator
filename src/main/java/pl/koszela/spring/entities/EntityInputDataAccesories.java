package pl.koszela.spring.entities;

import javax.persistence.*;

@Entity
@Table(name = "input_data_accesories")
public class EntityInputDataAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tasmaKalenicowa;
    private String wspornikLatyKalenicowej;
    private String tasmaDoObrobkiKomina;
    private String listwaWykonczeniowaAluminiowa;
    private String koszDachowyAluminiowy;
    private String klamraDoMocowaniaKosza;
    private String klinUszczelniajacyKosz;
    private String grzebienOkapowy;
    private String kratkaZabezpieczajacaPrzedPtactwem;
    private String pasOkapowy;
    private String klamraDoGasiora;
    private String spinkaDoDachowki;
    private String spinkaDoDachowkiCietej;
    private String lawaKominiarska;
    private String stopienKominiarski;
    private String plotekPrzeciwsniegowy155mmx2mb;
    private String plotekPrzeciwsniegowy155mmx3mb;
    private String membranaDachowa;
    private String tasmaDoLaczeniaMembarnIFolii;
    private String tasmaReparacyjna;
    private String blachaAluminiowa;
    private String ceglaKlinkierowa;

    public EntityInputDataAccesories() {
    }

    public static EntityInputDataAccesories.Builder builder() {
        return new EntityInputDataAccesories.Builder();
    }

    public static final class Builder {
        private String tasmaKalenicowa;
        private String wspornikLatyKalenicowej;
        private String tasmaDoObrobkiKomina;
        private String listwaWykonczeniowaAluminiowa;
        private String koszDachowyAluminiowy;
        private String klamraDoMocowaniaKosza;
        private String klinUszczelniajacyKosz;
        private String grzebienOkapowy;
        private String kratkaZabezpieczajacaPrzedPtactwem;
        private String pasOkapowy;
        private String klamraDoGasiora;
        private String spinkaDoDachowki;
        private String spinkaDoDachowkiCietej;
        private String lawaKominiarska;
        private String stopienKominiarski;
        private String plotekPrzeciwsniegowy155mmx2mb;
        private String plotekPrzeciwsniegowy155mmx3mb;
        private String membranaDachowa;
        private String tasmaDoLaczeniaMembarnIFolii;
        private String tasmaReparacyjna;
        private String blachaAluminiowa;
        private String ceglaKlinkierowa;

        public EntityInputDataAccesories build() {
            EntityInputDataAccesories entityInputDataAccesories = new EntityInputDataAccesories();
            entityInputDataAccesories.tasmaKalenicowa = this.tasmaKalenicowa;
            entityInputDataAccesories.wspornikLatyKalenicowej = this.wspornikLatyKalenicowej;
            entityInputDataAccesories.tasmaDoObrobkiKomina = this.tasmaDoObrobkiKomina;
            entityInputDataAccesories.listwaWykonczeniowaAluminiowa = this.listwaWykonczeniowaAluminiowa;
            entityInputDataAccesories.koszDachowyAluminiowy = this.koszDachowyAluminiowy;
            entityInputDataAccesories.klamraDoMocowaniaKosza = this.klamraDoMocowaniaKosza;
            entityInputDataAccesories.klinUszczelniajacyKosz = this.klinUszczelniajacyKosz;
            entityInputDataAccesories.grzebienOkapowy = this.grzebienOkapowy;
            entityInputDataAccesories.kratkaZabezpieczajacaPrzedPtactwem = this.kratkaZabezpieczajacaPrzedPtactwem;
            entityInputDataAccesories.pasOkapowy = this.pasOkapowy;
            entityInputDataAccesories.klamraDoGasiora = this.klamraDoGasiora;
            entityInputDataAccesories.spinkaDoDachowki = this.spinkaDoDachowki;
            entityInputDataAccesories.spinkaDoDachowkiCietej = this.spinkaDoDachowkiCietej;
            entityInputDataAccesories.lawaKominiarska = this.lawaKominiarska;
            entityInputDataAccesories.stopienKominiarski = this.stopienKominiarski;
            entityInputDataAccesories.plotekPrzeciwsniegowy155mmx2mb = this.plotekPrzeciwsniegowy155mmx2mb;
            entityInputDataAccesories.plotekPrzeciwsniegowy155mmx3mb = this.plotekPrzeciwsniegowy155mmx3mb;
            entityInputDataAccesories.membranaDachowa = this.membranaDachowa;
            entityInputDataAccesories.tasmaDoLaczeniaMembarnIFolii = this.tasmaDoLaczeniaMembarnIFolii;
            entityInputDataAccesories.blachaAluminiowa = this.blachaAluminiowa;
            entityInputDataAccesories.ceglaKlinkierowa = this.ceglaKlinkierowa;
            return entityInputDataAccesories;
        }

        public Builder tasmaKalenicowa(String tasmaKalenicowa) {
            this.tasmaKalenicowa = tasmaKalenicowa;
            return this;
        }

        public Builder wspornikLatyKalenicowej(String wspornikLatyKalenicowej) {
            this.wspornikLatyKalenicowej = wspornikLatyKalenicowej;
            return this;
        }

        public Builder tasmaDoObrobkiKomina(String tasmaDoObrobkiKomina) {
            this.tasmaDoObrobkiKomina = tasmaDoObrobkiKomina;
            return this;
        }

        public Builder listwaWykonczeniowaAluminiowa(String listwaWykonczeniowaAluminiowa) {
            this.listwaWykonczeniowaAluminiowa = listwaWykonczeniowaAluminiowa;
            return this;
        }

        public Builder koszDachowyAluminiowy(String koszDachowyAluminiowy) {
            this.koszDachowyAluminiowy = koszDachowyAluminiowy;
            return this;
        }

        public Builder klamraDoMocowaniaKosza(String klamraDoMocowaniaKosza) {
            this.klamraDoMocowaniaKosza = klamraDoMocowaniaKosza;
            return this;
        }

        public Builder klinUszczelniajacyKosz(String klinUszczelniajacyKosz) {
            this.klinUszczelniajacyKosz = klinUszczelniajacyKosz;
            return this;
        }

        public Builder grzebienOkapowy(String grzebienOkapowy) {
            this.grzebienOkapowy = grzebienOkapowy;
            return this;
        }

        public Builder kratkaZabezpieczajacaPrzedPtactwem(String kratkaZabezpieczajacaPrzedPtactwem) {
            this.kratkaZabezpieczajacaPrzedPtactwem = kratkaZabezpieczajacaPrzedPtactwem;
            return this;
        }

        public Builder pasOkapowy(String pasOkapowy) {
            this.pasOkapowy = pasOkapowy;
            return this;
        }

        public Builder klamraDoGasiora(String klamraDoGasiora) {
            this.klamraDoGasiora = klamraDoGasiora;
            return this;
        }

        public Builder spinkaDoDachowki(String spinkaDoDachowki) {
            this.spinkaDoDachowki = spinkaDoDachowki;
            return this;
        }

        public Builder spinkaDoDachowkiCietej(String spinkaDoDachowkiCietej) {
            this.spinkaDoDachowkiCietej = spinkaDoDachowkiCietej;
            return this;
        }

        public Builder lawaKominiarska(String lawaKominiarska) {
            this.lawaKominiarska = lawaKominiarska;
            return this;
        }

        public Builder stopienKominiarski(String stopienKominiarski) {
            this.stopienKominiarski = stopienKominiarski;
            return this;
        }

        public Builder plotekPrzeciwsniegowy155mmx2mb(String plotekPrzeciwsniegowy155mmx2mb) {
            this.plotekPrzeciwsniegowy155mmx2mb = plotekPrzeciwsniegowy155mmx2mb;
            return this;
        }

        public Builder plotekPrzeciwsniegowy155mmx3mb(String plotekPrzeciwsniegowy155mmx3mb) {
            this.plotekPrzeciwsniegowy155mmx3mb = plotekPrzeciwsniegowy155mmx3mb;
            return this;
        }

        public Builder membranaDachowa(String membranaDachowa) {
            this.membranaDachowa = membranaDachowa;
            return this;
        }

        public Builder tasmaDoLaczeniaMembarnIFolii(String tasmaDoLaczeniaMembarnIFolii) {
            this.tasmaDoLaczeniaMembarnIFolii = tasmaDoLaczeniaMembarnIFolii;
            return this;
        }

        public Builder tasmaReparacyjna(String tasmaReparacyjna) {
            this.tasmaReparacyjna = tasmaReparacyjna;
            return this;
        }

        public Builder blachaAluminiowa(String blachaAluminiowa) {
            this.blachaAluminiowa = blachaAluminiowa;
            return this;
        }

        public Builder ceglaKlinkierowa(String ceglaKlinkierowa) {
            this.ceglaKlinkierowa = ceglaKlinkierowa;
            return this;
        }
    }

}
