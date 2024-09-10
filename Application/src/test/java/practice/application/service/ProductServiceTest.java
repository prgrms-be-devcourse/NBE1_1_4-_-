package practice.application.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.ProductEntity;
import practice.application.models.enumType.Category;
import practice.application.models.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Rollback(false)
    public void 요구사항_제품값_넣기() throws Exception {
       //given

        List<ProductEntity> productsList = new ArrayList<>();

        productsList.add(new ProductEntity("Columbia Narino", Category.COFFEE, 5000, 50,"콜롬비아산 커피" ));
        productsList.add(new ProductEntity("Brazil Serra DO Caparao", Category.Chocolate, 4000, 50,"브라질 산 커피"));
        productsList.add(new ProductEntity("Columbia Quindio", Category.WHINE, 3000,50 ,"콜롬비아산 와인"));
        productsList.add(new ProductEntity("Ethiopia Sidamo", Category.TEA, 2000, 50,"브라질 산 커피"));

        productService.saveAll(productsList);

       //when

       //then
    }


    @Test
    public void 전체상품조회() throws Exception {
       //given

        List<ProductEntity> all = productService.findAll();

        //when

       //then

        Assertions.assertThat(all.size()).isEqualTo(4);
    }

    @Test
    public void 세부상품_조회() throws Exception {
       //given


        //when
        ProductEntity findProduct = productService.findOne("0f1c946f-d460-4ef3-af28-d945962df321");


       //then


        Assertions.assertThat(findProduct.getProductName()).isEqualTo("Ethiopia Sidamo");
    }

    @Test
    public void 세부_상품_조회시_없음() throws Exception {
       //given

       //when


       //then
        assertThrows(NotFoundException.class, ()->{
            productService.findOne("0f1c946f-d460-4ef3-af28-d94596FSDFASF2df321");
        });

    }

    @Test
    public void 제품하나_생성() throws Exception {
       //given
        ProductEntity product = new ProductEntity("아샷추", Category.COFFEE, 5000, 30, "내가좋아함");
       //when
        String save = productService.save(product);

        entityManager.flush();
        entityManager.clear();


        ProductEntity productEntity = productService.findOne(save);


        //then

        Assertions.assertThat(productEntity.getProductName()).isEqualTo("아샷추");
    }

}