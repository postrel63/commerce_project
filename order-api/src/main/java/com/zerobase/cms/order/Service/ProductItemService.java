package com.zerobase.cms.order.Service;

import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    //상품 아이템만 추가


    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;



}
