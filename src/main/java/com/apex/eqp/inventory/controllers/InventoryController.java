package com.apex.eqp.inventory.controllers;

import com.apex.eqp.inventory.Validation;
import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.services.ProductService;
import com.apex.eqp.inventory.services.RecalledProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/inventory/product")
public class InventoryController {

    private final ProductService productService;

    /**
     *
     * @return all the products that are not recalled
     */
    @GetMapping
    public ResponseEntity<Collection<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        if(!isValidProduct(product)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //    //Validation for Name Price Quantity

        return ResponseEntity.ok(productService.save(product));
    }



    private boolean isValidProduct(Product product) {
        if(product == null ) return false;
        if( product.getName() == null || product.getName().isEmpty()) return false;
        if(product.getPrice() == null || (product.getPrice() > Validation.PRICE_MAX || product.getPrice() < Validation.PRICE_MIN ) ) return false;
        if(product.getQuantity() == null || (product.getQuantity() > Validation.QUANTITY_MAX || product.getQuantity() < Validation.QUANTITY_MIN) ) return false;

        return true;
    }

    @GetMapping("/{id}")
    ResponseEntity<Product> findProduct(@PathVariable Integer id) {
        Optional<Product> byId = productService.findById(id);
        return byId.map(ResponseEntity::ok).orElse(null);
    }


    @DeleteMapping
    ResponseEntity<Boolean> deleteProduct(@RequestParam Integer id){
        if(id == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Product> product = productService.findById(id);
        if(product.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        productService.deleteByProduct(product.get());
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<Product> updateProduct(@RequestBody Product product){
        if(product == null || !isValidProduct(product) ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Product savedProduct = productService.save(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.OK);
    }





    //Add Update Delete
}
