package com.example.demo.utils;

import com.example.demo.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * class converts ResultSet's row to json format string
 */
public class ResultSetToJsonStringConverter implements Converter<ResultSet, String> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public String convert(ResultSet rs) {

        if (rs == null) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(Contact.class),
                    TypeDescriptor.valueOf(String.class), rs, null);
        }

        String contactStr = null;
        try {
            //{"id":53,"name":"Зуб Алексей"}
            contactStr = "{\"id\":" +
                    rs.getLong(1) +
                    ",\"name\":\"" +
                    rs.getString(2) +
                    "\"}";
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
        }
        return contactStr;
    }
}
