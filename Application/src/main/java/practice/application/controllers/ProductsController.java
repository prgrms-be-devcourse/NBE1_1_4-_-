package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practice.application.models.DTO.ProductsAllResponseDTO;
import practice.application.models.DTO.ProductsResponseDTO;
import practice.application.models.DTO.ProductsSaveDTO;
import practice.application.models.ProductEntity;
import practice.application.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
public class ProductsController {

    private final ProductService productService;


    @GetMapping() //상품 조회
    public List<ProductsAllResponseDTO> findList() {
        List<ProductEntity> productServiceAll = productService.findAll();

        log.info("상품 조회 컨트롤러");

        return productServiceAll.stream().map(ProductsAllResponseDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{productsId}")
    public ProductsResponseDTO find(@PathVariable("productsId") String productId) {

        log.info(productId);
        ProductEntity product = productService.findOne(productId);

        return new ProductsResponseDTO(product);


    }


    @PostMapping()
    public ProductsSaveDTO save(@RequestBody ProductsSaveDTO productsSaveDTO) {
        log.info("save 컨트롤러 통과");

        ProductEntity product = productsSaveDTO.dtoToEntity();

        String id = productService.save(product);

        ProductEntity one = productService.findOne(id);

        productsSaveDTO.setId(one.getId());
        return productsSaveDTO;


    }


}
