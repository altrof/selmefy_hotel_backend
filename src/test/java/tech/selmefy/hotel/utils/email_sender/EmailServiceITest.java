package tech.selmefy.hotel.utils.email_sender;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.thymeleaf.context.Context;
import tech.selmefy.hotel.AbstractIntegrationTest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmailServiceITest extends AbstractIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("test.sender@selmefy.tech", "testpw"))
        .withPerMethodLifecycle(false);

    @Autowired
    private EmailService emailService;

    @Test
    void greenMail_isWork() {
        List<MimeMessage> receivedMessage = List.of(greenMail.getReceivedMessages());

        assertEquals(0, receivedMessage.size());
    }

    @Test
    void sendHtmlMessage_greenMailShouldContainMessage_WhatIsSent() throws MessagingException, IOException {


        String link = "https://selmefy/confirm?token=";

        final Context ctx = new Context();
        ctx.setVariable("name", "SelmefyTest");
        ctx.setVariable("confirmationUrl", link);

        emailService.sendHtmlMessage(
            "test@email.com",
            "/html/email_confirm.html",
            ctx
        );

        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();

        assertEquals(1, receivedMessage.length);
        assertEquals("test.sender@selmefy.tech", receivedMessage[0].getFrom()[0].toString());
        assertEquals("test@email.com", receivedMessage[0].getAllRecipients()[0].toString());
        assertTrue(receivedMessage[0].getContent().toString().contains("<!DOCTYPE html>"));
        assertTrue(receivedMessage[0].getContent().toString().contains("<title>Email Confirmation</title>"));
        assertTrue(receivedMessage[0].getContent().toString().contains("<p style=\"margin: 0;\">Hello, SelmefyTest</p>"));
        assertTrue(receivedMessage[0].getContent().toString().contains(link));
    }

    @Test
    void sendHtmlMessage_throwsIllegalStateException_WhenEmailToIsEmpty() {
        String link = "https://selmefy/confirm?token=";

        final Context ctx = new Context();
        ctx.setVariable("name", "SelmefyTest");
        ctx.setVariable("confirmationUrl", link);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            emailService.sendHtmlMessage(
                "",
                "/html/email_confirm.html",
                ctx
            );
        });

        assertEquals("Failed to send email", exception.getMessage());
    }
}
