package com.justdeepfried.GyanJyotiLMS.entities;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse <T> (
        List<T> content,
        Integer pageNum,
        Integer pageSize,
        Integer numOfElements,
        Long totalElements
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements()
        );
    }
}
