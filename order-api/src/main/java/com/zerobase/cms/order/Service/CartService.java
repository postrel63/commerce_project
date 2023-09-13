package com.zerobase.cms.order.Service;

import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private RedisClient redisClient;

    public Cart addCart(Long customerId, AddProductCartForm form) {

        Cart cart = redisClient.get(customerId, Cart.class);
        if (cart == null){
            //카트가 없으면 새로 생성
            cart = new Cart();
            cart.setCustomerId(customerId);
        }
        // 이전에 같은 상품이 있는지
        Optional<Cart.Product> productOptional = cart.getProducts().stream()
                .filter(product1 -> product1.getId().equals(form.getId())).findFirst();

        if (productOptional.isPresent()){
            Cart.Product redisProduct = productOptional.get();
            List<Cart.ProductItem> items = form.getItems()
                    .stream().map(Cart.ProductItem::from).collect(Collectors.toList());
            Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, it -> it));


            if (!redisProduct.getName().equals(form.getName())){
                cart.addMessage(redisProduct.getName()+"의 정보가 변경되었습니다. 확인 부탁드립니다.");
            }
            for (Cart.ProductItem item : items){
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());

                if (redisItem == null){
                    redisProduct.getItems().add(item);
                }else{
                    if (redisItem.getPrice().equals(item.getPrice())){
                        cart.addMessage(redisProduct.getName()+item.getName()+"의 가격 정보가 변경되었습니다. 확인 부탁드립니다.");
                    }
                    redisItem.setCount(redisItem.getCount()+item.getCount());
                }
            }

            redisClient.put(customerId,cart);

        }else {
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
        }
            redisClient.put(customerId, cart);
            return cart;
    }


}
