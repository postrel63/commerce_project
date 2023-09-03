package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.application.SignUpApplication;
import com.zerobase.cms.user.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignUpApplication signUpApplication;

    //회원가입
    @PostMapping
    public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }

    //이메일을 받고 인증번호 인증
    @GetMapping("/verify/customer")
    public ResponseEntity<String> verifyCustomer(@RequestParam String email, String code){
        signUpApplication.customerVerify(email,code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}
