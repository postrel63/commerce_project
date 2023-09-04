package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    public void EmailTest() {
        //given
        SendMailForm form = SendMailForm.builder()
                .from("test@Email.com")
                .to("postrel6333@gmail.com")
                .subject("EmailTest")
                .text("Test")
                .build();

        Response response = mailgunClient.sendEmail(form);
        Assertions.assertNotNull(response);
        //when
        //then
    }

}