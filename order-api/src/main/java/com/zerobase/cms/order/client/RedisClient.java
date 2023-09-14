package com.zerobase.cms.order.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();


    public <T> T get(Long key, Class<T> classType){
        return get(key.toString(),classType);
    }

    private <T> T get(String key, Class<T> classType){
        // 저장된 문자열을 다시 cart 객체로 전환. 어떤 종류의 객체로 변환할지 알려주기 위해Class<T> classType 사용
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(redisValue)){
            return null;
        }else{
            try{
                return mapper.readValue(redisValue,classType);
            }catch (JsonProcessingException e){
                log.error("Parsing error", e);
                return null;
            }
        }
    }

    public void put(Long key, Cart cart){
        put(key.toString(), cart);
    }
    private void put(String key, Cart cart){
        //cart 객체를 받아서 문자열로 변환하고 저장
        try {
            redisTemplate.opsForValue().set(key,mapper.writeValueAsString(cart));
        }catch (JsonProcessingException e){
           throw new CustomException(ErrorCode.CART_CHANGE_FAIL);
        }
    }



}
