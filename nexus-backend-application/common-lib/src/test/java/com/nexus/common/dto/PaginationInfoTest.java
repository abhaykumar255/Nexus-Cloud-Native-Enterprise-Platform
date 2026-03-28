package com.nexus.common.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for PaginationInfo
 */
class PaginationInfoTest {

    @Test
    void builder_shouldCreatePaginationInfoWithAllFields() {
        PaginationInfo paginationInfo = PaginationInfo.builder()
                .page(0)
                .size(10)
                .totalElements(100)
                .totalPages(10)
                .hasNext(true)
                .hasPrevious(false)
                .build();
        
        assertThat(paginationInfo).isNotNull();
        assertThat(paginationInfo.getPage()).isEqualTo(0);
        assertThat(paginationInfo.getSize()).isEqualTo(10);
        assertThat(paginationInfo.getTotalElements()).isEqualTo(100);
        assertThat(paginationInfo.getTotalPages()).isEqualTo(10);
        assertThat(paginationInfo.isHasNext()).isTrue();
        assertThat(paginationInfo.isHasPrevious()).isFalse();
    }

    @Test
    void from_withSpringDataPage_shouldCreatePaginationInfo() {
        List<String> content = Arrays.asList("item1", "item2", "item3");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(content, pageRequest, 100);
        
        PaginationInfo paginationInfo = PaginationInfo.from(page);
        
        assertThat(paginationInfo).isNotNull();
        assertThat(paginationInfo.getPage()).isEqualTo(0);
        assertThat(paginationInfo.getSize()).isEqualTo(10);
        assertThat(paginationInfo.getTotalElements()).isEqualTo(100);
        assertThat(paginationInfo.getTotalPages()).isEqualTo(10);
        assertThat(paginationInfo.isHasNext()).isTrue();
        assertThat(paginationInfo.isHasPrevious()).isFalse();
    }

    @Test
    void from_withSecondPage_shouldHavePreviousAndNext() {
        List<String> content = Arrays.asList("item1", "item2", "item3");
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<String> page = new PageImpl<>(content, pageRequest, 100);
        
        PaginationInfo paginationInfo = PaginationInfo.from(page);
        
        assertThat(paginationInfo.getPage()).isEqualTo(1);
        assertThat(paginationInfo.isHasNext()).isTrue();
        assertThat(paginationInfo.isHasPrevious()).isTrue();
    }

    @Test
    void from_withLastPage_shouldNotHaveNext() {
        List<String> content = Arrays.asList("item1", "item2");
        PageRequest pageRequest = PageRequest.of(9, 10);
        Page<String> page = new PageImpl<>(content, pageRequest, 92);
        
        PaginationInfo paginationInfo = PaginationInfo.from(page);
        
        assertThat(paginationInfo.getPage()).isEqualTo(9);
        assertThat(paginationInfo.getTotalPages()).isEqualTo(10);
        assertThat(paginationInfo.isHasNext()).isFalse();
        assertThat(paginationInfo.isHasPrevious()).isTrue();
    }

    @Test
    void from_withSinglePage_shouldNotHaveNextOrPrevious() {
        List<String> content = Arrays.asList("item1", "item2");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(content, pageRequest, 2);
        
        PaginationInfo paginationInfo = PaginationInfo.from(page);
        
        assertThat(paginationInfo.getPage()).isEqualTo(0);
        assertThat(paginationInfo.getTotalPages()).isEqualTo(1);
        assertThat(paginationInfo.isHasNext()).isFalse();
        assertThat(paginationInfo.isHasPrevious()).isFalse();
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyPaginationInfo() {
        PaginationInfo paginationInfo = new PaginationInfo();
        
        assertThat(paginationInfo).isNotNull();
        assertThat(paginationInfo.getPage()).isEqualTo(0);
        assertThat(paginationInfo.getSize()).isEqualTo(0);
        assertThat(paginationInfo.getTotalElements()).isEqualTo(0);
    }

    @Test
    void allArgsConstructor_shouldCreatePaginationInfoWithAllFields() {
        PaginationInfo paginationInfo = new PaginationInfo(2, 20, 200, 10, true, true);
        
        assertThat(paginationInfo.getPage()).isEqualTo(2);
        assertThat(paginationInfo.getSize()).isEqualTo(20);
        assertThat(paginationInfo.getTotalElements()).isEqualTo(200);
        assertThat(paginationInfo.getTotalPages()).isEqualTo(10);
        assertThat(paginationInfo.isHasNext()).isTrue();
        assertThat(paginationInfo.isHasPrevious()).isTrue();
    }
}

