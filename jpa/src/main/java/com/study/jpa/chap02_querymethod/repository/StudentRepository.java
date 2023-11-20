package com.study.jpa.chap02_querymethod.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByName(String name); //by 다음 컬럼명이랑 똑같이야 함!!!

    List<Student> findByCityAndMajor(String city, String major);


    List<Student> findByMajorContaining(String major);


    //네이티브 쿼리 사용
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :nm", nativeQuery = true) //일반 sql을 내가 직접 작성 가능
    Student findNameWithSQL(@Param("nm") String name);



}
