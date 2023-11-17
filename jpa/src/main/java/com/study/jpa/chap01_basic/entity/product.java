package com.study.jpa.chap01_basic.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class product {

    @Id //primary 키라는 것을 인텔리제이에게 알려주는 아노테이션
    private int id;
    private String name;







}
