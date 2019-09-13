package app.DAOs;

import app.entities.EntityTiles;
import app.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DaoUser {


    private UsersRepo usersRepo;

    @Autowired
    public DaoUser(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;

        EntityTiles entityTiles = new EntityTiles();
        String name = entityTiles.getName();
        String nazwaCennika = entityTiles.getPriceListName();
        BigDecimal price = entityTiles.getUnitRetailPrice();


        /*usersRepo.save()
        GUIs.getDachowkiEntityList().get(1).*/


    }
}
