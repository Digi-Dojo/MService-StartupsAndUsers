package com.startupsdigidojo.usersandteams.teamMember.application;
import jakarta.servlet.ServletContext;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class TeamMemberControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesStartupController() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("teamMemberController"));
    }

    @Test
    public void postMappingCreatesTeamMember() throws Exception {
        MvcResult result = mockMvc.perform(post("/v1/startup/create")
                        .contentType("application/json")
                        .content("{\"name\":\"DigiDojo\",\"description\":\"a fun way to create startups\"}"))
                .andReturn();
        JSONObject object = new JSONObject(result.getResponse().getContentAsString());
        Long startupId = object.getLong("id");
        MvcResult result1 = mockMvc.perform(post("/v1/users/create")
                        .contentType("application/json")
                        .content("{\"name\":\"Ernald\",\"mailAddress\":\"enrami@unibz.org\",\"password\":\"passwordErnald\"}"))
                .andReturn();
        JSONObject object1 = new JSONObject(result1.getResponse().getContentAsString());
        Long userId = object1.getLong("id");
        MvcResult result2 = mockMvc.perform(post("/v1/teammembers/create")
                .contentType("application/json")
                .content("{\"userId\":\"" + userId + "\",\"role\":\"designer\",\"startupId\":\"" + startupId + "\" }"))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject object2 = new JSONObject(result2.getResponse().getContentAsString());
        Long teamMemberId = object2.getLong("id");
        mockMvc.perform(get("/v1/teammembers/findByUSIds")
                .contentType("application/json")
                .content("{\"userId\":\"" + userId + "\",\"startupId\":\"" + startupId + "\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teamMemberId));
        mockMvc.perform(delete("/v1/teammembers/delete")
                        .contentType("application/json")
                        .content("{\"id\":\"" + teamMemberId + "\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/v1/startup/delete")
                        .contentType("application/json")
                        .content("{\"name\":\"DigiDojo\",\"description\":\"a fun way to create startups\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/v1/users/delete")
                        .contentType("application/json")
                        .content("{\"mailAddress\":\"enrami@unibz.org\"}"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void getMappingWithStartupIdReturnsUsersList() throws Exception{
//        mockMvc.perform(get("/v1/teammembers/startup/{startupId}", "2"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    public void deleteMappingShouldDeleteTeamMember() throws Exception{
//        mockMvc.perform(delete("/v1/teammembers/delete")
//                .contentType("application/json")
//                .content("{\"id\":\"3\"}"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}
