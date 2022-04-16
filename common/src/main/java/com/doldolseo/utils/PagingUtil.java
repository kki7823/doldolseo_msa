package com.doldolseo.utils;

import org.springframework.data.domain.Page;

public class PagingUtil {
    public int pageNumber; //현재 페이지번호
    public int totalPages;  //총 페이지 수
    public int startBlockPage; //시작 페이지 번호
    public int endBlockPage; //마지막 페이지 번호

    public PagingUtil(int pageBlock, Page page) {
        this.pageNumber = page.getPageable().getPageNumber();
        this.totalPages = page.getTotalPages();
        this.startBlockPage = ((pageNumber) / pageBlock) * pageBlock + 1;
        this.endBlockPage = (pageNumber / pageBlock == totalPages / pageBlock) ?
                totalPages : startBlockPage + pageBlock - 1; //현재페이지가 마지막 블록이면 마지막페이지 = 전체 페이지 수
    }
}
