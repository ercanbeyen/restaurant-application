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
@Slf4j
@ActiveProfiles("test")
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
    void testCreateItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/items/createItem")
                        .flashAttr("item", new ItemDto(null, "Test Item 4", ItemCategory.APPETIZER.toString(), 1.5D, List.of("ingredient1", "ingredient2")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/items/getItems?category=APPETIZER&pageNo=1&pageSize=5&sortField=name&sortDir=asc"));
    }

    @Test
    @Order(2)
    @DisplayName("Happy path test: Get item case")
    void testGetItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/items/getItem/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("get-item"));
    }

    @Test
    @Order(3)
    @DisplayName("Happy path test: Update item case")
    void testUpdateItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items/updateItem/{id}", 2)
                        .flashAttr("item", new ItemDto(2L, "Updated Test Item", ItemCategory.BEVERAGE.toString(), 1.5D, List.of("ingredient1", "ingredient2")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/items/getItems?category=BEVERAGE&pageNo=1&pageSize=5&sortField=name&sortDir=asc"));
    }

    @Test
    @Order(4)
    @DisplayName("Happy path test: Delete item case")
    void testDeleteItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/deleteItem/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/items/getItems?category=SALAD&pageNo=1&pageSize=5&sortField=name&sortDir=asc"));
    }
}