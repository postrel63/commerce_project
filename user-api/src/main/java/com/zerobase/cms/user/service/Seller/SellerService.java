package com.zerobase.cms.user.service.Seller;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    // 회원가입
    public Seller signUp(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    //회원가입 시 이메일이 존재하는지
    public boolean isEmailExist(String email) {
        return sellerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .isPresent();
    }

    //이메일 발송 후 회원 정보 변경
    @Transactional
    public LocalDateTime changeSellerValidEmail(Long sellerId, String code) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);

        System.out.println("@@@@");
        System.out.println(code);

        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            seller.setVerificationCode(code);
            seller.setVerificationExpireAt(LocalDateTime.now().plusDays(1));
            return seller.getVerificationExpireAt();
        }
        throw new CustomException(ErrorCode.NOT_FOUND_USER);
    }

    //이메일 링크 클릭 시 인증번호 유효성 검사
    @Transactional
    public void verifyEmail(String email, String code) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)
                );
        if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.WRONG_VERIFY);
        }
        if (seller.getVerificationExpireAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFY);
        }
        if (seller.isVerify()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFY);
        }

        seller.setVerify(true);
    }

    // 로그인 시 유효성 검사
    public Optional<Seller> findValidSeller(String email, String password) {
        return sellerRepository.findByEmail(email).stream().filter(
                seller -> seller.isVerify() && seller.getPassword().equals(password)
        ).findFirst();
    }

    // 아이디와 이메일로 회원 찾기
    public Optional<Seller> findIdAndEmail(Long id, String email) {
        return sellerRepository.findById(id).stream().filter(
                seller -> seller.getEmail().equals(email)
        ).findFirst();
    }
}
