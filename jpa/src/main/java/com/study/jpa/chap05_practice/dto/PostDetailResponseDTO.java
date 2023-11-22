package com.study.jpa.chap05_practice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponseDTO {
//화면단에 던질 용도로 만든 dto

    private  String writer;
    private String title;
    private  String content;
    private List<String> hashTags; //해시태그의 이름만 뽑고 싶다. post, id눈 필요없기 때문에 string 타입으로 !

    @JsonFormat(pattern = "yyyy/MM/dd") //regDate가 이 형태로 만들어 주게된다.
    private LocalDateTime regDate;



    //엔터티를 dto로 변환하는 생성자
    public  PostDetailResponseDTO(Post post) {
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.regDate = post.getCreateDate();
        this.hashTags = post.getHashTags().stream()
                                .map(HashTag::getTagName)
                                .collect(Collectors.toList()); //hashtags 객체에서 태그 이름만 뽑기
    }






}
