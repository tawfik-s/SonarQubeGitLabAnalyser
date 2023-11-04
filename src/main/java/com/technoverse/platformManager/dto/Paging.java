package com.technoverse.platformManager.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paging {
    private int pageIndex;
    private int pageSize;
    private int total;
}
