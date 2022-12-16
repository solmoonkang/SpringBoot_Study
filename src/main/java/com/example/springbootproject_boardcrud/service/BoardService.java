package com.example.springbootproject_boardcrud.service;

import com.example.springbootproject_boardcrud.domain.repository.BoardRepository;
import com.example.springbootproject_boardcrud.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service               // 서비스 계층임을 명시해주는 어노테이션
@AllArgsConstructor    // Controller에서 봤던 어노테이션으로 Repository를 주입하기 위해 사용
public class BoardService {

    private BoardRepository boardRepository;

    @Transactional      // 선언적 트랜잭션이라 부르며, 트랜잭션을 적용하는 어노테이션
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();   // JpaRepository에서 정의된 메소드로, DB에 INSERT, UPDATE를 담당함 -> 매개변수로는 Entity를 전달
    }
}