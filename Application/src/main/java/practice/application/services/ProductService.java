package practice.application.services;


import jakarta.transaction.Transactional;
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

    //    상품 등록
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO){
//      @CreationTimestamp,@UpdateTimestamp는 데이터베이스에 저장될 때만 설정되기 때문에 save()후 반환된 entity객체의 생성,수정시각이 null로 되어있음.
//      그러므로 DB에서 id로 다시 조회해서 날짜시각정보 저장하자
        ProductEntity savedProduct=productRepository.save(productDTO.toProductEntity());

//        flush를 사용하여  즉시 데이터베이스에 변경 사항을 반영하도록,,생성된 타임스탬프 반영
        productRepository.flush();
        return new ProductDTO(savedProduct);
    }

    //    상품 수정
    @Transactional
    public ProductDTO updateProduct(UUID productId,ProductDTO productDTO){
        Optional< ProductEntity> optionalUpdatedProduct=productRepository.findById(productId);
//       해당 id를 가진 상품이 없으면
        if(!optionalUpdatedProduct.isPresent()){
            throw new NoSuchElementException("유효하지 않은 상품입니다.");
        }
        ProductEntity productEntity=optionalUpdatedProduct.get();

//       현재 productDTO의 createdAt값이 null이므로 productEntity의  createdAt 속성값 대입해줌
        productDTO.setCreatedAt(productEntity.getCreatedAt());
        productDTO.setProductId(productId);
        ProductEntity updatedProduct=productRepository.save(productDTO.toProductEntity());
        productRepository.flush();
        return new ProductDTO(updatedProduct);
    }
    //    ID로 상품 삭제
    @Transactional
    public void deleteProduct(UUID productId){
        Optional<ProductEntity> optionalProductEntity=productRepository.findById(productId);
//       해당 id를 가진 상품이 없으면
        if(!optionalProductEntity.isPresent()){
            throw new NoSuchElementException("유효하지 않은 상품입니다.");
        }
        ProductEntity productEntity=optionalProductEntity.get();
        productRepository.delete(productEntity);
    }

}
