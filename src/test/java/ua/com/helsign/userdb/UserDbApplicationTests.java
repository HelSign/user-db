package ua.com.helsign.userdb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.com.helsign.userdb.model.PersonRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserDbApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void deleteAll() {
        personRepository.deleteAll();
    }

    @Test
    public void createEntity() throws Exception {
        mockMvc.perform(post("/persons")
                .content("{\"firstName\":\"Teddy\",\"lastName\":\"Bear\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("persons/")));
    }

    @Test
    public void returnRepositoryIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.persons").exists());
    }

    @Test
    public void updateEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/persons")
                .content("{\"firstName\":\"Teddy\",\"lastName\":\"Bear\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(put(location).content("{\"firstName\":\"Teddy\",\"lastName\":\"Rabbit\"}"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andDo(print())
                .andExpect(jsonPath("$.firstName").value("Teddy"))
                .andExpect(jsonPath("$.lastName").value("Rabbit"));
    }

    @Test
    public void returnEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/persons")
                .content("{\"firstName\":\"Teddy\",\"lastName\":\"Rabbit\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(get(location)).andDo(print())
                .andExpect(jsonPath("$.firstName").value("Teddy"))
                .andExpect(jsonPath("$.lastName").value("Rabbit"));
    }

}
