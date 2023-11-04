package com.technoverse.platformManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SonarFacetsDTO {
    private String property;
    private List<SonarFacetValue> values;

}
