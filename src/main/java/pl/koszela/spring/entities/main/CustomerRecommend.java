package pl.koszela.spring.entities.main;

import javax.persistence.*;

@Entity
@Table(name = "recommend")
public class CustomerRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String selectOption;
    private Boolean checked1;
    private Boolean checked2;
    private Boolean checked3;
    private Boolean checked4;
    private Integer number;
    private String status;

    @ManyToOne
    @JoinColumn
    private UserMobileApp userMobileApp;

    public CustomerRecommend() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSelectOption() {
        return selectOption;
    }

    public void setSelectOption(String selectOption) {
        this.selectOption = selectOption;
    }

    public boolean isChecked1() {
        return checked1;
    }

    public void setChecked1(Boolean checked1) {
        this.checked1 = checked1;
    }

    public boolean isChecked2() {
        return checked2;
    }

    public void setChecked2(Boolean checked2) {
        this.checked2 = checked2;
    }

    public boolean isChecked3() {
        return checked3;
    }

    public void setChecked3(Boolean checked3) {
        this.checked3 = checked3;
    }

    public boolean isChecked4() {
        return checked4;
    }

    public void setChecked4(Boolean checked4) {
        this.checked4 = checked4;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public UserMobileApp getUserMobileApp() {
        return userMobileApp;
    }

    public void setUserMobileApp(UserMobileApp userMobileApp) {
        this.userMobileApp = userMobileApp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
