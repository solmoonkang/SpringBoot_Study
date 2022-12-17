package com.example.springbootproject_boardcrud.service;

import com.example.springbootproject_boardcrud.domain.entity.BoardEntity;
import com.example.springbootproject_boardcrud.domain.repository.BoardRepository;
import com.example.springbootproject_boardcrud.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service               // 서비스 계층임을 명시해주는 어노테이션
@AllArgsConstructor    // Controller에서 봤던 어노테이션으로 Repository를 주입하기 위해 사용
public class BoardService {

    private static final int BLOCK_PAGE_NUM_COUNT = 5;  // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 4;       // 한 페이지에 존재하는 게시글 수

    private BoardRepository boardRepository;
    // 페이징은 Service에서 Page객체를 활용하는 방법과 알고리즘을 구현하면 된다

    // Controller와 Service 간에 데이터 전달은 dto 객체로 하기 위해, Repository에서 가져온 Entity를 반복문을 통해
    // dto로 변환하는 작업
    /* 기존 getBoardList() 메소드
    @Transactional
    public List<BoardDto> getBoardList() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntities) {
            BoardDto boardDto = BoardDto.builder()
                    .id(boardEntity.getId())
                    .writer(boardEntity.getWriter())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }
     */

    // 기존 getBoardList() -> 페이징을 할 수 있도록 수정
    @Transactional
    public List<BoardDto> getBoardList(Integer pageNum) {
        // repository의 find() 관련 메소드를 호출할 때 Pageable 인터페이스를 구현한 클래스 (PageRequest.od())를 전달하면 페이징을 할 수 있다
            // 첫 번째 인자
                // limit을 의미한다
                // "현재 페이지 번호 - 1"을 계산한 값이며, 실제 페이지 번화와 SQL 조회시 사용되는 limit는 다르기 때문이다
            // 두 번째 인자
                // offset을 의미한다
                // 몇 개를 가져올 것인가?
            // 세 번째 인자
                // 정렬 방식을 결정한다
                // createDate컬럼을 기준으로 오름차순으로 정렬하여 가져온다
            // 반환된 Page 객체의 getContent() 메소드를 호출하면, 엔티티를 리스트로 꺼낼 수 있다
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum -1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));

        List<BoardEntity> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    // getBoardCount() 메소드 -> 전체 게시글 개수를 가져온다
    public Long getBoardCount() {
        return boardRepository.count();
    }

    // getPageList() 메소드 -> 프론트에 노출시킬 페이지 번호 리스트를 계산하는 로직이다
        // 1) 하나의 페이지에는 4개의 게시글을 가져온다
        // 2) 총 5개의 번호를 노출한다
        // 3) 번호를 5개 채우지 못하면(= 게시글이 20개가 안된다면), 존재하는 번호까지만 노출한다
        // 4) UI를 어떻게 구현하느냐에 따라 페이징 로직이 조금씩 다르다
    public Integer[] getPageList(Integer curPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 개수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());
        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산(올림으로 계산)
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;
        // 페이지 시작번호 조정
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;
        // 페이지 번호 할당
        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }
        return pageList;
    }

    @Transactional
    public BoardDto getPost(Long id) {
        // findById() :
            // PK 값을 where 조건으로 하여 데이터를 가져오기 위한 메소드이며, JpaRepository 인터페이스에 정의되어 있다
            // 반환 값은 Optional 타입인데, 엔티티를 쏙 빼오려면 boardEntityWrapper.get(); 이렇게 get() 메소드를 사용해 가져온다
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(boardEntity.getId())
                .writer(boardEntity.getWriter())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdDate(boardEntity.getCreatedDate())
                .build();

        return boardDto;
    }

    //
    @Transactional      // 선언적 트랜잭션이라 부르며, 트랜잭션을 적용하는 어노테이션
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();   // JpaRepository에서 정의된 메소드로, DB에 INSERT, UPDATE를 담당함 -> 매개변수로는 Entity를 전달
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        // deleteById() :
            // PK 값을 where 조건으로 하여 데이터를 삭제하기 위한 메소드이며, JpaRepository 인터페이스에 정의되어 있다
        boardRepository.deleteById(id);
    }

    public List<BoardDto> searchPosts(String keyword) {
        // searchPosts()
            // Repository에서 검색 결과를 받아와 비즈니스 로직을 실행하는 함수이다
            // 마찬가지로 특별한 것은 없으며, Controller <--> Service 간에는 Dto 객체로 전달하는 것이 좋으므로 이와 관련된 로직만 있을 뿐이다
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boardEntities.isEmpty()) return boardDtoList;

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        // Entity를 Dto로 변환하는 작업이 중복해서 발생하여, 이를 함수로 처리하도록 개선
        return BoardDto.builder()
                .id(boardEntity.getId())
                .writer(boardEntity.getWriter())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdDate(boardEntity.getCreatedDate())
                .build();
    }
}