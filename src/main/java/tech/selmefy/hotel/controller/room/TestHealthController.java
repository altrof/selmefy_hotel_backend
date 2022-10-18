package tech.selmefy.hotel.controller.room;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHealthController {

    @GetMapping("/test-health")
    public String getBackendTestHealth() {
        return "BACKEND TEST HEALTH: OK! <-- HARDCODED..";
    }
}
