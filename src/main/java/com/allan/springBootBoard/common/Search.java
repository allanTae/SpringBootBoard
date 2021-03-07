package com.allan.springBootBoard.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString(callSuper = true)
public class Search extends Pagination{
    private String searchType;
    private String keyword;

    @Builder
    public Search(String searchType, String keyword,
                  int page, int range, int listCnt) {
        this.searchType = searchType;
        this.keyword = keyword;
    }
}
