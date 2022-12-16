package com.example.springbootproject_boardcrud.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// DB 테이블과 매핑되는 객체를 정의
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // access는 생성자의 접근 권한을 설정하는 속성이며, 최종적으로 protexted BoardEntity() {}와 동일
                                                    // 파라미터가 없는 기본 생성자를 추가하는 어노테이션(JPA 사용을 위해 기본 생성자 생성은 필수) -> protected인 이유는 Entity 생성을 외부에서 할 필요가 없기 때문
@Getter // @Data : @Getter와 @Setter를 모두 해결, @Getter는 모든 필드에 getter를 자동생성 해주는 어노테이션
@Entity // 객체를 테이블과 매핑할 엔티티라고 JPA에게 알려주는 역할을 하는 어노테이션(엔티티 매핑) -> 즉, @Entity가 붙은 클래스는 JPA가 관리하며, 이를 엔티티 클래스라고 한다
@Table(name = "board")  // 엔티티 클래스와 매핑되는 테이블 정보를 명시하는 어노테이션
public class BoardEntity extends TimeEntity {

    @Id // 테이블의 기본 키임을 명시하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 대체키로 사용할 때, 기본키 값 생성 전략을 명시
    private Long id;

    @Column(length = 10, nullable = false)  // 컬럼을 매핑하는 어노테이션
    private String writer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder    // 빌더패턴 클래스를 생성해주는 어노테이션으로 @Setter 사용 대신 빌더패턴을 사용해야 안정성을 보장할 수 있다
    public BoardEntity(Long id, String writer, String title, String content) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
}
