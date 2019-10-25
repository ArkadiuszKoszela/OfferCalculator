package pl.koszela.spring.entities.gutter;

import pl.koszela.spring.entities.EntityUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class InputGutterData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double odcinek;
    private Double rynna3mb;
    private Double rynna4mb;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "input_id", nullable = false)
//    private EntityGutter input;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public EntityGutter getInput() {
//        return input;
//    }
//
//    public void setInput(EntityGutter input) {
//        this.input = input;
//    }
}
