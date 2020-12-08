package com.hazlewood.coffeetracker.purchases;

import com.hazlewood.coffeetracker.beans.Beans;
import com.hazlewood.coffeetracker.beans.BeansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class BeansPurchaseController {

  private static Logger log = LoggerFactory.getLogger(BeansPurchaseController.class);
  @Autowired
  private BeansPurchaseService beansPurchaseService;
  @Autowired
  private BeansService beansService;

  @PostMapping("/purchases/existing")
  @ResponseStatus(HttpStatus.CREATED)
  public BeansPurchase registerExistingBeans(@Valid @RequestBody ExistingBeansPurchaseRequest purchaseRequest) {
    try {
      boolean alreadyExists = beansService.exists(purchaseRequest.getBeansID());
      if (!alreadyExists) throw new RuntimeException("Error while retrieving beans with id " + purchaseRequest.getBeansID());

      var beans = beansService.findById(purchaseRequest.getBeansID())
          .orElseThrow(() -> new RuntimeException("Error while retrieving beans that exist."));
      return beansPurchaseService.save(new BeansPurchase(beans, purchaseRequest.getInitialQuantity()));
    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred while saving this purchase.", ex);
    }
  }

  @PostMapping("/purchases/new")
  @ResponseStatus(HttpStatus.CREATED)
  public BeansPurchase registerNewBeans(@Valid @RequestBody NewBeansPurchaseRequest purchaseRequest) {
    boolean alreadyExists = beansService.exists(purchaseRequest.getBeans().getName());
    if (alreadyExists) {
      var beans = beansService.findByName(purchaseRequest.getBeans().getName())
          .orElseThrow(() -> new RuntimeException("Error while retrieving beans that exist."));
      var purchase = new BeansPurchase(beans, purchaseRequest.getInitialQuantity());
      return beansPurchaseService.save(purchase);
    } else {
      Beans savedBeans = beansService.save(purchaseRequest.getBeans());
      var purchase = new BeansPurchase(savedBeans, purchaseRequest.getInitialQuantity());
      return beansPurchaseService.save(purchase);
    }
  }



}
