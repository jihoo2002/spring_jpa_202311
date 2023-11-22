package com.study.jpa.chap05_practice.dto;

import com.study.jpa.chap05_practice.entity.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDTO {
    //@NotNull -> null을 허용하지 않음 "", " "은 허용
    //@NotEmpty -> null, ""을 허용하지 않음 " "은 허용

    @NotBlank //null, "" , " " 등 공백 포함하지 않겠다
    @Size(min = 2, max = 5)
    private String writer;

    @NotBlank
    @Size(min =1, max = 20)
    private  String title;

    private  String content;
    private List<String> hashTags;

    //dto를 엔터티로 변환하는 메서드
    public Post toEntity() {
        return Post.builder()
                .writer(this.writer)
                .content(this.content)
                .title(this.title)
                //해시태그는 여기서 넣는 게 아니에용 읽기전용이라 DB에 없어용
                .build();
    }//내가 해시태그를 빌더로 주지 않으면 post 엔터티에서 null이 간다.



}
