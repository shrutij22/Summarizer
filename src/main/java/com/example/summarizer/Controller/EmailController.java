package com.example.summarizer.Controller;

import com.example.summarizer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request.getTo(), request.getContent());
        return "Email sent successfully to " + request.getTo();
    }

    public static class EmailRequest {
        private String to;
        private String content;

        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
