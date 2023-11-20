package com.study.jpa.chap01_basic.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@ToString @EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "tbl_product")
public class product {

    @Id //primary 키라는 것을 인텔리제이에게 알려주는 아노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) //테이블 생성할 떄 시퀀스 자동 생성
    @Column(name = "prod_id")
    private long id;
    @Column(name ="prod_name" , nullable = false, unique = true, length = 30)
    private String name;

    private int price;
    @Enumerated(EnumType.STRING)
    private Category category;

    @CreationTimestamp //default sysdate임
    @Column(updatable = false) //등록시간이 한번 결정되면 수정이 안되게 막을 수 있는 아노테이션
    private LocalDateTime createDate;
    @UpdateTimestamp //update 될때 값 따로 안줘도 자동으로 값 매겨줌

    private LocalDateTime updateDate;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }






}
