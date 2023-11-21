package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {

        @Autowired
        DepartmentRepository departmentRepository;

        @Autowired
        EmployeeRepository employeeRepository;

        @Autowired
        //엔터티들을 관리하는 역할을 수행하는 클래스.(영속성 컨텍스트를 관리,
        // spring data jpa에서만 사용하는 게 x)
        // 영속성 컨텍스트 내의 내용들을 DB에 반영시키거나 비워내거나 수명을 관리할 수 있는 객체. (순서 뒤바꿀수있다)
        EntityManager  entityManager;

        @Test
        @DisplayName("특정 부서를 조회하면 해당 부서원들도 함께 조회되어야 한다.")
        void testFindDept() {
            //given
            Long id =2L;
            //when
            Department department = departmentRepository.findById(id)
                    .orElseThrow();
            //then
            System.out.println("\n\n\n");
            System.out.println("department = " + department);
            System.out.println("department.getEmployees() = " + department.getEmployees());
            System.out.println("\n\n\n");
        }
        
        @Test
        @DisplayName("Lazy 로딩과 Eager 로딩의 차이")
        void testLazyEager() {
            //3번 사원을 조회하고 싶은데 굳이 부서정보는 필요없다..
            //given
            Long id = 3L;
            //when
            Employee employee = employeeRepository.findById(id).orElseThrow();
            
            //then
            System.out.println("\n\n\n");
            System.out.println("employee = " + employee); //lazy 설정으로 조인을 안함
            System.out.println("dept_name = " + employee.getDepartment().getName());
            System.out.println("\n\n\n");
        }

        @Test
        @DisplayName("양방향 연관관계에서 연관데이터의 수정")
        void testChangeDept() {
            //1번 사원의 부서를 1번 부서에서 2번부서로 변경
            //given
             Employee foundEmp = employeeRepository.findById(1L).orElseThrow();

            Department newDept = departmentRepository.findById(2L).orElseThrow();
            //2번 -> 사원의 부서 정보를 업데이트 하면서, 부서에 대한 정보도 수동으로 같이 업데이트
            foundEmp.setDepartment((newDept)); //1번 부서로 다시 세팅
            newDept.getEmployees().add(foundEmp); //수동으로 업데이트
            employeeRepository.save(foundEmp); //변경된 사항 update 해줌

            
            
            //1번 강제커밋
            //변경 감지(더티 체크) 후 변경된 내용을 DB에 즉시 반영하는 역할.
            //entityManager.flush(); //DB로 밀어내기
            //entityManager.clear(); // 영속성 컨텍스트 비우기(비우지 않으면 컨텍스트 내의 정보를 참조하려 함)
            
            //when
            //1번 부서 정보를 조회해서 모든 사원을 보겠다.
            Department foundDept = departmentRepository.findById(2L)
                    .orElseThrow();

            System.out.println("\n\n\n");
            foundDept.getEmployees().forEach((System.out::println));
            System.out.println("\n\n\n");
            //then



        }
    @Test
    @DisplayName("n+1 문제 발생예시")
    void testNPlusEx() {

            //부서 전체가 옴 (총 두개)
        List<Department> departments = departmentRepository.findAll();

        departments.forEach(dept -> { //부서가 올때마다 사원 리스트 출력
            System.out.println("\n\n========사원리스트 ==========");
            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");

        });
        
        //then
    }

    @Test
    @DisplayName("n+1 문제 해결 예시")
    void testPlusSolution() {

        //부서 전체가 옴 (총 두개)
        List<Department> departments = departmentRepository.findAllIncludesEmployees();

        departments.forEach(dept -> { //부서가 올때마다 사원 리스트 출력
            System.out.println("\n\n========사원리스트 ==========");
            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");

        });

        //then
    }


}