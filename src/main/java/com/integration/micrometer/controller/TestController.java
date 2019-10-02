package com.integration.micrometer.controller;


import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Timed
@RequestMapping("/rest")
public class TestController {

    @Timed(
            value = "chetan.hello.request",
            histogram = true,
            percentiles = {0.05, 0.10, 0.25, 0.50, 0.75, 0.95, 0.99},
            extraTags = {"version", "1.0"}
    )
    @GetMapping("/hello")
    public String hello() {
        return "Hello Youtube";
    }

    @Timed(
            value = "chetan.hello2.request",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "1.0"}
    )
    @GetMapping("/hello2")
    public String hello2() {
        return "Hello Youtube";
    }
    @Timed(
            value = "chetan.hello2.request",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "1.0"}
    )
    @GetMapping("/hello3")
    public String hello3() {
        return "Hello Youtube";
    }
    @Timed(
            value = "chetan.hello2.request",
            histogram = true,
            percentiles = {0.95, 0.99},
            extraTags = {"version", "1.0"}
    )
    @GetMapping("/hello4")
    public String hello4() {
        return "Hello Youtube";
    }
}
