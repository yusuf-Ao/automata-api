package io.aycodes.automataapi.common.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PageResponse {

    private Object  pageContent;
    private int     currentPage;
    private int     totalPages;
    private Long    totalItems;
}
