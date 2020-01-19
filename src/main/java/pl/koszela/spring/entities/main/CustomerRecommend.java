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
    @Column(name = "select_option")
    private String selectOption;
    private String place;
    private String status;
    @Column(name = "url_image")
    private String urlImage;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_mobile_app_id")
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
