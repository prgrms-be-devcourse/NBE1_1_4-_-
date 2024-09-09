package practice.application.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import practice.application.models.entities.ProductEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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

    @Test
    @DisplayName("Find By Category Test")
    void getAllProductsByCategory() {
        logger.info("Testing product getAllProductsByCategory");

        int i = 0;
        for (; i < entities.length / 2; i++) {
            entities[i].setCategory("Test Category 1");
            productService.saveProduct(entities[i]);
        }

        for (; i < entities.length; i++) {
            entities[i].setCategory("Test Category 2");
            productService.saveProduct(entities[i]);
        }

        Set<ProductEntity> entitySet = new HashSet<>(Arrays.asList(entities));

        Set<ProductEntity> category1 = new HashSet<>(productService.getAllProducts("Test Category 1"));

        Set<ProductEntity> category2 = new HashSet<>(productService.getAllProducts("Test Category 2"));

        assertTrue(entitySet.containsAll(category1));
        assertTrue(entitySet.containsAll(category2));
        assertEquals(entitySet.size(), category1.size() + category2.size());

        logger.info("Product getAllProductsByCategory test successful");
    }
}
