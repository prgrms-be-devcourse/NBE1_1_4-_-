package practice.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.models.dto.ProductCategory;
import practice.application.models.entities.ProductEntity;
import practice.application.repositories.ProductRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * <h5>제품의 CRUD 와 관련된 {@code Service} 개체</h5>
 *
 * @see practice.application.controllers.ProductController
 */
@Service
public class ProductService {
    private final ProductRepository productRepo;

    @Autowired
    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }


    /**
     * DB 에 제품 추가
     *
     * @param product 추가할 제품
     * @return {@link ProductEntity}
     * @see practice.application.controllers.ProductController#addProduct
     */
    public ProductEntity saveProduct(ProductEntity product) {
        assert product != null;
        assert product.getProductId() == null;

        return productRepo.save(product);
    }

    /**
     * DB 내 모든 제품 반환
     *
     * @return {@code List<}{@link ProductEntity}{@code >}
     * @see practice.application.controllers.ProductController#listProducts
     */
    public List<ProductEntity> getAllProducts() {
        return productRepo.findAll();
    }

    /**
     * DB 내 특정 카테고리 제품들 반환
     *
     * @param category 검색할 카테고리
     * @return @return {@code List<}{@link ProductEntity}{@code >}
     * @see practice.application.controllers.ProductController#listProducts
     * @see ProductCategory
     */
    public List<ProductEntity> getAllProducts(String category) {
        assert category != null;

        return productRepo.findAllByCategory(category);
    }

    /**
     * DB 내 특정 {@code ID} 인 제품 반환
     *
     * @param id 제품 {@code ID}
     * @return {@link ProductEntity} or {@code null}
     * @see practice.application.controllers.ProductController#showProductDetail
     * @see practice.application.controllers.ProductController#editProductDetail
     * @see practice.application.controllers.ProductController#deleteProduct
     */
    public ProductEntity getProductById(UUID id) {
        assert id != null;

        return productRepo.findById(id)
                          .orElse(null);
    }

    /**
     * DB 내 특정 제품의 정보를 수정, 결과 반환. {@code product} 의 {@code ID} 와 일치하는 제품의 정보를 {@code product} 로 수정.
     *
     * @param product 수정할 제품의 정보.
     * @return {@link ProductEntity}
     * @warning 만약 주어진 {@code product} 의 {@code id} 로 저장된 제품이 없으면 {@code warning} 로그 후, 그대로 DB 에 저장
     * @see practice.application.controllers.ProductController#editProductDetail
     */
    public ProductEntity updateProduct(ProductEntity product) {
        assert product != null;
        assert product.getProductId() != null;

        Instant now = Instant.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        return productRepo.save(product);
    }

    /**
     * 현 DB 에 저장된 모든 제품의 개수를 반환
     *
     * @return {@code long}
     */
    public long countProducts() {
        return productRepo.count();
    }

    /**
     * 특정 제품 {@code (id)} 을 DB 에서 삭제
     *
     * @param id 삭제할 제품의 {@code ID}
     */
    public void deleteProduct(UUID id) {
        assert id != null;

        productRepo.deleteById(id);
    }
}
