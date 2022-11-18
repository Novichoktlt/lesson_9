package ru.gb.lesson_99.controller.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.gb.lesson_99.controller.dto.ProductDto;
import ru.gb.lesson_99.entity.Cart;
import ru.gb.lesson_99.entity.Product;
import ru.gb.lesson_99.service.ProductService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getProductList(){
        return productService.findAll();
    }

    @GetMapping("/cart")
    public List<Cart> getCartList() {
        return productService.findCartAll();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long id){
    Product product;
    if (id != null) {
        product = productService.findById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> handlePost(@Validated @RequestBody ProductDto productDto){
        ProductDto savedProductDto = productService.save(productDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/product/" + savedProductDto.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping("/add")
    public ResponseEntity<?> cartSave(@RequestParam(name = "id") Long id) {
        String flag = "save";
        Product savedCart = productService.cartSave(flag, id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/product/add/" + savedCart.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> handleUpdate(@PathVariable("productId") Long id, @Validated @RequestBody ProductDto productDto){
        productDto.setId(id);
        ProductDto savedProductDto = productService.save(productDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/product/" + savedProductDto.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("productId") Long id) {
        productService.deleteById(id);
    }

    @GetMapping("/deleteCart/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByIdCart(@PathVariable("productId") Long id) {
        productService.deleteByIdCart(id);
    }


}
