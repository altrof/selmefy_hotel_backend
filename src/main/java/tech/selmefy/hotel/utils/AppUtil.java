package tech.selmefy.hotel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtil {
    @Value("${spring.profiles.active}")
    private static String activeProfile;

    @Value("${selmefy.client.base-url}")
    private String clientBaseUrl;

    private AppUtil(){}

    public String getEnvMode() {
        return activeProfile;
    }

    public String getClientBaseUrl() {
        return clientBaseUrl;
    }
}
