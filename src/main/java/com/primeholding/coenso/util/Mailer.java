package com.primeholding.coenso.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class Mailer {
    private JavaMailSender javaMailSender;

    @Autowired
    public Mailer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendEmail(String from, String to, String body) {
        return sendEmail(from, new String[]{to}, body, null, new String[]{}, new String[]{});
    }

    public boolean sendEmail(String from, String[] to, String body) {
        return sendEmail(from, to, body, null, new String[]{}, new String[]{});
    }

    public boolean sendEmail(String from, String to, String body, String subject) {
        return sendEmail(from, new String[]{to}, body, subject, new String[]{}, new String[]{});
    }

    public boolean sendEmail(String from, String[] to, String body, String subject) {
        return sendEmail(from, to, body, subject, new String[]{}, new String[]{});
    }

    public boolean sendEmail(String from, String to, String body, String subject, String[] cc) {
        return sendEmail(from, new String[]{to}, body, subject, cc, new String[]{});
    }

    public boolean sendEmail(String from, String[] to, String body, String subject, String[] cc) {
        return sendEmail(from, to, body, subject, cc, new String[]{});
    }

    public boolean sendEmail(String from, String[] to, String body, String subject, String[] cc, String[] bcc) {
        SimpleMailMessage emailContent = new SimpleMailMessage();
        emailContent.setFrom(from);
        emailContent.setTo(to);
        emailContent.setSubject(subject);
        emailContent.setCc(cc);
        emailContent.setBcc(bcc);
        emailContent.setText(body);
        try {
            javaMailSender.send(emailContent);
        } catch (MailException e) {
            return false;
        }
        return true;
    }
}
