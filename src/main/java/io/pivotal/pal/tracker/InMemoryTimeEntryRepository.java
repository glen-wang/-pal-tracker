package io.pivotal.pal.tracker;


import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//@Repository
public class InMemoryTimeEntryRepository implements  TimeEntryRepository {
    Long id = 0l;
    Map<Long, TimeEntry> repos = new HashMap<>();
    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++id);
        repos.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return repos.get(id);
    }

    public TimeEntry update(long l, TimeEntry timeEntry) {
        if(repos.get(l) !=null) {
            timeEntry.setId(l);
            repos.put(l, timeEntry);
            return timeEntry;
        } return null;
    }

    @Override
    public void delete(long id) {
        repos.remove(id);
    }

    @Override
    public List<TimeEntry> list() {
        return repos.values().stream().collect(Collectors.toList());
    }
}
