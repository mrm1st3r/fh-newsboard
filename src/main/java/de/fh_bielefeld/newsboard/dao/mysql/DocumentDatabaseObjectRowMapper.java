package de.fh_bielefeld.newsboard.dao.mysql;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by felixmeyer on 03.01.17.
 */
class DocumentDatabaseObjectRowMapper implements RowMapper<DocumentDatabaseObject> {
    @Override
    public DocumentDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
        DocumentDatabaseObject document = new DocumentDatabaseObject();
        document.setId(resultSet.getInt("id"));
        document.setAuthor(resultSet.getString("author"));
        document.setTitle(resultSet.getString("title"));
        document.setSource(resultSet.getString("source"));
        document.setModuleId(resultSet.getString("module_id"));
        document.setCrawlTime(getCalendarFromTime(resultSet.getTime("crawl_time")));
        document.setCreationTime(getCalendarFromTime(resultSet.getTime("creation_time")));
        return document;
    }

    private Calendar getCalendarFromTime(Time time) {
        if (time == null) {
            return null;
        } else {
            ZonedDateTime dateTime = ZonedDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault());
            return GregorianCalendar.from(dateTime);
        }
    }
}
