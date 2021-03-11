package com.allan.springBootBoard.web.board.controller;

import com.allan.springBootBoard.web.board.domain.Reply;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.ReplyDTO;
import com.allan.springBootBoard.web.board.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restBoard")
@Slf4j
public class RestBoardController {

    @Autowired
    ReplyService replyService;

    @PostMapping("/getHierarReplyList")
    public List<ReplyDTO> getReplyList(@ModelAttribute BoardDTO dto){
        return replyService.list(dto.getBoardId());
    }

    @PostMapping("/insertHierarReqly/parent")
    public ResponseEntity<String> insertParentReply(@RequestBody ReplyDTO dto){
        try{
            replyService.saveParentReply(dto);
            return new ResponseEntity<String>("댓글 저장 성공", HttpStatus.OK);
        }catch(Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글 저장 실패", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/insertHierarReply/child")
    public ResponseEntity<String> insertChildReply(@RequestBody ReplyDTO dto){
        try{
            if(dto.getReplyGroupOrder() > Reply.MAX_DEPTH){
                return new ResponseEntity<String>("답변 댓글은 최대 " + Reply.MAX_DEPTH + "개 까지 작성 할 수 있습니다.", HttpStatus.FORBIDDEN);
            }
            replyService.saveChildReply(dto);
            return new ResponseEntity<String>("댓글 저장 성공", HttpStatus.OK);
        }catch (Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글 저장 실패", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/updateHierarReply")
    public ResponseEntity<String> updateReply(@RequestBody ReplyDTO dto){
        try{
            replyService.updateReply(dto);
            return new ResponseEntity<String>("댓글 수정 성공", HttpStatus.OK);
        }catch(Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글 수정 실패", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/deleteHierarReply")
    public ResponseEntity<String> deleteReply(@RequestBody ReplyDTO dto){
        try{
            replyService.deleteReply(dto.getReplyId(), dto.getBoardId());
            return new ResponseEntity<String>("댓글 삭제 성공", HttpStatus.OK);
        }catch (Exception e){
            log.error("error message: " + e.getMessage());
            log.error("dto: " + dto.toString());
            return new ResponseEntity<String>("댓글 삭제 실패", HttpStatus.FORBIDDEN);
        }
    }
}
