package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductItemForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    @Audited
    private String name;

    @Audited //실시간 변동 가능성있는것만
    private Integer price;

    private Integer count;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    public static ProductItem of(Long sellerId, AddProductItemForm form){
        return ProductItem.builder()
                .sellerId(sellerId)
                .name(form.getName())
                .price(form.getPrice())
                .count(form.getCount())
                .build();
    }





}
