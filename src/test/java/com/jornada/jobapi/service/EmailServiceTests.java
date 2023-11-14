package com.jornada.jobapi.service;

import com.jornada.jobapi.exception.RegraDeNegocioException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;
    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private freemarker.template.Configuration fmConfiguration;

    @Mock
    Template template;

    @Mock
    private FreeMarkerTemplateUtils freeMarkerTemplateUtils;
    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(emailService, "from", "${spring.mail.username}");
    }

    @Test
    public void deveTestarEnviarEmailComTemplateAprovadoComSucesso() throws MessagingException, TemplateException, IOException {
        String email = "carlos@gmail.com";
        String assunto = "assuntodsdksjds";
        String nome = "Carlos";

        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("email", email);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        FreeMarkerTemplateUtils.processTemplateIntoString(template,dados);

//        MimeMessage mimeMessage1 = mock(MimeMessage.class);

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate("template-email-aprovado.html")).thenReturn(template);


        emailService.enviarEmailComTemplateAprovado(email, assunto, nome);


        Assertions.assertNotNull(email);
        Assertions.assertNotNull(assunto);
        Assertions.assertNotNull(nome);
    }

//    @Test
    public void deveTestarEnviarEmailComTemplateAprovadoComErro() throws MessagingException, TemplateException, IOException {
        String email = "carlos@gmail.com";
        String assunto = "assuntodsdksjds";
        String nome = "Carlos";

        when(emailSender.createMimeMessage()).thenThrow(TemplateException.class);

        Assertions.assertThrows(TemplateException.class, ()-> {
            //act
            emailService.enviarEmailComTemplateAprovado(email, assunto, nome);
        });


        Assertions.assertNotNull(email);
        Assertions.assertNotNull(assunto);
        Assertions.assertNotNull(nome);
    }


    @Test
    public void deveTestarEnviarEmailComTemplateReprovadoComSucesso() throws MessagingException, TemplateException, IOException {
        String email = "carlos@gmail.com";
        String assunto = "assuntodsdksjds";
        String nome = "Carlos";

        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("email", email);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        FreeMarkerTemplateUtils.processTemplateIntoString(template,dados);


        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate("template-email-reprovado.html")).thenReturn(template);


        emailService.enviarEmailComTemplateReprovado(email, assunto, nome);


        Assertions.assertNotNull(email);
        Assertions.assertNotNull(assunto);
        Assertions.assertNotNull(nome);
    }

    @Test
    public void deveTestarGerarConteudoComTemplateAprovadoComSucesso() throws TemplateException, IOException {
        String email = "carlos@gmail.com";
        String nome = "Carlos";

        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("email", email);


        when(fmConfiguration.getTemplate("template-email-aprovado.html")).thenReturn(template);

        emailService.gerarConteudoComTemplateAprovado(nome,email);

        Assertions.assertNotNull(dados);
        Assertions.assertNotNull(template);
        Assertions.assertNotNull(nome, email);
    }

    @Test
    public void deveTestarGerarConteudoComTemplateReprovadoComSucesso() throws TemplateException, IOException {
        String email = "carlos@gmail.com";
        String nome = "Carlos";

        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("email", email);


        when(fmConfiguration.getTemplate("template-email-reprovado.html")).thenReturn(template);

        emailService.gerarConteudoComTemplateReprovado(nome,email);

        Assertions.assertNotNull(dados);
        Assertions.assertNotNull(template);
        Assertions.assertNotNull(nome, email);
    }



}
