package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.common.Pagination;
import com.allan.springBootBoard.infra.AuthenticationConverter;
import com.allan.springBootBoard.web.board.domain.model.MyBoardDTO;
import com.allan.springBootBoard.web.board.service.MyBoardService;
import com.allan.springBootBoard.web.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/myBoard")
public class MyBoardController {

    @Autowired
    MyBoardService myBoardService;

    @Autowired
    AuthenticationConverter authenticationConverter;

    @GetMapping("/getBoardList")
    public String myBoardList(Authentication authentication, Model model,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "1") int range){

        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);

        int listCnt = myBoardService.getMyBoardListCnt(findMember.getMemberId());
        Pagination pagination = new Pagination(page, range, listCnt);


        List<MyBoardDTO> myBoards = myBoardService.getMyBoardList(findMember.getMemberId(), pagination);
        model.addAttribute("boardList", myBoards);
        model.addAttribute("pagination", pagination);

        model.addAttribute("user_name", findMember.getName());
        return "board/myBoardList";
    }
}
