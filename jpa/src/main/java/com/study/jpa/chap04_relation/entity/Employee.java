package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;

@Setter @Getter
//jpa 연관관계 매핑에서 연관관계 데이터는 toString에서 제외해야 합니다., 양방향일때는 뺴줘야 함
@ToString(exclude = "department") //필드 중 department는 빼줘
@EqualsAndHashCode(of = "id") //아이디가 같으면 같은 객체라 판단하게끔
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_emp")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private long id;

    @Column(name = "emp_name", nullable = false)
    private String name;

    //EAGER : 항상 무조건 조인을 수행
    //LAZY : 필요한 경우에만 조인을 수행 (실무)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id") //foreign key 알려줘야 함 부서와 연결할 fk 키
    private Department department;
    //사원 입장에서는 내가 속한 부서의 정보를 가지고 있을 수 있따.
    //사원은 부서에 속해있으므로  n: 1
    
    //dept_id를 통해 부서에 접근할 수 있게함




}
