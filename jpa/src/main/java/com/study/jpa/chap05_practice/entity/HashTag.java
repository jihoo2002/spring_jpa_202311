package com.study.jpa.chap05_practice.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = {"post"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_hash_tag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_no")
    private Long id;


    private String tagName; // 해시태그 이름

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //부모객체에 변화가 발견 -> 자식도 삭제
    @JoinColumn(name = "post_no")
    private Post post; //게시글 번호만 쓰나????


}
