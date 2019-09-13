package app.repositories;

import app.entities.EntityInputData;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("input_data")
public interface InputData extends CrudRepository<EntityInputData, TextField> {

}
