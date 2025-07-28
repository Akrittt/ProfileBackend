package com.portfolio.service;

import com.portfolio.entity.ContactEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${portfolio.owner.email}")
    private String ownerEmail;

    public void sendContactNotification(ContactEntity contactRequest) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(ownerEmail);
        helper.setSubject("New Contact Form Submission: " + contactRequest.getSubject());

        String emailBody = createContactNotificationBody(contactRequest);
        helper.setText(emailBody, true);

        mailSender.send(message);
    }

    public void sendThankYouEmail(ContactEntity contactRequest) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(contactRequest.getEmail());
        helper.setSubject("Thank you for contacting me!");

        String emailBody = createThankYouEmailBody(contactRequest);
        helper.setText(emailBody, true);

        mailSender.send(message);
    }

    private String createContactNotificationBody(ContactEntity contactRequest) {
        return String.format(
                "<html><body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
                        "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                        "<h2 style='color: #8b5cf6; border-bottom: 2px solid #8b5cf6; padding-bottom: 10px;'>New Contact Form Submission</h2>" +
                        "<div style='background-color: #f8fafc; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                        "<p><strong style='color: #4a5568;'>Name:</strong> %s</p>" +
                        "<p><strong style='color: #4a5568;'>Email:</strong> %s</p>" +
                        "<p><strong style='color: #4a5568;'>Subject:</strong> %s</p>" +
                        "</div>" +
                        "<div style='margin: 20px 0;'>" +
                        "<h3 style='color: #4a5568;'>Message:</h3>" +
                        "<div style='background-color: #ffffff; padding: 15px; border-left: 4px solid #8b5cf6; margin: 10px 0;'>" +
                        "<p>%s</p>" +
                        "</div>" +
                        "</div>" +
                        "<hr style='border: none; border-top: 1px solid #e2e8f0; margin: 30px 0;'>" +
                        "<p style='color: #718096; font-size: 14px;'>This message was sent from your portfolio contact form.</p>" +
                        "</div>" +
                        "</body></html>",
                contactRequest.getName(),
                contactRequest.getEmail(),
                contactRequest.getSubject(),
                contactRequest.getMessage().replace("\n", "<br>")
        );
    }

    private String createThankYouEmailBody(ContactEntity contactRequest) {
        return String.format(
                "<html><body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
                        "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                        "<h2 style='color: #8b5cf6; border-bottom: 2px solid #8b5cf6; padding-bottom: 10px;'>Thank You for Reaching Out!</h2>" +
                        "<p>Hi %s,</p>" +
                        "<p>Thank you for contacting me through my portfolio. I've received your message and will get back to you as soon as possible.</p>" +
                        "<div style='background-color: #f8fafc; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                        "<h3 style='color: #4a5568; margin-top: 0;'>Your Message Summary:</h3>" +
                        "<p><strong>Subject:</strong> %s</p>" +
                        "<p><strong>Message:</strong></p>" +
                        "<div style='background-color: #ffffff; padding: 15px; border-left: 4px solid #8b5cf6;'>" +
                        "<p>%s</p>" +
                        "</div>" +
                        "</div>" +
                        "<p>I typically respond within 24-48 hours. If your inquiry is urgent, please feel free to reach out to me directly.</p>" +
                        "<p>Best regards,<br>Akrit Gupta</p>" +
                        "<hr style='border: none; border-top: 1px solid #e2e8f0; margin: 30px 0;'>" +
                        "<p style='color: #718096; font-size: 14px;'>This is an automated response from the portfolio contact form.</p>" +
                        "</div>" +
                        "</body></html>",
                contactRequest.getName(),
                contactRequest.getSubject(),
                contactRequest.getMessage().replace("\n", "<br>")
        );
    }
}