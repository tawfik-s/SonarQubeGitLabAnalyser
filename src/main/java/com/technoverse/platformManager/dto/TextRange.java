package com.technoverse.platformManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextRange {
    private int startLine;
    private int endLine;
    private int startOffset;
    private int endOffset;
}
