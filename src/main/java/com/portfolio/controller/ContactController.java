package com.portfolio.controller;

import com.portfolio.entity.ContactEntity;
import com.portfolio.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://profile2-murex.vercel.app/")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<?> submitContact(@RequestBody ContactEntity contactRequest) {
        try {
            // Send email to yourself with the contact details
            emailService.sendContactNotification(contactRequest);

            // Send thank you email to the sender
            emailService.sendThankYouEmail(contactRequest);

            return ResponseEntity.ok().body("{\"message\":\"Contact form submitted successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"error\":\"Failed to send email\"}");
        }
    }
}
