package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.service.Customer.SignUpCustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService service;

    @Test
    void signUp() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("email")
                .name("홍길동")
                .birth(LocalDate.now())
                .password("1")
                .phone("01000000000")
                .build();
        //when
        //then

        Customer customer = service.signUp(form);
        Assertions.assertEquals(customer.getEmail(),"email");
        assertNotNull(customer.getBirth());
    }
}