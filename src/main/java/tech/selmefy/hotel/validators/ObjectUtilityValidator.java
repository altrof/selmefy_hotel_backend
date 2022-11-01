package tech.selmefy.hotel.validators;

import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.hasText;
import static java.util.Objects.isNull;

@Component
public class ObjectUtilityValidator {

    public static <T> Boolean IsNullOrEmpty(T element) {
        return isNull(element) || !hasText(String.valueOf(element));
    }
}
