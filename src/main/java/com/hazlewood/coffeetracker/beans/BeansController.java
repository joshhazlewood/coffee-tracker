package com.hazlewood.coffeetracker.beans;

import com.hazlewood.coffeetracker.purchases.BeansPurchase;
import com.hazlewood.coffeetracker.purchases.BeansPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BeansController {

  @Autowired private BeansService beansService;

  @GetMapping("/beans")
  public List<Beans> beans() {
    return beansService.getAllBeans();
  }

  @GetMapping("/beans/{id}")
  public Beans one(@PathVariable Long id) {
    return beansService.findById(id).orElseThrow(() -> new BeansNotFoundException(id));
  }

  @PostMapping("/beans")
  @ResponseStatus(HttpStatus.CREATED)
  public Beans save(@Valid @RequestBody Beans newBeans) {
    try {
      return beansService.save(newBeans);
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred while saving these beans.", ex);
    }
  }

  @GetMapping("/beans/name/{name}")
  public Beans oneByName(@PathVariable String name) {
    return beansService.findByName(name).orElseThrow(() -> new BeansNotFoundException(name));
  }

}
