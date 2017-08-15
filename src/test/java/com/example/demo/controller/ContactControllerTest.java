package com.example.demo.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.demo.ContactTestData.*;
import static com.example.demo.controller.ContactController.REST_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactControllerTest extends AbstractControllerTest {

    @Test
    public void testGetPage() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("nameFilter", REGEX))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testEmptyResults() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("nameFilter", ""))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadRequest() throws Exception {
        mockMvc.perform(get(REST_URL)
                .param("nameFilter", null))
                .andDo(print());
    }

    @Test
    public void testContent() throws Exception {

        MvcResult result = mockMvc.perform(get(REST_URL)
                .param("nameFilter", REGEXLOWERCASE)) // should be 2 objects
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), isIn(CONTENT));
    }
}

