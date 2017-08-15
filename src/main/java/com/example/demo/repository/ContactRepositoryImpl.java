package com.example.demo.repository;

import com.example.demo.utils.ResultSetToJsonStringConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

/**
 * Spring Jdbc contact repository.
 */
@Repository
public class ContactRepositoryImpl implements ContactRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSource dataSource;
    private static final String QUERY_STR = "SELECT * FROM contacts";
    private final GenericConversionService conversionService = new GenericConversionService();
    private final Converter<ResultSet, String> converter = new ResultSetToJsonStringConverter();

    @PostConstruct
    private void registerConverter() {
        conversionService.addConverter(converter);
    }

    // https://jdbc.postgresql.org/documentation/head/query.html#fetchsize-example
    public Long printAll(String regex, PrintWriter responseWriter) throws JsonProcessingException, PatternSyntaxException {
        boolean isFirst = true;
        long aLong = 0;
        Pattern pattern = Pattern.compile(regex);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            // make sure autocommit is off
            conn.setAutoCommit(false);
            stmt = conn.createStatement(TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
            // Turn use of the cursor on.
            stmt.setFetchSize(5_000);
            rs = stmt.executeQuery(QUERY_STR);
            while (rs.next()) {
                if (!pattern.matcher(rs.getString(2)).matches()) {
                    if (isFirst) {
                        responseWriter.write(converter.convert(rs));
                        isFirst = false;
                    } else
                        responseWriter.write("," + converter.convert(rs));
                    aLong++;
                }
            }
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return aLong;
    }
}
