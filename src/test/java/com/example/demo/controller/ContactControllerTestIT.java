package com.example.demo.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.demo.ContactTestData.CONTENT;
import static com.example.demo.ContactTestData.REGEX;
import static com.example.demo.ContactTestData.REGEXLOWERCASE;
import static com.example.demo.controller.ContactController.REST_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactControllerTestIT extends AbstractControllerTest {

    @Test
    public void testGetPage() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("nameFilter", REGEX) // should be 22 objects
                .param("page", "1")
                .param("cnt", "100"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("nameFilter", REGEX)
                .param("page", "-1")
                .param("cnt", "100"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testContent() throws Exception {

        MvcResult result = mockMvc.perform(get(REST_URL)
                .param("nameFilter", REGEXLOWERCASE) // should be 3 objects
                .param("page", "1")
                .param("cnt", "20"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(CONTENT, equalTo(result.getResponse().getContentAsString()));

    }
}

