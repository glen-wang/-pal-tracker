package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@RestController
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;

    }
    @GetMapping(path="/time-entries")
    public @ResponseBody ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @GetMapping(path="/time-entries/{timeEntryId}")
    public @ResponseBody ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry result = timeEntryRepository.find(timeEntryId);
        return result !=null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping(path="/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry result = timeEntryRepository.create(timeEntryToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(path="/time-entries/{timeEntryId}")
    public @ResponseBody
    ResponseEntity delete(@PathVariable long timeEntryId) {
        boolean result = timeEntryRepository.delete(timeEntryId);
        return result? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @PutMapping(path="/time-entries/{timeEntryId}")
    public @ResponseBody ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry result = timeEntryRepository.update(timeEntryId, expected);

        return result !=null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
}
