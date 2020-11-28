package com.hazlewood.coffeetracker.espresso;

import com.hazlewood.coffeetracker.beans.BeansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EspressoShotController {

  Logger log = LoggerFactory.getLogger(EspressoShotController.class);

  @Autowired private EspressoShotService shotService;

  @Autowired private BeansService beansService;

  @GetMapping("/shots")
  private List<EspressoShot> getAll() {
    return shotService.getAll();
  }

  @GetMapping("/shots/{id}")
  private EspressoShot getById(@PathVariable Long id) {
    return shotService.findById(id).orElseThrow(() -> new EspressoShotNotFoundException(id));
  }

  @PostMapping("/shots")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<EspressoShot> save(@Valid @RequestBody EspressoShot newShot) {
    try {
      boolean validBeans = beansService.exists(newShot.getBeans().getId());
      if (!validBeans) {
        return ResponseEntity.notFound().build();
      }

      EspressoShot savedShot = shotService.save(newShot);
      if (savedShot == null) {
        return ResponseEntity.notFound().build();
      }

      var uri = createIdURI(newShot.getId());
      return ResponseEntity.created(uri).body(savedShot);
    } catch (Exception ex) {
      log.error(ex.toString());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred while saving this shot.", ex);
    }
  }

  private URI createIdURI(Long id) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(id)
        .toUri();
  }

}
