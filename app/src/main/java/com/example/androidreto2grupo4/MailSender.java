package com.example.androidreto2grupo4;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private final String email; // Tu correo predefinido
    private final String password; // Contraseña de tu correo

    public MailSender(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void enviarCorreo(final String destinatario, final String asunto, final String mensaje) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Message mensajeCorreo = new MimeMessage(session);
        mensajeCorreo.setFrom(new InternetAddress(email));
        mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensajeCorreo.setSubject(asunto);
        mensajeCorreo.setText(mensaje);

        Transport.send(mensajeCorreo);
    }
}
