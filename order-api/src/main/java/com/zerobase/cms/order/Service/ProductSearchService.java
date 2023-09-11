package com.zerobase.cms.order.Service;


import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductRepository productRepository;


    //쿼리를 사용한 검색
    public List<Product> searchByName(String name){
        return productRepository.searchByName(name);
    }


    public Product getByProductId(Long productId){
        return productRepository.findWithProductItemsById(productId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public List<Product> getListByProductIds(List<Long> productIds){
        return productRepository.findAllById(productIds);
    }


}
