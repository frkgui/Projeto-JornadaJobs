package com.jornada.jobapi.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @SuppressWarnings("java:S2637")
    private final JavaMailSender emailSender;
    private final freemarker.template.Configuration fmConfiguration;
    @Value("${spring.mail.username}")
    private String from;
    public void enviarEmailComTemplateAprovado(String emailDestino, String assunto, String nome) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(emailDestino);
            mimeMessageHelper.setSubject(assunto);
            String email = gerarConteudoComTemplateAprovado(nome, from);
            mimeMessageHelper.setText(email, true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public void enviarEmailComTemplateReprovado(String emailDestino, String assunto, String nome) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(emailDestino);
            mimeMessageHelper.setSubject(assunto);
            String email = gerarConteudoComTemplateReprovado(nome, from);
            mimeMessageHelper.setText(email, true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }


    public String gerarConteudoComTemplateAprovado(String nome, String email) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("email", email);

        Template template = fmConfiguration.getTemplate("template-email-aprovado.html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }

    public String gerarConteudoComTemplateReprovado(String nome, String email) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("email", email);

        Template template = fmConfiguration.getTemplate("template-email-reprovado.html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}
