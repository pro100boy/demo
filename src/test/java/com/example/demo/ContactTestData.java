package com.example.demo;

import com.example.demo.model.Contact;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ContactTestData {

    public static final Contact CONTACT1 = new Contact(1L, "Рыжова Бронислава");
    public static final Contact CONTACT2 = new Contact(2L, "Гайдученко Илья");
    public static final Contact CONTACT3 = new Contact(3L, "Малкина Фаина");

    public static final Contact CONTACT101 = new Contact(101L, "Утинцева Дарья");
    public static final Contact CONTACT102 = new Contact(102L, "Ожгибесов Эммануил");
    public static final Contact CONTACT103 = new Contact(103L, "Канадов Захар");

    public static final Contact CONTACT201 = new Contact(201L, "Аримов Павел");
    public static final Contact CONTACT202 = new Contact(202L, "Соломин Егор");
    public static final Contact CONTACT203 = new Contact(203L, "Минаева Светлана");

    public static final Contact CONTACT301 = new Contact(301L, "Аримов Казимир");
    public static final Contact CONTACT302 = new Contact(302L, "Соломин Егор");
    public static final Contact CONTACT303 = new Contact(303L, "Минаев Вениамин");

    public static final Contact CONTACT401 = new Contact(401L, "Голубева Инга");
    public static final Contact CONTACT402 = new Contact(402L, "Васильев Демьян");
    public static final Contact CONTACT403 = new Contact(403L, "Щербатых Ким");

    public static final List<Contact> CONTACTS = new LinkedList<>(Arrays.asList(
            CONTACT1, CONTACT2, CONTACT3,
            CONTACT101, CONTACT102, CONTACT103,
            CONTACT201, CONTACT202, CONTACT203,
            CONTACT301, CONTACT302, CONTACT303,
            CONTACT401, CONTACT402, CONTACT403
    ));
}
