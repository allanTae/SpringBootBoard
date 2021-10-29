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

    public Search(String searchType, String keyword) {
        this.searchType = searchType;
        this.keyword = keyword;
    }
}
