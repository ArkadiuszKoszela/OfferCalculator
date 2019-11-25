package pl.koszela.spring.views.priceLists;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.other.CustomerRecommend;
import pl.koszela.spring.repositories.other.CustomerRecommendRepository;
import pl.koszela.spring.views.MainView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = CustomerRecommendListView.CUSTOMER_RECOMMEND, layout = MainView.class)
public class CustomerRecommendListView extends VerticalLayout {

    public static final String CUSTOMER_RECOMMEND = "customerRecommend";

    private Grid<CustomerRecommend> grid = new Grid<>();
    private Binder<CustomerRecommend> binder;
    private List<CustomerRecommend> list = new ArrayList<>();

    private CustomerRecommendRepository customerRecommendRepository;

    @Autowired
    public CustomerRecommendListView(CustomerRecommendRepository customerRecommendRepository) throws SQLException, ClassNotFoundException {
        this.customerRecommendRepository = Objects.requireNonNull(customerRecommendRepository);

        list = all();
//        connect();

        add(gridd());
    }

    private Grid<CustomerRecommend> gridd() {
        grid.addColumn(CustomerRecommend::getName).setHeader("Imię");
        grid.addColumn(CustomerRecommend::getPhone).setHeader("Telefon");

        grid.setDataProvider(new ListDataProvider<>(all()));
        grid.getColumns().forEach(e -> e.setAutoWidth(true));
        return grid;
    }

    private void connect() throws SQLException, ClassNotFoundException {
//        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setURL("jdbc:mysql://localhost:3306/id11673562_recommend_database");
//        dataSource.setUser("id11673562_arek");
//        dataSource.setPassword("arek123");

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://remotemysql.com:3306/sVwj1w7NAv", "sVwj1w7NAv", "g3cqGPdm5b");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM recommend");
        while(rs.next())
            System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        con.close();

    }

    @Transactional("otherTransactionManager")
    List<CustomerRecommend> all(){
        return customerRecommendRepository.findAll();
    }
}