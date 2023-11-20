package com.study.jpa.chap02_querymethod.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter //실무적 측면에서 setter 는 신중하게 선택할 것.
@Getter
@ToString
@EqualsAndHashCode(of = "id")
//id만 같아도 같은 객체라고 인식, id만 equal 한지 판단해라라는 기준을 줌,
// 두개 이상의 판단 기준을 주고 싶으면 {} 쓰면 된다.
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_student")
public class Student {

    @Id
    @Column(name = "stu_id")
    @GeneratedValue( generator = "uid") //uuid를 이용해서 시퀀스를 만들겠다.
    @GenericGenerator(strategy = "uuid", name = "uid")
    private String id;

    @Column(name = "stu_name", nullable = false) //not nyll
    private String name;

    private String city;

    private String major;



}
