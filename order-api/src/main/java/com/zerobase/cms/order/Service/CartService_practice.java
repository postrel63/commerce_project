package com.zerobase.cms.order.Service;


import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService_practice {

    private RedisClient redisClient;

    public Cart addCart(Long customerId, AddProductCartForm form){
        //해당 고객의 장바구니 정보 가져오기
        Cart cart = redisClient.get(customerId, Cart.class);
        if (cart == null){
        //카트가 없으면 생성
            cart = new Cart();
            cart.setCustomerId(customerId);
        }
        //상품이 있는지 확인하고
        Optional<Cart.Product> productOptional = cart.getProducts().stream().filter(
                product1 -> product1.getId().equals(form.getId())).findFirst();
        //상품이 있으면
        if (productOptional.isPresent()){



        }

        //새로 가져온 상품을 list로 만들고
        //장바구니에 있는 건 map으로 뽑아서

        //상품item의 아이디가 같은지 확인
        //가격이 같은지 확인

        return null;
    }



}
