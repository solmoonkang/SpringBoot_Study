package com.example.springbootproject_boardcrud.controller;

import com.example.springbootproject_boardcrud.dto.BoardDto;
import com.example.springbootproject_boardcrud.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor     // Bean 주입 방식과 관련이 있으며, 생성자로 Bean 객체를 받는 방식을 해결해주는 어노테이션
// 그래서 BoardService 객체를 주입 받을 때 @Autowired 같은 특별한 어노테이션을 부여하지 않음
public class BoardController {

    private BoardService boardService;

    @GetMapping("/")
    public String list() {
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
}
