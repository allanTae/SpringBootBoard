package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.BoardForm;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

    @Autowired
    BoardService boardService;

    // 게시판 목록.
    @GetMapping("/getBoardList")
    public String list(Model model,
                       @RequestParam(required = false, defaultValue = "1") int page,
                       @RequestParam(required = false, defaultValue = "1") int range,
                       @RequestParam(required = false, defaultValue = "title") String searchType,
                       @RequestParam(required = false) String keyword,
                       Authentication authentication){

        Search search = Search.builder()
                .searchType(searchType)
                .keyword(keyword)
                .build();

        int boardListCnt = boardService.findBoardListCnt(search);
        log.info("boardListCnt: "+ boardListCnt);

        search.pageInfo(page, range, boardListCnt);
        log.info("startList: " + search.getStartList());
        log.info("listSize: " + search.getListSize());

        model.addAttribute("boardList", boardService.findBoardList(search));
        model.addAttribute("pagination", search);
        model.addAttribute("userId", authentication.getName());
        return "board/boardList";
    }

    // 게시글 작성 폼.
    @GetMapping("/boardForm")
    public String boardForm(@ModelAttribute("boardForm") BoardForm form,
                            Authentication authentication){
        form.setRegisterId(authentication.getName());
        return "board/boardForm";
    }

    // 게시글 저장.
    @PostMapping("/saveBoard")
    public String saveForm(@Valid @ModelAttribute BoardForm form, BindingResult bindingResult, @RequestParam("mode") String mode){

        if(bindingResult.hasErrors()){
            log.info("error: " + bindingResult.toString());
            return "board/boardForm";
        }

        BoardDTO dto = BoardDTO.builder()
                .boardId(form.getBoardId())
                .title(form.getTitle())
                .content(form.getContent())
                .tag(form.getTag())
                .build();

        if(mode.equals("edit")){
            boardService.update(dto, form.getRegisterId());
            log.info("update execute!!");
        }else{
            boardService.save(7L, dto, form.getRegisterId()); // 현재 작성자와 카테고리 정보를 고정으로(4L, 5L)로 고정 해놈. 수정 필요.
            log.info("save execute!!");
        }

        return "redirect:/board/getBoardList";
    }

    // 게시글 내용 확인
    @GetMapping("/boardContent")
    public String getBoardContent(Model model, @RequestParam("boardId") Long boardId,
                                  Authentication authentication){
        ReplyDTO dto = new ReplyDTO();
        dto.setRegisterId(authentication.getName());
        model.addAttribute("boardContent", boardService.findOne(boardId));
        model.addAttribute("replyDTO", dto);
        model.addAttribute("userId", authentication.getName());

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
