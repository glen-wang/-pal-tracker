package io.pivotal.pal.tracker;

import java.util.List;

public interface TimeEntryRepository {
    List<TimeEntry> list();

    TimeEntry update(long eq, TimeEntry any);

    TimeEntry create(TimeEntry any);

    TimeEntry find(long timeEntryId);

    void delete(long timeEntryId);
}
