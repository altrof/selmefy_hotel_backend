package tech.selmefy.hotel.utils.email_sender;

import org.thymeleaf.context.Context;

public interface EmailSender {
    void sendHtmlMessage(String to, String htmlTemplate, Context ctx);
}
