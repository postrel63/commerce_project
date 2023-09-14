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
@Service  // 이 클래스가 Spring Service 컴포넌트임을 나타냅니다.
@Slf4j  // 이 클래스에서 SLF4J 로깅 기능을 사용하도록 설정합니다.
@RequiredArgsConstructor  // final 또는 @NonNull 필드만으로 구성된 생성자를 자동으로 생성합니다.
public class CartService {

    private RedisClient redisClient;  // Redis 데이터베이스와 상호작용하기 위한 클라이언트입니다.

    // 고객 ID와 상품 정보를 받아, 해당 고객의 장바구니에 상품을 추가합니다.
    public Cart addCart(Long customerId, AddProductCartForm form) {

        // Redis에서 해당 고객의 장바구니 정보를 가져옵니다.
        Cart cart = redisClient.get(customerId, Cart.class);

        if (cart == null) {
            // 만약 장바구니가 없으면 새로 생성합니다.
            cart = new Cart();
            cart.setCustomerId(customerId);
        }

        // 이전에 같은 상품이 있는지 확인합니다. 있다면 Optional에 포함되어 반환됩니다.
        Optional<Cart.Product> productOptional = cart.getProducts().stream()
                .filter(product1 -> product1.getId().equals(form.getId())).findFirst();

        if (productOptional.isPresent()) {  // 같은 상품이 이미 있으면
            Cart.Product redisProduct = productOptional.get();  // 기존 상품 정보를 가져옵니다.

            //같은 상품이 있으면 그 내용과 입력받은 내용이 다른지 비교하기위해

            List<Cart.ProductItem> items = form.getItems()
                    .stream().map(Cart.ProductItem::from).collect(Collectors.toList());
                    /* 입력받은 상품 아이템들(form.getItems())
                       각각의 아이템들을 'Cart.ProductItem' 형태로 변환 후 리스트에 담습니다. */

            Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, it -> it));
                    /* 기존(레디스)상품 내부의 아이템들(redisProduct.getItems())
                       각각의 아이템들을 Key-Value 형태(ID - Product Item)로 맵에 저장합니다. */

            if (!redisProduct.getName().equals(form.getName())) {
                // 만약 기존 상품의 이름과 새로운 상품의 이름이 다르면 메시지를 추가합니다.
                cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
            }

            for (Cart.ProductItem item : items) {  // 새로운 상품 아이템들에 대해
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());  // 같은 ID를 가진 기존 아이템을 찾습니다.

                if (redisItem == null) {  // 기존에 해당 아이템이 없으면
                    redisProduct.getItems().add(item);  // 새 아이템을 추가합니다.
                } else {
                    if (!redisItem.getPrice().equals(item.getPrice())) {
                        // 가격 정보가 바뀌었다면 메시지를 추가합니다.
                        cart.addMessage(redisProduct.getName() + item.getName() + "의 가격 정보가 변경되었습니다. 확인 부탁드립니다.");
                    }
                    redisItem.setCount(redisItem.getCount() + item.getCount());  // 수량을 업데이트 합니다.
                }
            }

        } else {  // 같은 상품이 없으면
            Cart.Product product = Cart.Product.from(form);  // 입력받은 상품 정보로 새 'Cart.Product' 객체를 생성합니다.
            cart.getProducts().add(product);   // 장바구니에 새 상품을 추가합니다.
        }

        redisClient.put(customerId, cart);   /* 최종적으로 수정된 장바구니(cart)
                                                Redis 데이터베이스에 업데이트(저장) 합니다. */

        return cart;   /* 최종적으로 수정된 장바구니(cart)
                          클라이언트(호출자)에게 반환해줍니다.*/
    }
}
