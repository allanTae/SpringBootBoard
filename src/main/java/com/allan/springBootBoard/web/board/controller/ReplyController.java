package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.service.ReplyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/boards/{boardId}")
@Slf4j
public class ReplyController {

    @NonNull
    ReplyService replyService;

    @GetMapping("/replies")
    public List<ReplyDTO> getReplyList(@ModelAttribute BoardDTO dto){
        return replyService.list(dto.getBoardId());
    }

    @PostMapping("/replies/parent-reply")
    public ResponseEntity<String> insertParentReply(@RequestBody ReplyDTO dto){
        try{
            replyService.saveParentReply(dto);
            return new ResponseEntity<String>("댓글 저장 성공", HttpStatus.OK);
        }catch(Exception e){
            log.error("ReplyController's error message: " + e.getMessage());
            return new ResponseEntity<String>("댓글 저장 실패", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/replies/child-reply")
    public ResponseEntity<String> insertChildReply(@RequestBody ReplyDTO dto){
        try{
            if(dto.getParentDepth() >= Reply.MAX_DEPTH){
                return new ResponseEntity<String>("답변 댓글은 최대 " + Reply.MAX_DEPTH + "개 까지 작성 할 수 있습니다.", HttpStatus.FORBIDDEN);
            }
            replyService.saveChildReply(dto);
            return new ResponseEntity<String>("댓글 저장 성공", HttpStatus.OK);
        }catch (Exception e){
            log.error("exception: " + e.toString());
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글 저장 실패", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/replies/{replyId}")
    public ResponseEntity<String> updateReply(@RequestBody ReplyDTO dto, @PathVariable("replyId") Long replyId){
        try{
            replyService.updateReply(dto);
            return new ResponseEntity<String>("댓글 id: " + replyId + " 내용 수정 성공", HttpStatus.OK);
        }catch(Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글용 id: " + replyId + "내용 수정 실패", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<String> deleteReply(@RequestBody ReplyDTO dto, @PathVariable("replyId") Long replyId){
        try{
            replyService.deleteReply(dto);
            return new ResponseEntity<String>("댓글 id: " + replyId + " 삭제 성공", HttpStatus.OK);
        }catch (Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글 id: " + replyId + " 삭제 실패", HttpStatus.FORBIDDEN);
        }
    }
}
