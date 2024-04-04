package com.apex.eqp.inventory.controllers;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.services.ProductService;
import com.apex.eqp.inventory.services.RecalledProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/inventory/recalled")
public class RecalledProductController {

    private final RecalledProductService recalledProductService;

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<RecalledProduct> createProduct(@RequestBody RecalledProduct recalledProduct) {
        return ResponseEntity.ok(recalledProductService.save(recalledProduct));
    }

    @GetMapping("/")
    ResponseEntity<Collection<RecalledProduct>> findRecallProducts() {
        Collection<RecalledProduct> allRecalledProducts = recalledProductService.getAllRecalledProducts();

        return ResponseEntity.ok(allRecalledProducts);
    }



    @GetMapping(path="/Not/")
    ResponseEntity<Collection<Product>> findNotRecalledProducts(){
        Collection<Product> allProducts = productService.getAllProduct();
        Collection<RecalledProduct> recalledProducts = recalledProductService.getAllRecalledProducts();
        if(recalledProducts == null || recalledProducts.isEmpty()){
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        }
        Set<Integer> recalledIds = recalledProducts.stream().map(a->a.getId()).collect(Collectors.toSet());
        Collection<Product> resProducts = new ArrayList<>();
        for(Product product: allProducts){
            if(!recalledIds.contains(product.getId())) {
                resProducts.add(product);
            }
        }

        return new ResponseEntity<>(resProducts, HttpStatus.OK);

    }
}

