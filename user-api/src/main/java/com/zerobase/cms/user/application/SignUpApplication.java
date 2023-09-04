package com.zerobase.cms.user.application;


import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.Customer.SignUpCustomerService;
import com.zerobase.cms.user.service.Seller.SellerService;
import com.zerobase.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SellerService sellerService;

    // 구매자 회원가입 기능
    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        } else {

            Customer customer = signUpCustomerService.signUp(form);

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@dannymyteester.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(customer.getEmail(), "customer", customer.getName(), code))
                    .build();
            mailgunClient.sendEmail(sendMailForm);
            signUpCustomerService.ChangeCustomerValidateEmail(customer.getId(), code);
            return "회원가입 성공 ";
        }
    }

    // 판매자 회원가입 기능
    public String sellerSignUp(SignUpForm form) {
        if (sellerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Seller seller = sellerService.signUp(form);

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@dannymyteester.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(seller.getEmail(), "seller" ,seller.getName(), code))
                    .build();
            mailgunClient.sendEmail(sendMailForm);
            sellerService.changeSellerValidEmail(seller.getId(),code);
            return "회원가입 성공 ";
        }
    }

    //apache.commons-lang3 라이브러리 사용 랜덤 인증번호
    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String type, String name, String code) {
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello").append(name).append("! Click Link for verification \n")
                .append("http://localhost:8081/signup/"+type+"/verify/?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }

    //이메일 인증
    public void customerVerify(String email, String code) {
        signUpCustomerService.VerifyEmail(email, code);

    }

    public void sellerVerify(String email, String code){
        sellerService.verifyEmail(email,code);
    }
}
