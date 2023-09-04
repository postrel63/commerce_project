package com.zerobase.cms.user.service.EmailTest;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final MailgunClient mailgunClient;

    public Response sendEmail() {

        SendMailForm form = SendMailForm.builder()
                .from("zerobaseTest@Email.com")
                .to("postrel6333@gmail.com")
                .subject("from zerobase  to postrel633")
                .text("test email")
                .build();

        return mailgunClient.sendEmail(form);

    }

}
