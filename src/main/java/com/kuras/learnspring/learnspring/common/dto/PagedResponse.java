package com.kuras.learnspring.learnspring.common.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;

public class PagedResponse<T> {
    public ArrayList<T> content;
    public int page;
    public int size;
    public int totalElements;
    public int totalPages;
    public boolean first;
    public boolean last;

    public static <U> PagedResponse<U> fromPage(Page<U> page) {
        PagedResponse<U> resp = new PagedResponse<>();

        resp.content = new ArrayList<>(page.getContent());
        resp.page = page.getNumber();
        resp.size = page.getSize();
        resp.totalElements = (int) page.getTotalElements(); // uzun ise cast gerekebilir
        resp.totalPages = page.getTotalPages();
        resp.first = page.isFirst();
        resp.last = page.isLast();
        return resp;
    }
}
