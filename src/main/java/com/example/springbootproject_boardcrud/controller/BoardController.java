package com.example.springbootproject_boardcrud.controller;

import com.example.springbootproject_boardcrud.domain.repository.BoardRepository;
import com.example.springbootproject_boardcrud.dto.BoardDto;
import com.example.springbootproject_boardcrud.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor     // Bean 주입 방식과 관련이 있으며, 생성자로 Bean 객체를 받는 방식을 해결해주는 어노테이션
// 그래서 BoardService 객체를 주입 받을 때 @Autowired 같은 특별한 어노테이션을 부여하지 않음
public class BoardController {
    private BoardService boardService;
    private final BoardRepository boardRepository;

    // 게시글 목록 -> 기존 존재했던 컨트롤러의 매핑함수 list()
    // public String list(Model model) {} -> Model 객체를 통해 View에 데이터를 전달
    /*
    @GetMapping("/")
    public String list(Model model) {
        List<BoardDto> boardList = boardService.getBoardList();

        model.addAttribute("boardList", boardList);
        return "board/list.html";
    }
     */

    // 게시글 목록 - 페이징
    @GetMapping("/")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        // page 이름으로 넘어오면 파라미터를 받아주고, 없으면 기본 값으로 1을 설정한다
        // 페이지 번호는 서비스 계층의 getPageList() 함수로 넘겨준다
        List<BoardDto> boardList = boardService.getBoardList(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/list.html";
    }

    @GetMapping("/post")
    public String write() {
        return "board/write.html";
    }

    @PostMapping("/")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        // dto : Controller와 Service 사이에서 데이터를 주고받는 객체를 의미
        return "redirect";
    }

    // TODO : 게시글 상세조회 및 수정 페이지, 게시글 수정 및 삭제
    // 게시글 상세조회 페이지
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        // 유동적으로 변하는 PathVariable을 처리하는 방법
        // URL 매핑하는 부분에서 {변수} 처리를 해주면, 메소드 파라미터로 @PathVariable("변수")의 형식으로 받을 수 있다
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);
        return "board/detail.html";
    }

    // 게시글 수정 페이지
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);
        return "board/update.html";
    }

    // 게시글 수정
    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDto) {
        // update() -> 게시글 추가에서 사용하는 boardService.savePost() 메소드를 같이 사용하고 있다
        boardService.savePost(boardDto);

        return "redirect";
    }

    // 게시글 삭제
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect";
    }

    // 게시글 검색
    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        // 기존에 존재했던 컨트롤러에서 매핑함수 search()를 작성한다
        // 특별한 것은 없고, 클라이언트에서 넘겨주는 keyword를 검색어로 활용한
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list.html";
    }
}
