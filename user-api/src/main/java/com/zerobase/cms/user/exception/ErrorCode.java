package com.zerobase.cms.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    WRONG_VERIFY(HttpStatus.BAD_REQUEST, "잘못된 인증 시도입니다."),
    EXPIRED_VERIFY(HttpStatus.BAD_REQUEST, "인증기간이 만료됐습니다."),
    ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증된 회원입니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "회원 정보가 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}

