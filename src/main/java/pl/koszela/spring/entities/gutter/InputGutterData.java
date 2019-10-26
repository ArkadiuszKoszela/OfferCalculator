package pl.koszela.spring.entities.gutter;

import javax.persistence.Embeddable;

@Embeddable
public class InputGutterData {

    private Double odcinek;
    private Double rynna3mb;
    private Double rynna4mb;

    public Double getOdcinek() {
        return odcinek;
    }

    public void setOdcinek(Double odcinek) {
        this.odcinek = odcinek;
    }

    public Double getRynna3mb() {
        return rynna3mb;
    }

    public void setRynna3mb(Double rynna3mb) {
        this.rynna3mb = rynna3mb;
    }

    public Double getRynna4mb() {
        return rynna4mb;
    }

    public void setRynna4mb(Double rynna4mb) {
        this.rynna4mb = rynna4mb;
    }
}