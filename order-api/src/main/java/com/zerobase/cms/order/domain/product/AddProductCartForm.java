package com.zerobase.cms.order.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductCartForm {

    private Long Id;
    private Long sellerId;
    private String name;
    private String description;
    private List<ProductItem> items;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductItem{
        private Long id;
        private String name;
        private Integer count;
        private Integer price;
    }


}
