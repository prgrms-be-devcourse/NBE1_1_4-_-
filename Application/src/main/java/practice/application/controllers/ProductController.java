package practice.application.controllers;

import jakarta.transaction.Transactional;
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
 * <li>{@link #addProduct}          :   {@code /add}
 * <li>{@link #listProducts} : {@code /list} + {@code (Optional)/category}
 * <li>{@link #showProductDetail}   : {@code /detail}
 * <li>{@link #editProductDetail}   :   {@code /edit}
 * <li>{@link #deleteProduct}       :   {@code /delete}
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
     * 새로운 제품을 등록하는 {@code EndPoint}.
     *
     * <li>상품 추가할 때 반드시 필요한 {@link ProductDTO} {@code Fields} :
     * <pre class="code">
     *      UUID productId = [ null | empty ]
     *      String productName;
     *      ProductCategory category;
     *      long price;
     *  </pre>
     *
     * @param productDTO 제품 정보가 포함된 {@code DTO}
     * @return {@link ResponseEntity}
     * @warning {@code ProductCategory category} 는 반드시 {@link ProductCategory} 원소와 일치하는 {@code (ProductCategory.valueOf(...))} {@code String} 이어야 함.
     * @see ProductCategory
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        UUID id = productDTO.getProductId();

        if (id != null) {
            logger.warning("[addProduct] - Given Product id is not null. Silently set id as null.");
            productDTO.setProductId(null);
        }

        ProductDTO result = productService.saveProduct(productDTO.toEntity())
                                          .toDTO();

        return ResponseEntity.ok()
                             .body(result);
    }

    /**
     * 현 DB 에 저장된 모든 제품을 보여주는 {@code EndPoint}.
     * <p>{@code category} 가 주어졌으면 해당 카테고리의 모든 제품을 보여줌.
     *
     * @param category 검색하고 싶은 카테고리, {@link ProductCategory} 이름과 일치해야 함.
     * @return {@link ResponseEntity}
     * @see ProductCategory
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
     * <li>상품 상세 내용 조회할 때 반드시 필요한 {@link ProductDTO} {@code Fields} :
     * <pre class="code">
     *      UUID productId;
     *  </pre>
     *
     * @param productDTO 제품 {@code ID} 가 포함된 {@code DTO}
     * @return {@link ResponseEntity}
     */
    @GetMapping("/detail")
    public ResponseEntity<?> showProductDetail(
            @RequestBody ProductDTO productDTO
    ) {
        UUID id = productDTO.getProductId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Product id is required");

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
     * 만약 주어진 {@code productDTO} 의 {@code id} 로 저장된 제품이 없으면 {@code warning} 로그 후, 그대로 DB 에 저장
     *
     * <li>상품 정보 편집할 때 반드시 필요한 {@link ProductDTO} {@code Fields} :
     * <pre class="code">
     *      UUID productId;
     *      String productName;
     *      ProductCategory category;
     *      long price;
     *  </pre>
     *
     * @param productDTO 편집할 제품의 정보, {@code ID} 는 이전 제품과 일치해야 하고, 나머지 정보는 수정된 정보
     * @return {@link ResponseEntity}
     * @see ProductDTO
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editProductDetail(
            @RequestBody ProductDTO productDTO
    ) {
        UUID id = productDTO.getProductId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Product id is required");

        if (productService.getProductById(id) == null)
            logger.warning("No such product with id [" + id + "] found. " + "It will be automatically added to DB");

        ProductEntity resultEntity = productService.updateProduct(productDTO.toEntity());

        return ResponseEntity.ok()
                             .body(resultEntity.toDTO());
    }

    /**
     * 특정 제품을 {@code DB} 에서 삭제하는 {@code EndPoint}.
     *
     * <li>상품 정보 편집할 때 반드시 필요한 {@link ProductDTO} {@code Fields} :
     * <pre class="code">
     *      UUID productId;
     *  </pre>
     *
     * @param productDTO 삭제할 제품 {@code ID} 가 포함된 {@code DTO}
     * @return {@link ResponseEntity}
     */
    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(
            @RequestBody ProductDTO productDTO
    ) {
        UUID id = productDTO.getProductId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Product id is required");

        ProductEntity entity = productService.getProductById(id);

        if (entity == null)
            logger.warning("No such product with id [" + id + "] found. " + "It will silently ignored.");

        productService.deleteProduct(id);

        return ResponseEntity.ok()
                             .body(entity == null ?
                                   null :
                                   entity.setDescription("<< Product has been deleted >>")
                                         .toDTO());
    }
}