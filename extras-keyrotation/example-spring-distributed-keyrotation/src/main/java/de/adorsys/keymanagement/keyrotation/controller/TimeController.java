package de.adorsys.keymanagement.keyrotation.controller;

import de.adorsys.keymanagement.keyrotation.config.KeyRotation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/clock")
@RequiredArgsConstructor
public class TimeController {

    private final KeyRotation.ResettableClock clock;

    @GetMapping
    public Instant get() {
        return clock.instant();
    }

    @PutMapping("/{value}")
    public void set(@PathVariable("value") Instant time) {
        clock.setInstant(time);
    }
}
