package io.pivotal.pal.tracker;

import org.springframework.boot.autoconfigure.web.servlet.JspTemplateAvailabilityProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.zip.CheckedOutputStream;
//@Repository
public class JdbcTimeEntryRepository implements TimeEntryRepository {


    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries", mapper
        );
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id=?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                timeEntryId);
        return find(timeEntryId);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection-> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES(?,?,?,?) ",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            return statement;

        },keyHolder);
        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{timeEntryId},
                extractor);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
