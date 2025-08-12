package lk.ijse.skillworker_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class HelloController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("hello")
    public String hello() {
        return "Hello, you are authenticated!";
    }
}