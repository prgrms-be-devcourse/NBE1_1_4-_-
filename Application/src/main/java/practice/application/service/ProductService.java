package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.ProductEntity;
import practice.application.models.exception.NotFoundException;
import practice.application.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional
    public List<ProductEntity> saveAll(List<ProductEntity> list){
        return  productRepository.saveAll(list);
    }

    @Transactional
    public String save(ProductEntity productEntity){
        ProductEntity save = productRepository.save(productEntity);

        return save.getId();
    }



    public List<ProductEntity> findAll(){
        return productRepository.findAll();
    }


    public ProductEntity findOne(String productId){
     return   productRepository.findById(productId).orElseThrow(() -> new NotFoundException("해당 제품을 찾을수 없습니다"));
    }


}
