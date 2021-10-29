package com.allan.springBootBoard.web.board.service;

import com.allan.springBootBoard.common.Pagination;
import com.allan.springBootBoard.common.Search;
import com.allan.springBootBoard.web.board.domain.model.BoardDTO;
import com.allan.springBootBoard.web.board.domain.model.MyBoardDTO;
import com.allan.springBootBoard.web.board.repository.BoardRepository;
import com.allan.springBootBoard.web.board.repository.mapper.BoardMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyBoardService {

    @NonNull BoardRepository boardRepository;

    @NonNull BoardMapper boardMapper;

    public int getMyBoardListCnt(Long memberId){
        return boardMapper.selectMyBoardListCnt(memberId);
    }

    public List<BoardDTO> getMyBoardList(Long memberId, Search search){
        HashMap<String, Object> pagingInfo = new HashMap<>();
        pagingInfo.put("startList", search.getStartList());
        pagingInfo.put("listSize", search.getListSize());
        pagingInfo.put("searchType", search.getSearchType());
        pagingInfo.put("keyword", search.getKeyword());
        pagingInfo.put("memberId", memberId);
        return boardMapper.selectMyBoardListByLoginId(pagingInfo);
    }
}
