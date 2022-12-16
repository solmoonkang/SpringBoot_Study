package com.example.springbootproject_boardcrud.dto;

// BoardDto : 데이터 전달 객체
// dto는 Contoller <-> Service <-> Repository 간에 필요한 데이터를 캡슐화한 데이터 전달 객체
// Service에서 Repository 메소드를 호출할 때, Entity를 전달한 이유는 JpaRepository에 정의된 함수들은 미리 정의되어 있기 때문이다
// 그래서 Entity를 전달할 수 밖에 없었는데, 요점은 각 계층에서 필요한 객체 전달은 Entity 객체가 아닌 dto 객체를 통해 주고받는 것이 좋다

import com.example.springbootproject_boardcrud.domain.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    // toEntity()
    // 필요한 Entity는 이런 형식으로 추가하면 된다
    // dto에서 필요한 부분을 빌더패턴을 통해 entity로 만든다
    public BoardEntity toEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .writer(writer)
                .title(title)
                .content(content)
                .build();
        return boardEntity;
    }

    @Builder
    public BoardDto(Long id, String writer, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
