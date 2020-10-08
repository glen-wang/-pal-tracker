package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping(value="/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(
            TimeEntryRepository timeEntriesRepo,
            MeterRegistry meterRegistry
    ) {
        this.timeEntryRepository = timeEntriesRepo;

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }
    @GetMapping
    public @ResponseBody ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @GetMapping(path="/{timeEntryId}")
    public @ResponseBody ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry result = timeEntryRepository.find(timeEntryId);
        return result !=null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry result = timeEntryRepository.create(timeEntryToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(path="/{timeEntryId}")
    public @ResponseBody
    ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        //return result? ResponseEntity.ok().build() :
                return ResponseEntity.noContent().build();
    }

    @PutMapping(path="/{timeEntryId}")
    public @ResponseBody ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry result = timeEntryRepository.update(timeEntryId, expected);

        return result !=null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
}
