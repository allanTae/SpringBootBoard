package com.allan.springBootBoard.common;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/*
* boardList.jsp 에서 페이징을 처리하기 위해 사용하는 class 입니다.
* rangeSize는 사용자에게 한번에 보여줄 최대 페이지 갯수를 의미합니다.(한마디로 페이지 그룹을 나누기 위한 기준입니다.)
* range는 현재 페이지 그룹값입니다. 예) prev, 1, 2, 3, 4, 5, next 형태로 프론트단에 페이징ui 를 구상중이라고 한다면,
* 1~5의 range는 1이고, 6~10의 range는 2가 됩니다.
* range, rangeSize 값은 프론트단에서 range 값이 변하는 경우, page 를 계산하기 위해서도 사용 됩니다.
* startPae, endPage는 프론트단에서 range에 속한 page를 표현 할 떄 사용합니다.
* */

@Getter @Setter
@ToString
@NoArgsConstructor
public class Pagination {

    private int listSize = 10;              //초기값으로 1개의 페이지가 포함하는 게시글 수, 10으로 설정

    private int rangeSize = 10;             //초기값으로 몇개의 페이지를 그룹화 할지 결정하는 설정 값, 10으로 셋팅

    private int page;					    // 현재 페이지 번호

    private int range;					    // 현재 페이지 그룹 번호

    private int listCnt;				    // 전체 게시물의 갯수

    private int pageCnt;				    // 전체 페이지의 갯수

    private int startPage;					// 현재 페이지 그룹 내 시작 페이지 번호

    private int startList;					// 페이지 그룹 시작 번호, 페이징 기능을 위한 limit 시작 위치 값

    private int endPage;					// 현재 페이지 그룹 내 마지막 페이지 번

    private boolean prev;					// 이전 페이지 여부

    private boolean next;					// 다음 페이지 여부

    public Pagination(int page, int range, int listCnt) {
        this.page = page;
        this.range = range;
        this.listCnt = listCnt;
        pageInfo(page, range, listCnt);
    }

    public void pageInfo(int page, int range, int listCnt) {

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
