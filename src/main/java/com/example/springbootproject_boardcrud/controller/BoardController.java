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

    // 게시글 목록
    // public String list(Model model) {} -> Model 객체를 통해 View에 데이터를 전달
    @GetMapping("/")
    public String list(Model model) {
        List<BoardDto> boardList = boardService.getBoardList();

        model.addAttribute("boardList", boardList);
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
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        // 유동적으로 변하는 PathVariable을 처리하는 방법
        // URL 매핑하는 부분에서 {변수} 처리를 해주면, 메소드 파라미터로 @PathVariable("변수")의 형식으로 받을 수 있다
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);
        return "board/detail.html";
    }

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);
        return "board/update.html";
    }

    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDto) {
        // update() -> 게시글 추가에서 사용하는 boardService.savePost() 메소드를 같이 사용하고 있다
        boardService.savePost(boardDto);

        return "redirect";
    }

    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect";
    }
}
