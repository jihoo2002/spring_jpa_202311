package com.study.jpa.chap05_practice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModifyDTO {

    //제이슨으로 넘어온 변수랑 똑같이 필드 명 작성해야 함
    @NotBlank
    @Size(min = 1, max = 20)
    private String title;


    private String content;

    @NotNull //공백이나 빈 문자열이 들어올 수 없는 타입은 notnull로 선언
    @Builder.Default
    private Long postNo = 0L;
    //Long 타입은 빈문자열 , 공백 타입이 전혀 없음 , 그래서 notBlank 아노테이션 안됨
    //기본값 세팅 안되어 있으면
    // noarg아노테이션으로 null이 들어올 가능성이 있음 그래서 @Builder.Default을 통해 기본값 강제함




}
