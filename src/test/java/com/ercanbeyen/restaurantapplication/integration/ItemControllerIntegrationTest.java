package com.ercanbeyen.restaurantapplication.integration;

import com.ercanbeyen.restaurantapplication.constant.enums.ItemCategory;
import com.ercanbeyen.restaurantapplication.dto.ItemDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Sql(value = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemControllerIntegrationTest {
    @Resource
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void start() {
        log.info("ItemControllerIntegrationTest::initial --> BeforeAll");
    }

    @BeforeEach
    void setUp() {
        log.info("ItemControllerIntegrationTest::initial --> BeforeEach");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @AfterEach
    void tearDown() {
        log.info("ItemControllerIntegrationTest::tearDown --> AfterEach");
    }

    @AfterAll
    static void end() {
        log.info("ItemControllerIntegrationTest::end --> AfterAll");
    }

    @Test
    @Order(1)
    @DisplayName("Happy path test: Create item case")
    void givenItemDto_whenCreateItem_thenRedirectToGetItemsView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items/createItem")
                        .flashAttr("item", new ItemDto(null, "Test Item 4", ItemCategory.APPETIZER.toString(), 1.5D, List.of("ingredient1", "ingredient2")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/items/getItems?category=APPETIZER&pageNo=1&pageSize=5&sortField=name&sortDir=asc"));
    }

    @Test
    @Order(2)
    @DisplayName("Exception path test: Create item case")
    void givenItemDto_whenCreateItem_thenRedirectToErrorView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items/createItem")
                        .flashAttr("item", new ItemDto(null, "Test Item 4", ItemCategory.APPETIZER.toString(), 1.5D, List.of("ingredient1", "ingredient2")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @Order(3)
    @DisplayName("Happy path test: Get item case")
    void givenId_whenGetItem_thenRedirectToGetItemView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/getItem/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("item"))
                .andExpect(MockMvcResultMatchers.view().name("get-item"));
    }

    @Test
    @Order(4)
    @DisplayName("Exception path test: Get item case")
    void givenId_whenGetItem_thenRedirectToErrorView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/getItem/{id}", 25)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @Order(5)
    @DisplayName("Happy path test: Update item case")
    void givenItemDto_whenUpdateItem_thenRedirectToGetItemsView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items/updateItem/{id}", 2)
                        .flashAttr("item", new ItemDto(2L, "Updated Test Item", ItemCategory.BEVERAGE.toString(), 1.5D, List.of("ingredient1", "ingredient2")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/items/getItems?category=BEVERAGE&pageNo=1&pageSize=5&sortField=name&sortDir=asc"));
    }

    @Test
    @Order(6)
    @DisplayName("Exception path test: Update item case")
    void givenItemDto_whenUpdateItem_thenRedirectToErrorView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items/updateItem/{id}", 2)
                        .flashAttr("item", new ItemDto(2L, "Test Item 4", ItemCategory.BEVERAGE.toString(), 1.5D, List.of("ingredient1", "ingredient2")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @Order(7)
    @DisplayName("Happy path test: Search items case")
    void givenName_whenSearchItems_thenRedirectToSearchItemsView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/searchItems").param("name", "Test Item")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("items"))
                .andExpect(MockMvcResultMatchers.view().name("search-items"));

    }

    @Test
    @Order(8)
    @DisplayName("Happy path test: Delete item case")
    void givenId_whenDeleteItem_thenRedirectToGetItemsView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/deleteItem/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/items/getItems?category=SALAD&pageNo=1&pageSize=5&sortField=name&sortDir=asc"));
    }

    @Test
    @Order(9)
    @DisplayName("Exception path test: Delete item case")
    void givenId_whenDeleteItem_thenRedirectToErrorView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/deleteItem/{id}", 25))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @Order(10)
    @DisplayName("Happy path test: Get item by name case")
    void givenName_whenGetItemByName_thenRedirectToGetItemView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/getItemByName/{name}", "Updated Test Item")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("item"))
                .andExpect(MockMvcResultMatchers.view().name("get-item"));
    }

    @Test
    @Order(11)
    @DisplayName("Exception path test: Get item by name case")
    void givenName_whenGetItemByName_thenRedirectToErrorView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/getItemByName/{name}", "Unknown Test Item")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("item"))
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @Order(12)
    @DisplayName("Happy path test: Show menu page case")
    void whenShowMenuPage_thenRedirectToMenuView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/menu"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("menu"));
    }
}