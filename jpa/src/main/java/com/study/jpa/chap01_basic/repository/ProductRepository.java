package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<product, Long> {
//엔터티의 값과 primary key 값을 줘야 함 | mapper와 같은 역할

}
