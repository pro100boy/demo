package com.example.demo.controller;

import org.junit.Test;

import static com.example.demo.controller.ContactController.REST_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RootControllerTest extends AbstractControllerTest {

    @Test
    public void testRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl(REST_URL + "/?nameFilter="))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

}