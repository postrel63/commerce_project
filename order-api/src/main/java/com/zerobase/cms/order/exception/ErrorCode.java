package com.zerobase.cms.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT_ITEM(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템명이 중복입니다."),
    CART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),

    CART_DUPLICATE(HttpStatus.BAD_REQUEST, "아이템명이 중복입니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
