package io.pivotal.pal.tracker;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements  TimeEntryRepository {
    Long id = 0l;
    Map<Long, TimeEntry> repos = new HashMap<>();
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++id);
        repos.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

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

    public boolean delete(long id) {
        return repos.remove(id) != null;
    }

    public List<TimeEntry> list() {
        return repos.values().stream().collect(Collectors.toList());
    }
}
