package app.Controller;

import app.entities.EntityTiles;
import app.repositories.Tiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/lista")
public class ControllerSpring {

    private Tiles tiles;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ControllerSpring(Tiles tiles) {
        this.tiles = Objects.requireNonNull(tiles);
    }

    @GetMapping("/cos/{nazwa}")
    public ResponseEntity<List<EntityTiles>> cos(@PathVariable String nazwa) {
        return ResponseEntity.ok(tiles.findByPriceListNameContains(nazwa));
    }

    @GetMapping("/show")
    public ModelAndView showAll(@ModelAttribute EntityTiles entityTiles) {
        String nazwa = "Bogen";
        ModelAndView modelAndView = new ModelAndView("table-list");
        modelAndView.addObject("lista", tiles.findByPriceListNameContains(nazwa));
        return modelAndView;
    }

    @GetMapping("/one")
    public String one(){
        return "pierwszy";
    }

    @GetMapping("/showAll")
    public ModelAndView cos2 (){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping("/cos")
    public String cos() {
        return "templates/table-list.html";
    }
}
