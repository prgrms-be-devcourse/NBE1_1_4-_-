package practice.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.application.model.dto.response.ProductResponseDTO;
import practice.application.model.entity.common.Category;
import practice.application.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam("category") Category category) {
        try {
            List<ProductResponseDTO> allProducts = productService.findAllProducts(category);
            return ResponseEntity.ok().body(allProducts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("bad request");
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId) {
        try {
            ProductResponseDTO product = productService.findProduct(productId);
            return ResponseEntity.ok().body(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
