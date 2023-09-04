package com.zerobase.cms.user.config.filter;

import com.zerobase.cms.user.service.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//customer 로 들어오는 모든 http 요청에 대해 jwt 기반 인증을 실시하기 위한 필터
@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
public class CustomerFilter implements Filter {


    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;


    //  1. 클라이언트로 부터 전달된 http 요청에서 x-auth-token 즉 jwt 토큰을 추출
    //  2. 해당 jwt 토큰의 유효성을 검사하고
    //  3. 유효하다면 UserVo를 기반으로 해당 고객 데이터가 존재하는지 확인하고 뽑아냄.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");
        if (!jwtAuthenticationProvider.validateToken(token)){
            throw new ServletException("Invalid Access");
        }
        UserVo vo = jwtAuthenticationProvider.getUserVo(token);
        customerService.findByIdAndEmail(vo.getId(),vo.getEmail()).orElseThrow(
                () -> new ServletException("Invalid Access")
        );
        chain.doFilter(request, response);
    }
}
