package com.example.demo.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static com.example.demo.ContactTestData.REGEX;
import static com.example.demo.ContactTestData.REGEXLOWERCASE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ContactServiceImplTest extends AbstractServiceTest{

    @Autowired
    ContactServiceImpl service;

    @Test(expected = NullPointerException.class)
    public void testGetNull() throws Exception{
        service.createResponse(null, null);
    }

    @Test
    public void testCreateResponse() throws Exception
    {
        HttpServletResponse response = new MockHttpServletResponse();
        service.createResponse(REGEX, response);
        assertThat(response.getStatus(), is(200));
        //assertThat(service.getALong().get(), is(22L));

        service.createResponse(REGEXLOWERCASE, response);
        assertThat(response.getStatus(), is(200));
       // assertThat(service.getALong().get(), is(2L));
    }
}