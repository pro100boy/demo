package com.example.demo.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContactServiceImplTest extends AbstractServiceTest{

    @Autowired
    ContactService service;

    @Test
    public void testGetAll() throws Exception {
        /*List<RestaurantTo> restaurantsTo = service.getAll();
        Assert.assertEquals(restaurantsTo.size(), 3);*/
    }

}