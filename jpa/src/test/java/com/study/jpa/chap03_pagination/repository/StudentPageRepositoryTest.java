package com.study.jpa.chap03_pagination.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//JPA는 INSERT, UPDATE,DELETE 시에 트랜젝션을 기준으로 동작하는 경우가 많음.
//기능을 보장받기 위해서는 웬만하면 트랜잭션 기능을 함께 사용해야 합니다.
//나중에 mvc 구조에서 service 클래스에 아노테이션을 첨부하면 됩니다.
@Transactional //나중에 service쪽에 붙어주면 됨
@Rollback(false)
class StudentPageRepositoryTest {

    @Autowired
    StudentPageRepository pageRepository;

/*        @BeforeEach
    void bulkInsert() {
            //학생을 147명 저장
            for(int i =1; i<=147; i++) {
                Student s= Student.builder()
                        .name("김테스트" + i)
                        .city("도시시" + i)
                        .major("전공공" + i)
                        .build();
                pageRepository.save(s);
            }
        }*/
        
        @Test
        @DisplayName("기본 페이징 테스트")
        void testBasicPagination() {
            //given
            int pageNo = 1;
            int amount = 10;

            //페이지 정보 생성
            //페이지 번호가 zero_dased -> 0이 1페이지
            PageRequest pageInfo = PageRequest.of(pageNo-1,
                                                        amount,
                    Sort.by(
                            Sort.Order.desc("name"),
                                    Sort.Order.asc("city"))
                    ); //이름은 내림차, city눈 오름차로 !
                                                      //  Sort.by("name").descending()); //정렬 기준 :엔터티 필드명 : 컬렁명 아님
                    //디폴트 오름차순, descending -> 내림차
            //부모가 구현하는 인터페이스도 구현 가능
            // 즉 pageRequest에서 가지고 있는 pageable를 자식에게 전달할 수 있다.

            //when
            Page<Student> students = pageRepository.findAll(pageInfo);//Pageable pageable를 받는 findAll 불러야 함

            
            //페이징이 완료된  총 학생 데이터 묶음
            List<Student> studenctList = students.getContent();
            
            //총 페이지 수
            int totalPages = students.getTotalPages();
            long totalElements = students.getTotalElements();
            boolean next = students.hasNext(); //다음 버튼
            boolean prev = students.hasPrevious(); //이전버튼

            //then
            System.out.println("\n\n\n");
            System.out.println("totalPages = " + totalPages);
            System.out.println("totalElements = " + totalElements);
            System.out.println("next = " + next);
            System.out.println("prev = " + prev);
            studenctList.forEach(System.out::println);

            System.out.println("\n\n\n");
        }
        
        @Test
        @DisplayName("이름 검색 + 페이징")
        void testSearchAndPagination() {
            //given
            int pageNo = 5;
            int size = 10;
            Pageable pageInfo = PageRequest.of(pageNo - 1, size);
            //when
            Page<Student> students = pageRepository.findByNameContaining("3", pageInfo); //name에 3이 들어간 게시물을 조회하겠따.
            boolean next = students.hasNext();
            boolean prev = students.hasPrevious();
            int totalPages = students.getTotalPages();
            long totalElements = students.getTotalElements();

            /*
            페이징 처리 시에 버튼 알고리즘은 jpa에서 따로 제공하지 않기 때문에
            버튼 배치 알고리즘을 수행할 클래스는 여전히 필요합니다.
            제공되는 정보는 이전보다 많기 때문에, 좀 더 수월하게 처리가 가능합니다.
             */


            //then
            System.out.println("\n\n\n");
            System.out.println("totalElements = " + totalElements);
            System.out.println("totalPages = " + totalPages);
            System.out.println("prev = " + prev);
            System.out.println("next = " + next);
            students.getContent().forEach(System.out::println); //반복문 돌려서 뽑아내기
            System.out.println("\n\n\n");
        }
        

}