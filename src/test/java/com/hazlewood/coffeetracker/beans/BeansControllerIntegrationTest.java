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
    var createdBean = new Beans( "Ancoats house blend", "Ancoats", "profile", "India");
    createdBean.setId(1L);
    when(service.save(ArgumentMatchers.any(Beans.class))).thenReturn(createdBean);

    mockMvc
        .perform(
            post("/api/beans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Beans("Ancoats house blend", "Ancoats", "profile", "India")))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Ancoats house blend")))
        .andExpect(jsonPath("$.roastery", is("Ancoats")))
        .andExpect(jsonPath("$.cupProfile", is("profile")));
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
    var beans = new Beans("House blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    when(service.findById(anyLong())).thenReturn(Optional.of(beans));

    mockMvc
        .perform(get("/api/beans/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is(beans.getName())))
        .andExpect(jsonPath("$.roastery", is("Ancoats")))
        .andExpect(jsonPath("$.cupProfile", is("profile")));
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
  public void whenFindingBeansByInvalidId_ThenThrowError() throws Exception {
    mockMvc
        .perform(get("/api/beans/c"))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenFindingExistingBeansByName_ThenReturnMatchingBeans() throws Exception {
    var beans = new Beans("House blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    when(service.findByName(anyString())).thenReturn(Optional.of(beans));

    mockMvc
        .perform(get("/api/beans/name/House blend"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is(beans.getName())))
        .andExpect(jsonPath("$.roastery", is(beans.getRoastery())))
        .andExpect(jsonPath("$.cupProfile", is(beans.getCupProfile())));
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
        .andExpect(jsonPath("$[0].roastery", is("Ancoats")))
        .andExpect(jsonPath("$[0].cupProfile", is("profile")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Atkinsons house blend")))
        .andExpect(jsonPath("$[1].roastery", is("Atkinsons")))
        .andExpect(jsonPath("$[1].cupProfile", is("profile")))
        .andExpect(jsonPath("$[2].id", is(3)))
        .andExpect(jsonPath("$[2].name", is("Neighbourhood house blend")))
        .andExpect(jsonPath("$[2].roastery", is("Neighbourhood")))
        .andExpect(jsonPath("$[2].cupProfile", is("profile")));
  }

  private List<Beans> getBeansList() {
    var ancoats = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    ancoats.setId(1L);
    var atkinsons = new Beans("Atkinsons house blend", "Atkinsons", "profile", "India");
    atkinsons.setId(2L);
    var neighbourhood = new Beans("Neighbourhood house blend", "Neighbourhood", "profile", "India");
    neighbourhood.setId(3L);

    return List.of(ancoats, atkinsons, neighbourhood);
  }
}
