package tech.selmefy.hotel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtil {

    @Value("${selmefy.client.base-url}")
    private String clientBaseUrl;

    private AppUtil(){}

    public String getClientBaseUrl() {
        return clientBaseUrl;
    }
}
