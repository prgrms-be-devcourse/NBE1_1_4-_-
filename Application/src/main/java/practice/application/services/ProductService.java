package practice.application.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.models.dto.ProductDTO;
import practice.application.models.entity.ProductEntity;
import practice.application.repositories.ProductRepository;

import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

//    전체 상품 목록 조회
    public List<ProductDTO>getAllProductList(){
        List<ProductEntity>productEntityList=productRepository.findAll();
        List<ProductDTO> productList=new ArrayList<>();
        for(ProductEntity productEntity:productEntityList){
            productList.add(new ProductDTO(productEntity));
        }
        return productList;
    }



}
