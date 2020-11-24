package com.hazlewood.coffeetracker.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BeansControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private BeansService service;

  @Test
  public void whenCreatingValidBean_ThenReturnBean() throws Exception {
    var createdBean = new Beans( "Ancoats house blend", BigDecimal.valueOf(500));
    createdBean.setId(1L);
    when(service.save(ArgumentMatchers.any(Beans.class))).thenReturn(createdBean);

    mockMvc
        .perform(
            post("/api/beans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Beans("Ancoats house blend", BigDecimal.valueOf(500))))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Ancoats house blend")))
        .andExpect(jsonPath("$.amount", is(500)));
  }

  @Test
  public void whenCreatingInvalidBean_ThenThrowError() throws Exception {
    var invalidBeans = new Beans();
    invalidBeans.setName("Invalid beans");
    mockMvc.perform(
        post("/api/beans")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(invalidBeans)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenFindingExistingBeansById_ThenReturnMatchingBeans() throws Exception {
    var beans = new Beans("House blend", BigDecimal.valueOf(500.0));
    beans.setId(1L);
    when(service.findById(anyLong())).thenReturn(Optional.of(beans));

    mockMvc
        .perform(get("/api/beans/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is(beans.getName())))
        .andExpect(jsonPath("$.amount", is(beans.getAmount().doubleValue())));
  }

  @Test
  public void whenFindingNonExistingBeansById_ThenReturnMatchingBeans() throws Exception {
    when(service.findById(anyLong())).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/beans/1"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find beans 1"));
  }

  @Test
  public void whenFindingExistingBeansByName_ThenReturnMatchingBeans() throws Exception {
    var beans = new Beans("House blend", BigDecimal.valueOf(500.0));
    beans.setId(1L);
    when(service.findByName(anyString())).thenReturn(Optional.of(beans));

    mockMvc
        .perform(get("/api/beans/name/House blend"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is(beans.getName())))
        .andExpect(jsonPath("$.amount", is(beans.getAmount().doubleValue())));
  }

  @Test
  public void whenFindingNonExistingBeansByName_ThenReturnMatchingBeans() throws Exception {
    when(service.findByName(anyString())).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/beans/name/nonexisting beans"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find beans nonexisting beans"));
  }

  @Test
  public void given3Beans_WhenGetAllBeans_ThenReturn3Records() throws Exception {
    when(service.getAllBeans()).thenReturn(getBeansList());
    mockMvc
        .perform(get("/api/beans"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Ancoats house blend")))
        .andExpect(jsonPath("$[0].amount", is(500)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Atkinsons house blend")))
        .andExpect(jsonPath("$[1].amount", is(250)))
        .andExpect(jsonPath("$[2].id", is(3)))
        .andExpect(jsonPath("$[2].name", is("Neighbourhood house blend")))
        .andExpect(jsonPath("$[2].amount", is(1000)));
  }

  private List<Beans> getBeansList() {
    var ancoats = new Beans("Ancoats house blend", BigDecimal.valueOf(500));
    ancoats.setId(1L);
    var atkinsons = new Beans("Atkinsons house blend", BigDecimal.valueOf(250));
    atkinsons.setId(2L);
    var neighbourhood = new Beans("Neighbourhood house blend", BigDecimal.valueOf(1000));
    neighbourhood.setId(3L);

    return List.of(ancoats, atkinsons, neighbourhood);
  }
}
