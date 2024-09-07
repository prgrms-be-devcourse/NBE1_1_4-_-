package practice.application.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.ProductEntity;
import practice.application.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @Rollback(false)
    public void 요구사항_제품값_넣기() throws Exception {
       //given

        List<ProductEntity> productsList = new ArrayList<>();

        productsList.add(new ProductEntity("Columbia Narino", "커피", 5000, "콜롬비아산 커피"));
        productsList.add(new ProductEntity("Brazil Serra DO Caparao", "초콜릿", 5000, "브라질 산 커피"));
        productsList.add(new ProductEntity("Columbia Quindio", "와인", 5000, "콜롬비아산 와인"));
        productsList.add(new ProductEntity("Ethiopia Sidamo", "차", 5000, "브라질 산 커피"));

        productService.saveAll(productsList);




       //when

       //then
    }

}