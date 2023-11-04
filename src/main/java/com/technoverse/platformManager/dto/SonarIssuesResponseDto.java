package com.technoverse.platformManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SonarIssuesResponseDto {
    private long total;
    private long p;
    private long ps;
    private Paging paging;
    private int effortTotal;
    private List<SonarIssueDto> issues;
    private List<SonarFacetsDTO> facets;
}
