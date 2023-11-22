package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Tag(name = "post API", description = "게시물 조회, 등록 및 수정, 삭제api입니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    // 리소스: 게시물 (Post)
    /*
        게시물 목록 조회: /posts            - GET , param : (page, size)
        게시물 개별 조회: /posts/{id}       - GET
        게시물 등록:     /posts            - POST, payload: (writer,title, content, hashTags)
        게시물 수정:     /posts            - PUT , PATCH, payload: (title, content, postNo)
        게시물 삭제:     /posts/{id}       - DELETE
     */
    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> list(PageDTO pagedto) {
            log.info("/api/v1/posts?page={}&size={}", pagedto.getPage(), pagedto.getSize());
           PostListResponseDTO dto = postService.getPosts(pagedto); //글 목록 가져와
        return ResponseEntity.ok().body(dto); //200이라는 데이터와 dto 나감
    }

    //게시물 개별 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.info("/api/v1/posts/{} get", id);
        try {
            PostDetailResponseDTO dto = postService.getDetail(id);
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }//만약 id 존재 안한다면 catch문
    }
    
   @Operation(summary = "게시물 작성", description = "게시물 작성을 담당하는 메서드 입니다.")
   @Parameters({
       @Parameter(name = "writer", description = "게시물의 작성자 이름을 쓰세요." , example = "김뽀삐", required = true),
       @Parameter(name = "title", description = "게시물의 제목을 쓰세요." , example = "제목", required = true),
       @Parameter(name = "content", description = "게시물의 내용을 쓰세요." , example = "내용내용"),
       @Parameter(name = "hashTags", description = "게시물의 해시태그를 쓰세요." , example = "['빠빠', '호호']")
   })
    @PostMapping // @Validated -> dto 안에 있는  @NotBlank 등을 검증
    public ResponseEntity<?> create(@Validated @RequestBody PostCreateDTO dto,
                                    BindingResult result //검증 에러 정보를 가진 객체
    ) {
        log.info("/api/v1/posts POST!!!!!! - padLoad:{}", dto);
        
        if(dto == null) {
            return ResponseEntity.badRequest().body("등록 게시물 정보를 전달해 주세요");
        }

        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
        if (fieldErrors != null) return fieldErrors;
        //위에 존재하는 if문을 모두 건너뜀 -> dto 가 null도 아니고, 입력값 검증도 모두 통과함 -> service에게 명령
        try {
            PostDetailResponseDTO responseDTO =  postService.insert(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        return ResponseEntity.internalServerError().body("미안 서버 터짐 원인: " + e.getMessage());

        }
    }
    
    


    //게시물 수정
        @RequestMapping(method = {RequestMethod.PATCH, RequestMethod.PUT}) //fatch 또는 put이 들어올수잇다
        public ResponseEntity<?> update(
                @Validated @RequestBody PostModifyDTO dto,
                BindingResult result,
                HttpServletRequest request
        ) { //BindingResult는 @Validate의 결과
            log.info("/api/v1/posts {} - payload:{}", request.getMethod(), dto);
            //요청 방식에 따라서 하나의 메서드에 서로 다른 여러 요청을 처리해야 한다면?
            //요청 방식을 파악하기 위해서  HttpServletRequest를 활용할 수 있다.
            //요청 방식을 파악해야 요청을 구분할 수 있기 때문이다.
            ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
            if(fieldErrors != null) return fieldErrors;

            //수정된 값을 사용자에게 그대로 보여주기 위해서 dto로 던져줄거임 !
            PostDetailResponseDTO responseDTO = postService.modify(dto);


            return ResponseEntity.ok().body(responseDTO);
        }




        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(@PathVariable Long id) {
            log.info("/api/v1/posts/{} DELETE@", id);
            try {
                postService.delete(id);
                return ResponseEntity.ok( "DEL SUCCESS");
            }
//            catch (SQLIntegrityConstraintViolationException e) {
//                return ResponseEntity.internalServerError()
//                        .body("해시태그가 달린 게시물은 삭제할 수 없습니다.");
//            } 에러를 잡아 사용자에게 보여줄 수 있지만 근본적 해결이 아님
 
            catch (Exception e) {
                e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
            }

        }








    //매번 메서드마다 코드 작성하기 귀찮다 메서드화 시켜주자
    //입력값 검증(Validation)의 결과를 처리해주는 전역 메서드
    private static ResponseEntity<List<FieldError>> getValidateResult(BindingResult result) {
        if(result.hasErrors()) { // 입력값 검증 단계에서 문제가 있었다면 true
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err -> {
                //에러 리스트가 하나씩 전달될 때마다
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        return null;
    }
        
        
        
        



}
