package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited//엔티티의 정보가 바뀌면 그 내용을 저장.(상품 옵션이 바뀌면 장바구니에 옵션도 바뀌듯)
@AuditOverride(forClass = BaseEntity.class)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductItem> productItems = new ArrayList<>();

    public static Product of(Long sellerId, AddProductForm form) {
        return Product.builder()
                .sellerId(sellerId)
                .name(form.getName())
                .description(form.getDescription())
                .productItems(form.getItems().stream()
                        .map(piFrom -> ProductItem.of(sellerId, piFrom)).collect(Collectors.toList()))
                .build();

    }


}
