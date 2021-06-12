package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.dto.EmailDTO;
import com.example.DukeStrategicTechnologies.pki.dto.RedisDTO;
import com.example.DukeStrategicTechnologies.pki.dto.ResendCodeDTO;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public static EmailDTO generateEmailInfo(RedisDTO p) {

        String email = p.getAccount().getEmail();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail(email);
        emailDTO.setSubject("Confirm registration");
        emailDTO.setVerificationCode(p.getCode());
        return emailDTO;
    }

    public static EmailDTO generateEmailInfo(ResendCodeDTO p, String verCode) {

        String email = p.getEmail();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail(email);
        emailDTO.setSubject("Password reset");
        emailDTO.setVerificationCode(verCode);
        return emailDTO;
    }
    public void sendConfirmationEmail(EmailDTO p) throws FileNotFoundException, IOException, MessagingException {
        String FilePath = "./template.html";
        File starting = new File(System.getProperty("user.dir"));
        File file = new File(starting,"src/main/java/com/example/DukeStrategicTechnologies/pki/service/template.html");



        Document doc = Jsoup.parse(file, "utf-8");

        MimeMessage message = javaMailSender.createMimeMessage();
        Multipart multiPart = new MimeMultipart("alternative");

        MimeBodyPart htmlPart = new MimeBodyPart();
        String body = doc.body().getElementsByTag("body").toString();
        body = body.replace("[name]", p.getEmail());
        body = body.replace("[code]", p.getVerificationCode());

        htmlPart.setContent(body, "text/html; charset=utf-8");
        multiPart.addBodyPart(htmlPart);

        message.setContent(multiPart);
        message.setRecipients(Message.RecipientType.TO, p.getEmail());

        message.setSubject(p.getSubject());

        javaMailSender.send(message);

    }

    public void sendPasswordResetEmail(EmailDTO p) throws FileNotFoundException, IOException, MessagingException {
        String FilePath = "./template_reset.html";
        File starting = new File(System.getProperty("user.dir"));
        File file = new File(starting,"src/main/java/com/example/DukeStrategicTechnologies/pki/service/template_reset.html");



        Document doc = Jsoup.parse(file, "utf-8");

        MimeMessage message = javaMailSender.createMimeMessage();
        Multipart multiPart = new MimeMultipart("alternative");

        MimeBodyPart htmlPart = new MimeBodyPart();
        String body = doc.body().getElementsByTag("body").toString();
        body = body.replace("[code]", p.getVerificationCode());

        htmlPart.setContent(body, "text/html; charset=utf-8");
        multiPart.addBodyPart(htmlPart);

        message.setContent(multiPart);
        message.setRecipients(Message.RecipientType.TO, p.getEmail());

        message.setSubject(p.getSubject());

        javaMailSender.send(message);

    }
}
