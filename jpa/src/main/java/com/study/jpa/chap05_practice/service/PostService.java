package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional //JPA 레파지토리는 트랜잭션 단위로 동작하기 때문에 작성해주세요
@Service
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;


    public PostListResponseDTO getPosts(PageDTO dto) {

        //pageable 객체 생성
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );  //페이지와 사이즈를 넘겨주어야 함
            //제로베이스라서 -1 해줘야 함
            //가장 최신의 글이 맨위로 올라올 수 있게 정렬

        
        //데이터베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(pageable);

        //게시물 정보만 꺼내기  |게시물 조회된 데이터 묶음
        List<Post> postList = posts.getContent();

        //게시물 정보를 dto의 형태에 맞게 변환(stream을 이용하여 객체마다 일괄 처리)
        List<PostDetailResponseDTO> detailList
                = postList.stream()
                .map(post -> new PostDetailResponseDTO(post))
                .collect(Collectors.toList());


        //DB에서 조회한 정보를 JSON 형태에 맞는 DTO로 변환 -> PostListResponseDTO
       return PostListResponseDTO.builder()
                .count(detailList.size()) //총 게시물 수가 아니라 조회된 게시물의 개수
                .pageInfo(new pageResponseDTO(posts)) //페이지 정보가 담긴 객체를 dto에게 전달해서 그쪽에서 처리하게 함
                .posts(detailList)
                .build();




    }

    public PostDetailResponseDTO getDetail(Long id) throws Exception{

        //게시글 조회 메서드 활용
        Post postEntity = getPost(id);

        return new PostDetailResponseDTO(postEntity);

    }


    //게시물 등록
    public PostDetailResponseDTO insert(PostCreateDTO dto)
    throws Exception {
        //예외가 발생한다면 controller에서 처리하게 한다.
        //게시물 저장 (아직 해시태그는 insert 되지 않음)
        Post saved = postRepository.save(dto.toEntity());//이쪽으로 엔터티가 오고 그걸 save()하는 것임

        //해시태그 저장
        List<String> hashTags = dto.getHashTags();
        if(hashTags != null && !hashTags.isEmpty()) {
            //해시태그가 널이 아니고 비어있지 않다면
            hashTags.forEach(ht -> {
                HashTag savedTag = hashTagRepository.save(
                        HashTag.builder()
                                .tagName(ht)
                                .post(saved) //저장된 원본 게시물의 정보를 post에게 전달, 그럼 몇번 게시글에 해시태그가 달렸는 지 알수있음
                                .build()   //결국 게시글의 id가 해시태그의 foreignKey
                );

                    /*
                    Post Entity는 DB에 save를 진행할 때 HashTag에 대한 내용을 갱신하지 않습니다.
                    HashTag Entity는 따로 save를 진행합니다. (테이블이 각각 나뉘어 있음)
                    HashTag는 양방향 맵핑이 되어있는 연관관계의 주인이기 때문에 save를 진행할 때 Post를 전달하므로
                    DB와 Entity와의 상태가 동일하지만,
                    Post는 HashTag의 정보가 비어있는 상태입니다.
                    Post Entity에 연관관계 편의 메서드를 작성하여 HashTag의 내용을 동기화 해야
                    추후에 진행되는 과정에서 문제가 발생하지 않습니다.
                    (Post를 화면단으로 return -> HashTag들도 같이 가야 함. -> 직접 갱신)
                    (Post를 다시 SELECT 해서 가져온다??? -> 의미없는 행동.(insert는 트랜잭션 종료 후 진행))
                 */

            saved.addHashTag(savedTag);
            });
        }
        
        

        return new PostDetailResponseDTO(saved);
    }

    public PostDetailResponseDTO modify(PostModifyDTO dto) {
        //수정 전 데이터 조회
        Post postEntity = getPost(dto.getPostNo());

        //수정 시작
        postEntity.setTitle(dto.getTitle());
        postEntity.setContent(dto.getContent());

        //수정 완료 처리 |update 하쟈
        Post modifiedPost = postRepository.save(postEntity);



        return new PostDetailResponseDTO(modifiedPost); //dto로 변환시켜서 컨트롤러에게 전달

        //만약 해시코드도 수정에 들어간다면 ?
        // 해시코드 조회, 변경, save 하고 addHashTag 해줘야 함,

    }




    //게시글 조회 메서드
    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "번 게시물이 존재하지 않습니다."));

    }

    public void delete(Long id) throws Exception{
        postRepository.deleteById(id);
    }
}
