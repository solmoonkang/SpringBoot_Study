package com.example.springbootproject_boardcrud.domain.repository;

import com.example.springbootproject_boardcrud.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository는 인터페이스로 정의하고 JpaRepository 인터페이스를 상속받으면 된다
// JpaRepository의 제네릭 타입에는 Entity 클래스와 PK 타입을 명시해준다
// JpaRepository에는 일반적으로 많이 사용하는 데이터 조작을 다루는 함수가 정의되어 CRUD 작업이 편함
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 검색은 Repository에서 함수만 잘 작성해주면 Like 검색이 된다

    // 검색을 직접적으로 호출하는 메소드
    // JpaRepository에서 메소드명의 By 이후는 SQL의 where 조건 절에 대응되는 것인데, 이렇게 Containing을 붙여주면 Like 검색이 된다
        // 즉, 해당 함수는 %{keyword}% 형식으로 표현된다
    List<BoardEntity> findByTitleContaining(String keyword);

    /*
    *  그 이외의 JPA like query 사용방법
    *  1. StarsWith
    *   - 검색어로 시작하는 Like 검색
    *   - {keyword}%
    *  2. EndsWith
    *   - 검색어로 끝나는 Like 검색
    *   - %{keyword}
    *  3. IgnoreCase
    *   - 대소문자 구분 없이 검색
    *  4. Not
    *   - 검색어를 포함하지 않는 검색
     */
}
