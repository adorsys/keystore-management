package de.adorsys.keymanagement.keyrotation.controller;

import de.adorsys.keymanagement.keyrotation.config.KeyRotation;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/clock")
@RequiredArgsConstructor
public class TimeController {

    private final KeyRotation.ResettableClock clock;

    @GetMapping
    @ApiOperation("Get current time (you can manipulate projects' time)")
    public Instant get() {
        return clock.instant();
    }

    @PutMapping("/{value}")
    @ApiOperation("Set current time (you can manipulate projects' time)")
    public void set(@PathVariable("value") Instant time) {
        clock.setInstant(time);
    }
}
