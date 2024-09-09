package practice.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.application.models.dto.ProductDTO;
import practice.application.services.ProductService;
import practice.application.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    //    상품 전체목록 조회 (모든 사용자 접근 가능)
    @GetMapping
    public List<ProductDTO> getAllProducts(){
        return productService.getAllProductList();
    }

    //    상품명으로 조회 (모든 사용자 접근 가능)
    @GetMapping("/{name}")
    public List<ProductDTO> getProductByName(@PathVariable String name){
        return productService.getProductByName(name);
    }

    //    상품 등록 (관리자만 접근 가능)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO, @RequestParam String email) {
        if (!userService.isAdmin(email)) {
            // 403 Forbidden 응답과 함께 메시지를 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("관리자만 접근 가능합니다");
        }
        ProductDTO createdProduct = productService.addProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED); // 201 Created 응답
    }


    //    상품 수정 (관리자만 접근 가능)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ProductDTO productDTO, @RequestParam String email) {
        if (!userService.isAdmin(email)) {
            // 403 Forbidden 응답과 함께 메시지를 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("관리자만 접근 가능합니다");
        }
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct); // 200 OK 응답
    }


    //    상품 삭제 (관리자만 접근 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, @RequestParam String email) {
        if (!userService.isAdmin(email)) {
            // 403 Forbidden 응답과 함께 메시지를 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("관리자만 접근 가능합니다");
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }
}