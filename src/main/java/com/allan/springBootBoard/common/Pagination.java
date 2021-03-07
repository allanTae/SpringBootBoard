package com.allan.springBootBoard.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@ToString
@Slf4j
public class Pagination {

    private int listSize = 10;             //초기값으로 목록개수를 10으로 셋팅

    private int rangeSize = 10;            //초기값으로 페이지 범위를 10으로 셋팅

    private int page;					   // 현재 목록의 페이지 번호

    private int range;					   // 각 페이지의 범위 시작 번호

    private int listCnt;				   // 전체 게시물의 갯수

    private int pageCnt;					// 전체 페이지의 갯수

    private int startPage;					// 각 페이지 범위 시작 번호

    private int startList;					// 페이징 기능을 위한 sql 페이지 갯수 값.

    private int endPage;					// 각 페이지 범위 끝 번호

    private boolean prev;					// 이전 페이지 여부

    private boolean next;					// 다음 페이지 여부

    public void pageInfo(int page, int range, int listCnt) {
        System.out.println("pagination cons call!!");

        this.page = page;
        this.range = range;
        this.listCnt = listCnt;

        //전체 페이지수
        this.pageCnt = (int) Math.ceil((double)listCnt/listSize);

        //시작 페이지
        this.startPage = (range - 1) * rangeSize + 1 ;

        //끝 페이지
        this.endPage = range * rangeSize;

        //게시판 시작번호
        //mysql 에서 limit 시작 값
        this.startList = (page - 1) * listSize;

        //이전 버튼 상태
        this.prev = range == 1 ? false : true;

        //다음 버튼 상태
        this.next = endPage > pageCnt ? false : true;
        if (this.endPage > this.pageCnt) {
            this.endPage = this.pageCnt;
            this.next = false;
        }
    }


}
