package practice.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.model.dto.response.ProductResponseDTO;
import practice.application.model.entity.common.Category;
import practice.application.model.entity.ProductEntity;
import practice.application.repository.ProductRepository;
import practice.application.service.utils.ProductUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductUtils {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponseDTO> findAllProducts(Category category) {
        List<ProductEntity> products = productRepository.findAllByCategory(category);
        return products.stream()
                .map(productEntity -> new ProductResponseDTO(productEntity))
                .collect(Collectors.toList());
    }

    public ProductResponseDTO findProduct(Long productId) {
        ProductEntity product = getProduct(productId);
        return new ProductResponseDTO(product);
    }

    public ProductEntity getProduct(Long productId) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElseThrow(() -> new RuntimeException("Can't find this Product!!"));
    }
}
