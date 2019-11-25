package pl.koszela.spring.entities.other;

import pl.koszela.spring.entities.main.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recommend")
public class CustomerRecommend {

    @Id
    private Long id;
    private String name;
    private String phone;

    public CustomerRecommend() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
