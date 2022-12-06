package tech.selmefy.hotel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AppUtil {

    @Value("${selmefy.client.base-url}")
    private String clientBaseUrl;

    private AppUtil(){}

    public String getClientBaseUrl() {
        return clientBaseUrl;
    }

    public String getUuidString() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }
}
