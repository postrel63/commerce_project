package com.zerobase.cms.user.controller.Customer;

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

    //고객 회원가입
    @PostMapping("/customer")
    public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form) {
        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }

    //고객 이메일을 받고 인증번호 인증
    @GetMapping("/customer/verify")
    public ResponseEntity<String> verifyCustomer(@RequestParam String email, String code) {
        signUpApplication.customerVerify(email, code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }

    @PostMapping("/seller")
    public ResponseEntity<String> sellerSignUp(@RequestBody SignUpForm form) {
        return ResponseEntity.ok(signUpApplication.sellerSignUp(form));
    }

    @GetMapping("/seller/verify")
    public ResponseEntity<String> verifySeller(@RequestParam String email, String code) {
        signUpApplication.sellerVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }
}
