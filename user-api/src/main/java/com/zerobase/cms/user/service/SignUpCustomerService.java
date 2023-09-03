package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form){
        return customerRepository.save(Customer.from(form));
    }

    public boolean isEmailExist(String email){
     return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
             .isPresent();
    }


    // 이메일 발송 후 정보 변경
    @Transactional
    public LocalDateTime ChangeCustomerValidateEmail(Long customerId, String code) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setVerificationCode(code);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return customer.getVerifyExpiredAt();
        }
        throw new CustomException(ErrorCode.NOT_FOUND_USER);

    }

    @Transactional
    public void VerifyEmail(String email, String code){
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (customer.isVerify()){
            throw new CustomException(ErrorCode.ALREADY_VERIFY);
        }
        if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.EXPIRED_VERIFY);
        }

        if (!customer.getVerificationCode().equals(code)){
            throw new CustomException(ErrorCode.WRONG_VERIFY);
        }

        customer.setVerify(true);

    }

}
