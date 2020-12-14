package com.hazlewood.coffeetracker.purchases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazlewood.coffeetracker.beans.Beans;
import com.hazlewood.coffeetracker.beans.BeansService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BeansPurchaseControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BeansPurchaseService beansPurchaseService;
  @MockBean private BeansService beansService;

  @Test
  public void whenRegisteringExistingBeansPurchase_ThenSuccess() throws Exception {
    var beans = new Beans("House blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    var createdBeans = new BeansPurchase(beans, BigDecimal.valueOf(250));
    createdBeans.setId(1L);

    when(beansService.exists(anyLong())).thenReturn(true);
    when(beansService.findById(anyLong())).thenReturn(Optional.of(beans));
    when(beansPurchaseService.save(ArgumentMatchers.any(BeansPurchase.class)))
        .thenReturn(createdBeans);

    var mockBeans = new Beans();
    mockBeans.setId(1L);
    var purchase = new ExistingBeansPurchaseRequest(BigDecimal.valueOf(250), 1L);

    mockMvc
        .perform(
            post("/api/purchases/existing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchase)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.beans.id", is(1)))
        .andExpect(jsonPath("$.beans.name", is(beans.getName())))
        .andExpect(jsonPath("$.beans.roastery", is(beans.getRoastery())))
        .andExpect(jsonPath("$.beans.cupProfile", is(beans.getCupProfile())))
        .andExpect(jsonPath("$.beans.countryOfOrigin", is(beans.getCountryOfOrigin())))
        .andExpect(jsonPath("$.initialQuantity", is(250)))
        .andExpect(jsonPath("$.currentQuantity", is(250)));
  }

  @Test
  public void givenNewBeans_whenRegisteringNewBeansPurchase_ThenSuccess() throws Exception {
    var beans = new Beans("House blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    var createdBeans = new BeansPurchase(beans, BigDecimal.valueOf(250));
    createdBeans.setId(1L);

    when(beansService.exists(anyLong())).thenReturn(false);
    when(beansService.save(ArgumentMatchers.any(Beans.class))).thenReturn(beans);
    when(beansPurchaseService.save(ArgumentMatchers.any(BeansPurchase.class)))
        .thenReturn(createdBeans);

    var mockBeans = new Beans();
    mockBeans.setId(1L);
    var purchase = new NewBeansPurchaseRequest(BigDecimal.valueOf(250), beans);

    mockMvc
        .perform(
            post("/api/purchases/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchase)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.beans.id", is(1)))
        .andExpect(jsonPath("$.beans.name", is(beans.getName())))
        .andExpect(jsonPath("$.beans.roastery", is(beans.getRoastery())))
        .andExpect(jsonPath("$.beans.cupProfile", is(beans.getCupProfile())))
        .andExpect(jsonPath("$.beans.countryOfOrigin", is(beans.getCountryOfOrigin())))
        .andExpect(jsonPath("$.initialQuantity", is(250)))
        .andExpect(jsonPath("$.currentQuantity", is(250)));
  }

  @Test
  public void givenExistingBeans_whenRegisteringNewBeansPurchase_ThenSuccess() throws Exception {
    var beans = new Beans("House blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    var createdBeans = new BeansPurchase(beans, BigDecimal.valueOf(250));
    createdBeans.setId(1L);

    when(beansService.exists(anyLong())).thenReturn(false);
    when(beansService.findByName(anyString())).thenReturn(Optional.of(beans));
    when(beansPurchaseService.save(ArgumentMatchers.any(BeansPurchase.class)))
        .thenReturn(createdBeans);

    var mockBeans = new Beans();
    mockBeans.setId(1L);
    var purchase = new NewBeansPurchaseRequest(BigDecimal.valueOf(250), beans);

    mockMvc
        .perform(
            post("/api/purchases/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchase)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.beans.id", is(1)))
        .andExpect(jsonPath("$.beans.name", is(beans.getName())))
        .andExpect(jsonPath("$.beans.roastery", is(beans.getRoastery())))
        .andExpect(jsonPath("$.beans.cupProfile", is(beans.getCupProfile())))
        .andExpect(jsonPath("$.beans.countryOfOrigin", is(beans.getCountryOfOrigin())))
        .andExpect(jsonPath("$.initialQuantity", is(250)))
        .andExpect(jsonPath("$.currentQuantity", is(250)));
  }
}
