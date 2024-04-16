package spring.ch5_service_abstraction.h_mail_service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

public class MockMailSender implements MailSender {

    List<String> requests = new ArrayList<>();

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        requests.add(simpleMessage.getTo()[0]);
    }

    public List<String> getRequests() {
        return requests;
    }
}
