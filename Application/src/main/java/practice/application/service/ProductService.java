package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.ProductEntity;
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


}
