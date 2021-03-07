package com.allan.springBootBoard.web.board.repository.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReplyMapper {
    int deleteAll();
}
