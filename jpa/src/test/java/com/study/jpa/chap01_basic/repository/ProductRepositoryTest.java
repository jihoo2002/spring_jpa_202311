package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01_basic.entity.product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //스프링 프레임워크임 | 테스트 완료 후 롤백.
@Rollback(false) //데이터가 누적되는 걸 보고 싶으니 false
class ProductRepositoryTest {

    @Autowired //test 쪽에서는 직접 autowired를 해줘야 함
            //서비스나 다른 클래스에서는 final 가능
    ProductRepository productRepository;

    @BeforeEach //테스트 메서드 전에 실행되는 메서드 (무조건 먼저 실행된다)
     void insertDummyData() {
         //given
         product p1 = product.builder()
                 .name("아이폰")
                 .category(ELECTRONIC)
                 .price(1000000)
                 .build();

         product p2 = product.builder()
                 .name("탕수육")
                 .category(FOOD)
                 .price(20000)
                 .build();

         product p3 = product.builder()
                 .name("구두")
                 .category(FASHION)
                 .price(300000)
                 .build();

         product p4 = product.builder()
                 .name("쓰레기")
                 .category(FOOD)
                 .build();
         //when
         productRepository.save(p1);
         productRepository.save(p2);
         productRepository.save(p3);
         productRepository.save(p4);

         //then

     }

     @Test
     @DisplayName("5번째 상품을 데이터베이스에 저장해야 한다.")
     void testSave () {
         //given
         product p5 = product.builder()
                 .name("정장")
                 .category(FASHION)
                 .price(1000000)
                 .build();
         //when
         product saved = productRepository.save(p5);
         //void가 아님 리턴값이 있다. insert가 제대로 진행되었을 땐 product
         // 객체가 아니면 null이 옴

         //then
         assertNotNull(saved);
     }
     
     @Test
     @DisplayName("id가 2번인 데이터를 삭제해야 한다.")
     void testRemove() {
         //given
         long id = 2L; //long 타입 , primary key가 long 타입이니까!?
         //when
        productRepository.deleteById(id);
         //then
     }
     @Test
     @DisplayName("상품 전체조회하면 상품의 개수가 4개여야 한다.")
     void testFindAll() {
         //given

         //when
         List<product> products = productRepository.findAll();
         //then
         products.forEach(System.out::println);


       



     }

     @Test
     @DisplayName("3번 상품을 조회하면 상품명이 구두일 것이다. ")
     void testFindOne () {
         //given
         Long id = 3L;
         //when
         Optional<product> product = productRepository.findById(id);
         //then
         product.ifPresent(p -> {
             assertEquals("구두", p.getName());
         });
         product foundProduct = product.get();
                 assertNotNull(foundProduct);
         System.out.println("foundProduct = " + foundProduct);

     }
     
     @Test
     @DisplayName("2번 상품의 이름과 가격을 변경해야 한다.")
     void testModify() {
         //given
         long id = 2L;
         String newName = "짜장면";
         int newPrice = 6000;
         //when
        //jpa에서는 update는 따로 메서드로 제공하지 않고,
         //조회한 후 setter로 변경하면 자동으로 update 문이 나갑니다.
         //변경 후 save 호출 하세요 그럼 update가 나갑니다.
         Optional<product> product = productRepository.findById(id);
         product.ifPresent(p ->  {
             p.setName(newName);
             p.setPrice(newPrice);

             productRepository.save(p);
         });

         //then
         assertTrue(product.isPresent());

         product p = product.get();
         assertEquals("짜장면", p.getName());

     }


}