package com.startupsdigidojo.usersandteams.user.application;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Test
    public void givenWac_whenServletContext_thenItProvidesUserController() throws Exception{
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("userController"));
    }

    @Test
    public void getMappingWithEmailReturnsUserWithIndicatedEmail() throws Exception {
        generateUser();
        mockMvc.perform(get("/v1/users/matteo.larcer@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mailAddress").value("matteo.larcer@gmail.com"));
        deleteUser();
    }

    @Test
    public void postMappingCreatesUser() throws Exception {
        mockMvc.perform(post("/v1/users/create")
                .contentType("application/json")
                .content("{\"name\":\"Ernald\",\"mailAddress\":\"enrami@unibz.org\",\"password\":\"passwordErnald\"}"))
                .andExpect(status().isOk());
        getUser1();
        deleteUser1();
    }

    @Test
    public void deleteMappingDeletesTheUser() throws Exception {
        mockMvc.perform(post("/v1/users/create")
                        .contentType("application/json")
                        .content("{\"name\":\"Ernald\",\"mailAddress\":\"enrami@unibz.org\",\"password\":\"passwordErnald\"}"))
                .andExpect(status().isOk());
        deleteUser1();
        mockMvc.perform(get("/v1/users/enrami@unibz.org"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postMappingUpdateUserMailUpdatesTheMail() throws Exception {
        generateUser1();
        mockMvc.perform(post("/v1/users/updateMail")
                        .contentType("application/json")
                        .content("{\"oldMail\":\"enrami@unibz.org\",\"newMail\":\"enrami@unibz.it\"}"))
                        .andExpect(status().isOk());
        getUser1AfterUpdate();
        mockMvc.perform(get("/v1/users/enrami@unibz.org"))
                .andExpect(status().isBadRequest());
        deleteUser1AfterUpdate();
    }

    @Test
    public void logInTest() throws Exception{
        generateUser();
        mockMvc.perform(post("/v1/users/login")
                .contentType("application/json")
                .content("{\"name\":\"Matteo\",\"mailAddress\":\"matteo.larcer@gmail.com\",\"password\":\"passwordMatteo\"}"))
                .andExpect(status().isOk());
        deleteUser();
    }

    private void generateUser() throws Exception {
        mockMvc.perform(post("/v1/users/create")
                        .contentType("application/json")
                        .content("{\"name\":\"Matteo\",\"mailAddress\":\"matteo.larcer@gmail.com\",\"password\":\"passwordMatteo\"}"))
                .andExpect(status().isOk());
    }

    private void deleteUser() throws Exception {
        mockMvc.perform(delete("/v1/users/delete")
                        .contentType("application/json")
                        .content("{\"mailAddress\":\"matteo.larcer@gmail.com\"}"))
                .andExpect(status().isOk());
    }

    private void generateUser1() throws Exception {
        mockMvc.perform(post("/v1/users/create")
                        .contentType("application/json")
                        .content("{\"name\":\"Ernald\",\"mailAddress\":\"enrami@unibz.org\",\"password\":\"passwordErnald\"}"))
                .andExpect(status().isOk());
    }

    private void getUser1() throws Exception {
        mockMvc.perform(get("/v1/users/enrami@unibz.org"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mailAddress").value("enrami@unibz.org"));
        controlGet("/v1/users/enrami@unibz.org", "Ernald", "enrami@unibz.org");
    }

    private void getUser1AfterUpdate() throws Exception {
        mockMvc.perform(get("/v1/users/enrami@unibz.it"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mailAddress").value("enrami@unibz.it"));
        controlGet("/v1/users/enrami@unibz.it", "Ernald", "enrami@unibz.it");
    }

    private void controlGet(String urlTemp, String expectedName, String expectedMail) throws Exception {
        mockMvc.perform(get(urlTemp)).andExpect(status().isOk())
                .andExpect(jsonPath("$.mailAddress").value(expectedMail))
                .andExpect(jsonPath("$.name").value(expectedName));
    }

    private void deleteUser1() throws Exception {
        mockMvc.perform(delete("/v1/users/delete")
                        .contentType("application/json")
                        .content("{\"mailAddress\":\"enrami@unibz.org\"}"))
                .andExpect(status().isOk());
    }

    private void deleteUser1AfterUpdate() throws Exception {
        mockMvc.perform(delete("/v1/users/delete")
                        .contentType("application/json")
                        .content("{\"mailAddress\":\"enrami@unibz.it\"}"))
                .andExpect(status().isOk());
    }
}

