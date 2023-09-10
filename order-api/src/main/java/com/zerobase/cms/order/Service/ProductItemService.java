package com.zerobase.cms.order.Service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    //상품 아이템만 추가


    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;

    // 판매자 아이디와 아이템 옵션 정보들( ex 에어포스 신발의 옵션)
    // 판매자 아이디와 상품 아이디로 해당 상품 컬럼 찾기
    // 해당 아이템이 없으면 해당에러
    // 있으면 상품의 옵션이 중복인지( name 이 같은 게 있는지) 판단 후 에러처리
    // 없으면 상품 저장

    @Transactional
    public Product addProductItem(Long sellerId, AddProductItemForm form){
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        if (product.getProductItems().stream()
                .anyMatch(item -> item.getName().equals(form.getName()))){
            throw new CustomException(ErrorCode.SAME_ITEM_NAME);
        }

        ProductItem productItem = ProductItem.of(sellerId, form);
        product.getProductItems().add(productItem);

        return product;

    }

    @Transactional
    public ProductItem updateProductItem(Long sellerId, UpdateProductItemForm form){
        ProductItem productItem = productItemRepository.findById(form.getProductId())
                .filter(pi -> pi.getSellerId().equals(sellerId))
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));

        productItem.setName(form.getName());
        productItem.setCount(form.getCount());
        productItem.setPrice(form.getPrice());
        return productItem;

    }

    @Transactional
    public ProductItem updateProductItemUpdateInfo(Long sellerId, UpdateProductItemForm form){
        ProductItem productItem = productItemRepository.findById(form.getProductId())
                .filter(pi -> pi.getSellerId().equals(sellerId))
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));
       productItem.updateInfo(form.getName(), form.getPrice(), form.getCount());
        return productItem;

    }

    @Transactional
    public void deleteProductItem(Long sellerId,Long productItemId){
        ProductItem productItem = productItemRepository.findById(productItemId)
                .filter(pi -> pi.getSellerId().equals(sellerId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));

        productItemRepository.delete(productItem);

    }




}
