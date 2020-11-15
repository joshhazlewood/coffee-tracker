package com.hazlewood.coffeetracker.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BeansController {
  @Autowired private BeansService beansService;

  @GetMapping("/beans")
  public List<Beans> beans() {
    return beansService.getAllBeans();
  }

  @PostMapping("/beans")
  @ResponseStatus(HttpStatus.CREATED)
  public Beans save(@RequestBody Beans newBeans) {
    return beansService.save(newBeans);
  }

}
