package com.psp.authservice.service;

import com.psp.authservice.model.ConfirmationToken;
import com.psp.authservice.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Configuration configuration;

    public void sendRegistrationEmail(User user, ConfirmationToken token) throws MessagingException, IOException, TemplateException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        Template template = configuration.getTemplate("registration-template.ftl");

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, token);
        helper.setTo(user.getEmail());
        helper.setText(html, true);
        helper.setSubject("Successful registration");
        javaMailSender.send(message);
    }
}
