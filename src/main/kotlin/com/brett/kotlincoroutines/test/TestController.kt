package com.brett.kotlincoroutines.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1.0/test")
class TestController {
    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}
