package tech.selmefy.hotel.utils.email_sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JavaMailSenderImpl mailSenderImpl;

    @Value("${spring.mail.username}")
    private String emailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            LOGGER.info(mailSenderImpl.getHost());
            LOGGER.info(String.valueOf(mailSenderImpl.getPort()));
            LOGGER.info(mailSenderImpl.getUsername());
            LOGGER.info(String.valueOf(mailSenderImpl.getJavaMailProperties()));
            LOGGER.info(mailSenderImpl.getDefaultEncoding());
            LOGGER.info(emailSender + " send to " + to);
            mailSenderImpl.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("Failed to send email ", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
