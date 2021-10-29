package com.allan.springBootBoard.web.board.domain.model;

import com.allan.springBootBoard.web.board.domain.Board;
import com.allan.springBootBoard.web.member.domain.JoinType;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class BoardDTO {
    private Long boardId;
    private String title;
    private String content;
    private String tag;
    private Long viewCnt;
    private String createdBy;
    private String createdDate;

    // 회원 유형에 따라 게시물 작성자를 표현하기 위한 필드.
    private String nickName;

    @Builder
    public BoardDTO(Long boardId, String title, String content, String tag){
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }

    // BoardController가 게시판 목록을 반환할때, mybatis 에서 사용할 생성자입니다.
    // 게시판 목록에 필요한 정보들만 반환합니다.
    public BoardDTO(Long boardId, String title, Long viewCnt, String createdBy, LocalDateTime createdDate, String memberName, String authId, MemberRole role){
        this.boardId = boardId;
        this.title = title;
        this.viewCnt = viewCnt;
        setCreatedDate(createdDate);
        setNickName(memberName, authId, role);
    }

    public void setNickName(String memberName, String authId, MemberRole role) {
        // 유형이 social 타입이면, nickName을 이름으로 설정하고 그렇지 않다면 아이디로 설정하도록 함.
        if(role.getJoinType().getType().equals(JoinType.NORMAL.getType()) || role.getJoinType().getType().equals(JoinType.NORMAL.getType())){
            this.nickName = authId;
        }else{
            this.nickName = memberName;
        }
    }

    public void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
