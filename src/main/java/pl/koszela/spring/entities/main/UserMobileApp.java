package pl.koszela.spring.entities.main;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_recommend")
public class UserMobileApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String username;
    private String password;
    private Integer points;

    @OneToMany(mappedBy = "userMobileApp")
    private Set<CustomerRecommend> customerRecommends;

    public UserMobileApp() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Set<CustomerRecommend> getCustomerRecommends() {
        return customerRecommends;
    }

    public void setCustomerRecommends(Set<CustomerRecommend> customerRecommends) {
        this.customerRecommends = customerRecommends;
    }
}
