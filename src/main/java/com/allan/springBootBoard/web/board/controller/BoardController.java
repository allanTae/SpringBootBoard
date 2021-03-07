package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.security.user.domain.UserDetailsVO;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.BoardForm;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.domain.model.BoardVO;
import com.allan.springBootBoard.web.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

    @Autowired
    BoardService boardService;

    // 나중에 삭제 필요.
    // jsp test page.
    @GetMapping("/testJsp")
    public String test(Model model){
        model.addAttribute("serverName", "testServer");
        return "board/testPage";
    }

    // 나중에 삭제 필요.
    // thymeleaf test page.
    @GetMapping("/testThymeleaf")
    public String test(){
        return "thymeleaf/thymeleafTest";
    }

    // 게시판 목록.
    @GetMapping("/getBoardList")
    public String list(Model model,
                       @RequestParam(required = false, defaultValue = "1") int page,
                       @RequestParam(required = false, defaultValue = "1") int range,
                       @RequestParam(required = false, defaultValue = "title") String searchType,
                       @RequestParam(required = false) String keyword,
                       @AuthenticationPrincipal UserDetailsVO user){


        Search search = Search.builder()
                .searchType(searchType)
                .keyword(keyword)
                .build();

        List<BoardVO> boardList = boardService.findAllByMybatis(search);

        search.pageInfo(page, range, boardList.size());

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", search);
        model.addAttribute("userId", user.getUsername());
        return "board/boardList";
    }

    // 게시글 작성 폼.
    @GetMapping("/boardForm")
    public String boardForm(@ModelAttribute("boardForm") BoardForm form,
                            @AuthenticationPrincipal UserDetailsVO user){
        form.setRegisterId(user.getUsername());
        return "board/boardForm";
    }

    // 게시글 저장.
    @PostMapping("/saveBoard")
    public String saveForm(@ModelAttribute BoardForm form, @RequestParam("mode") String mode){

        BoardDTO dto = BoardDTO.builder()
                .boardId(form.getBoardId())
                .title(form.getTitle())
                .content(form.getContent())
                .registerId(form.getRegisterId())
                .tag(form.getTag())
                .build();

        if(mode.equals("edit")){
            boardService.update(dto, form.getRegisterId());
            log.info("update execute!!");
        }else{
            boardService.save(7L, dto); // 현재 작성자와 카테고리 정보를 고정으로(4L, 5L)로 고정 해놈. 수정 필요.
            log.info("save execute!!");
        }


        return "redirect:/board/getBoardList";
    }

    // 게시글 내용 확인
    @GetMapping("/boardContent")
    public String getBoardContent(Model model, @RequestParam("boardId") Long boardId,
                                  @AuthenticationPrincipal UserDetailsVO user){
        ReplyDTO dto = new ReplyDTO();
        dto.setRegisterId(user.getUsername());
        model.addAttribute("boardContent", boardService.findOne(boardId));
        model.addAttribute("replyDTO", dto);
        model.addAttribute("userId", user.getUsername());

        return "board/boardContent";
    }

    // 게시물 삭제.
    @GetMapping("/deleteBoard")
    public String deleteBoard(@RequestParam("boardId") Long boardId){
        boardService.deleteById(boardId);
        return "redirect:/board/getBoardList";
    }

    // 게시물 수정.
    @GetMapping("/editForm")
    public String editForm(@RequestParam("boardId") Long boardId, @RequestParam("mode") String mode,
                           Model model){
        Board board = boardService.findOne(boardId);
        BoardForm form = BoardForm.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .tag(board.getTag())
                .registerId(board.getCreatedBy())
                .build();
        model.addAttribute("boardForm", form);
        model.addAttribute("mode", mode);
        return "board/boardForm";
    }

}
