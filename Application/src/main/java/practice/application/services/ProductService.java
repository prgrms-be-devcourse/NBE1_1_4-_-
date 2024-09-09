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

    //    상품명으로 조회
    public List<ProductDTO> getProductByName(String name){
        //       해당 이름을 가진 상품이 없으면 예외 throw
        List<ProductEntity> productEntities =productRepository.findByProductName(name);
        List<ProductDTO> productDTOS=new ArrayList<>();
        for(ProductEntity productEntity:productEntities){
            productDTOS.add(new ProductDTO(productEntity));
        }
        return productDTOS;
    }


}
