package com.study.jpa.chap02_querymethod.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void insertData() { //이 메서드가 젤 먼저 실행
        Student s1 = Student.builder()
                .name("춘식이")
                .city("서울시")
                .major("수학과")
                .build();
        Student s2 = Student.builder()
                .name("언년이")
                .city("부산시")
                .major("수학교육과")
                .build();
        Student s3 = Student.builder()
                .name("대길이")
                .city("한양도성")
                .major("체육과")
                .build();

        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);
    }

    @Test
    @DisplayName("이름이 춘식이인 학생의 정보를 조회해야 한다")
    void testFindByName() {
        //given
        String name = "춘식이";
        //when
        List<Student> students = studentRepository.findByName(name); //jpa 기본 제공이 아닌 내가 만든 메서드
        //then
        assertEquals(1, students.size()); //춘식이는 한명밖에 없을 것이다. 
        System.out.println("students.get(0) = " + students.get(0));
    }
    @Test
    @DisplayName("testfindByCityAndMajor")
    void testfindByCityAndMajor() {
        //given
        String city = "부산시";
        String major = "수학교육과";
        //when
        List<Student> students = studentRepository.findByCityAndMajor(city, major); //jpa 기본 제공이 아닌 내가 만든 메서드
        //then
        assertEquals(1, students.size()); //춘식이는 한명밖에 없을 것이다.
        assertEquals("언년이", students.get(0).getName());
        System.out.println("students.get(0) = " + students.get(0));
    }

    @Test
    @DisplayName("testfindByMajorContaining")
    void testFindByMajorContaining() {
        //given
        String major = "수학";
        //when
        List<Student> students = studentRepository.findByMajorContaining(major); //jpa 기본 제공이 아닌 내가 만든 메서드
        //then
        assertEquals(2, students.size()); //춘식이는 한명밖에 없을 것이다.
        System.out.println("\n\n\n");
        students.forEach((System.out::println));
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("testfindByMajorContaining")
    void testNativeSQL() {
        //given
        String name = "대길이";
        //when
        Student student = studentRepository.findNameWithSQL(name); //jpa 기본 제공이 아닌 내가 만든 메서드
        //then
        assertEquals("한양도성", student.getCity() ); //춘식이는 한명밖에 없을 것이다.
        System.out.println("\n\n\n");
        System.out.println("student = " + student);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("testFindCityWithJPQL")
    void testFindCityWithJPQL() {
        //given
        String city = "서울시";
        //when
        List<Student> list = studentRepository.getByCityWithJPQL(city);
        //then
        assertEquals("춘식이", list.get(0).getName());
        System.out.println("\n\n\n");
        System.out.println("student = " + list.get(0));
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("testSearchNameJPQL")
    void testSearchNameJPQL() {
        //given
        String name = "이";
        //when
        List<Student> list = studentRepository.searchByNameWithJPQL(name);
        //then
        assertEquals(3, list.size());
        System.out.println("\n\n\n");
       list.forEach(System.out::println);
        System.out.println("\n\n\n");
    }
    
    @Test
    @DisplayName("JPQL로 삭제하기")
    void testDeleteByJPQL() {
        //given
        String name = "대길이";

        //when
    studentRepository.deleteByNameWithJPQL(name);
        //then
        List<Student> students = studentRepository.findByName(name);
        assertEquals(0 , students.size());

    }
    
    








}