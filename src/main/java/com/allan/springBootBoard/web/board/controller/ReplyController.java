package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.service.ReplyService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/boards/{boardId}")
@Slf4j
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @GetMapping("/replies")
    public ResponseEntity<List<ReplyDTO>> getReplyList(@RequestParam("boardId") Long boardId){
        return new ResponseEntity<List<ReplyDTO>>(replyService.list(boardId), HttpStatus.OK);
    }

    @PostMapping("/replies/parent-reply")
    public ResponseEntity<ReplyResponse> insertParentReply(@RequestBody ReplyDTO dto){
        try{
            replyService.saveParentReply(dto);
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("200", ReplyResponse.REPLY_ENROLL_SUCCESS), HttpStatus.OK);
        }catch(Exception e){
            log.error("ReplyController's error message: " + e.getMessage());
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("403", ReplyResponse.REPLY_ENROLL_FAIL), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/replies/child-reply")
    public ResponseEntity<ReplyResponse> insertChildReply(@RequestBody ReplyDTO dto){
        try{
            if(dto.getParentDepth() >= Reply.MAX_DEPTH){
                return new ResponseEntity<ReplyResponse>(new ReplyResponse("403", ReplyResponse.CHILD_REPLY_MAX_COUNT)
                                                            , HttpStatus.FORBIDDEN);
            }
            replyService.saveChildReply(dto);
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("200", ReplyResponse.REPLY_ENROLL_SUCCESS), HttpStatus.OK);
        }catch (Exception e){
            log.error("exception: " + e.toString());
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("403", ReplyResponse.REPLY_ENROLL_FAIL), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/replies/{replyId}")
    public ResponseEntity<ReplyResponse> updateReply(@RequestBody ReplyDTO dto, @PathVariable("replyId") Long replyId){
        try{
            replyService.updateReply(dto);
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("200", replyId, ReplyResponse.REPLY_REVICE_SUCCESS), HttpStatus.OK);
        }catch(Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("403", replyId, ReplyResponse.REPLY_REVICE_FAIL), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<ReplyResponse> deleteReply(@RequestBody ReplyDTO dto, @PathVariable("replyId") Long replyId){
        try{
            replyService.deleteReply(dto);
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("200", replyId, ReplyResponse.REPLY_REMOVE_SUCCESS), HttpStatus.OK);
        }catch (Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<ReplyResponse>(new ReplyResponse("403", replyId, ReplyResponse.REPLY_REMOVE_FAIL), HttpStatus.FORBIDDEN);
        }
    }

    @Getter
    @Setter
    public class ReplyResponse{

        public static final String REPLY_ENROLL_SUCCESS = "댓글 등록 성공";
        public static final String REPLY_ENROLL_FAIL = "댓글 등록 실패";
        public static final String REPLY_REVICE_SUCCESS = "댓글 수정 성공";
        public static final String REPLY_REVICE_FAIL = "댓글 수정 실패";
        public static final String REPLY_REMOVE_SUCCESS = "댓글 삭제 성공";
        public static final String REPLY_REMOVE_FAIL = "댓글 삭제 실패";
        public static final String CHILD_REPLY_MAX_COUNT = "답변 댓글은 최대 " + Reply.MAX_DEPTH + "개 까지 작성 할 수 있습니다.";

        private ReplyResponse(String status, String message){
            this.status = status;
            this.message = message;
        }

        private ReplyResponse(String status, Long replyId, String message){
            this.status = status;
            this.message = "댓글 ID: " + replyId + " " + message;
        }

        private String status;
        private String message;
    }
}
