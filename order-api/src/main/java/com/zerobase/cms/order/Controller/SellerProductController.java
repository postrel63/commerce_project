package com.zerobase.cms.order.Controller;

import com.zerobase.cms.order.Service.ProductItemService;
import com.zerobase.cms.order.Service.ProductService;
import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.*;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final JwtAuthenticationProvider provider;
    private  final ProductItemService productItemService;

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                           @RequestBody AddProductForm form){


        return ResponseEntity.ok(ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(),form)));
    }

    @PostMapping("/productItem")
    public ResponseEntity<ProductDto> addProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                  @RequestBody AddProductItemForm form){
        return ResponseEntity.ok(ProductDto.from(productItemService.addProductItem(provider.getUserVo(token).getId(),form)));

    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token, @RequestBody UpdateProductForm form){
        return ResponseEntity.ok(ProductDto.from(productService.UpdateProduct(provider.getUserVo(token).getId(),form)));
    }

    @PatchMapping("/productItem")
    public ResponseEntity<ProductItemDto> updateProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token, @RequestBody UpdateProductItemForm form){
        return ResponseEntity.ok(ProductItemDto.from(productItemService.updateProductItem(provider.getUserVo(token).getId(),form)));
    }


}

