package tech.selmefy.hotel.utils.email_sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String SERVICE_EMAIL_ADDRESS;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Override
    @Async
    public void sendHtmlMessage(String to, String htmlTemplate, Context ctx) {
        try {

            String htmlBody = thymeleafTemplateEngine.process(htmlTemplate, ctx);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(SERVICE_EMAIL_ADDRESS);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("Failed to send email ", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
