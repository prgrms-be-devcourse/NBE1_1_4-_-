package practice.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.application.models.dto.ProductCategory;
import practice.application.models.dto.ProductDTO;
import practice.application.models.entities.ProductEntity;
import practice.application.services.ProductService;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * <h5>제품의 CRUD 와 관련된 {@code Controller}</h5>
 * <p>
 * 구현된 {@code EndPoint} 들
 * <li>{@link #listProducts} : {@code /list} + {@code (Optional)/category}
 * <li>{@link #showProductDetail}   : {@code /detail/{id}}
 * <li>{@link #editProductDetail}   :   {@code /edit}
 * <li>{@link #addProduct}          :   {@code /add}
 *
 * @see ProductDTO
 * @see ProductEntity
 * @see practice.application.repositories.ProductRepository
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private final Logger logger = Logger.getLogger(ProductController.class.getName());


    /**
     * 현 DB 에 저장된 모든 제품을 보여주는 {@code EndPoint}.
     * <p>{@code category} 가 주어졌으면 해당 카테고리의 모든 제품을 보여줌.
     *
     * @param category 검색하고 싶은 카테고리, {@link ProductCategory} 이름과 일치해야 함.
     * @return {@link ResponseEntity}
     */
    @GetMapping("/list")
    public ResponseEntity<?> listProducts(
            @RequestParam(value = "category", required = false) ProductCategory category
    ) {
        List<ProductEntity> entityList = category == null ?
                                         productService.getAllProducts() :
                                         productService.getAllProducts(category.toString());

        List<ProductDTO> dtoList = entityList.stream()
                                             .map(ProductEntity::toDTO)
                                             .toList();

        return ResponseEntity.ok()
                             .body(dtoList);
    }

    /**
     * 특정 제품의 정보를 보여주는 {@code EndPoint}.
     *
     * @param id 제품의 id {@code  (UUID)}
     * @return {@link ResponseEntity}
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> showProductDetail(
            @PathVariable("id") UUID id
    ) {
        ProductEntity entity = productService.getProductById(id);

        if (entity == null)
            return ResponseEntity.badRequest()
                                 .body("No such product with id [" + id + "] found.");

        return ResponseEntity.ok()
                             .body(entity.toDTO());
    }

    /**
     * 특정 제품의 정보를 편집하는 {@code EndPoint}.
     * <p>
     * 만약 주어진 {@code dto} 의 {@code id} 로 저장된 제품이 없으면 {@code warning} 로그 후, 그대로 DB 에 저장
     *
     * @param dto 편집할 제품의 정보, {@code id} 는 이전 제품과 일치해야 하고, 나머지 정보는 수정된 정보
     * @return {@link ResponseEntity}
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editProductDetail(
            @RequestBody ProductDTO dto
    ) {
        UUID id = dto.getProductId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Product id is required");

        if (productService.getProductById(id) == null)
            logger.warning("No such product with id [" + id + "] found. \nIt will be automatically added to DB");

        ProductEntity entity = dto.toEntity();
        productService.updateProduct(entity);

        return ResponseEntity.ok()
                             .body(entity.toDTO());
    }

    /**
     * 새로운 제품을 등록하는 {@code EndPoint}.
     *
     * @param productDTO 등록할 제품
     * @return {@link ResponseEntity}
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO result = productService.saveProduct(productDTO.toEntity())
                                          .toDTO();

        return ResponseEntity.ok()
                             .body(result);
    }
}

// TODO_imp delete 기능 만들어야 함.