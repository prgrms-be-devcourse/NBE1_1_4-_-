package practice.application.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import practice.application.models.entities.ProductEntity;

import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProductServiceTest {
    @Autowired
    ProductService productService;

    ProductEntity[] entities = new ProductEntity[10];

    @BeforeEach
    void init() {
        for (int i = 0; i < 10; i++) {
            entities[i] = new ProductEntity();
            entities[i].setProductName("Product " + i);
            entities[i].setPrice((i + 1) * 1000L);
            entities[i].setCategory("Category " + i);
        }
    }

    static Logger logger = Logger.getLogger(ProductServiceTest.class.getName());

    @Test
    @DisplayName("Save Test")
    public void save() {
        logger.info("Testing product save");

        ProductEntity[] resultEntities = new ProductEntity[entities.length];
        for (int i = 0; i < entities.length; i++)
             resultEntities[i] = productService.saveProduct(entities[i]);

        assertArrayEquals(entities, resultEntities);

        logger.info("Product save test successful");
    }

    @Test
    @DisplayName("Find Test")
    public void find() {
        logger.info("Testing product find");

        for (ProductEntity entity : entities)
            productService.saveProduct(entity);

        ProductEntity[] resultEntities = new ProductEntity[entities.length];
        for (int i = 0; i < entities.length; i++)
             resultEntities[i] = productService.getProductById(entities[i].getProductId());

        assertArrayEquals(entities, resultEntities);

        logger.info("Product find test successful");
    }

    @Test
    @DisplayName("Delete Test")
    public void delete() {
        logger.info("Testing product delete");

        for (ProductEntity entity : entities)
            productService.saveProduct(entity);

        for (ProductEntity entity : entities) {
            UUID id = entity.getProductId();
            productService.deleteProduct(id);
            assertNull(productService.getProductById(id));
        }

        logger.info("Product delete test successful");
    }

    @Test
    @DisplayName("Update Test")
    public void update() {
        logger.info("Testing product update");

        for (ProductEntity entity : entities)
            productService.saveProduct(entity);

        long numOfProducts = productService.countProducts();
        ProductEntity[] resultEntities = new ProductEntity[entities.length];

        for (int i = 0; i < entities.length; i++) {
            entities[i].setProductName("Updated Product " + i);
            productService.updateProduct(entities[i]);
            resultEntities[i] = productService.getProductById(entities[i].getProductId());
        }

        assertEquals(numOfProducts, productService.countProducts());
        assertArrayEquals(entities, resultEntities);

        logger.info("Product update test successful");
    }
}
