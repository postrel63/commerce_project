package com.zerobase.cms.user.domain.seller;

import com.zerobase.cms.user.domain.model.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class sellerDto {

    private Long id;
    private String email;

    public static sellerDto from(Seller seller) {
        return new sellerDto(seller.getId(), seller.getEmail());

    }
}
