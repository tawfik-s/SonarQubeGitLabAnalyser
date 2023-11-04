package com.technoverse.platformManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SonarIssueDto {
    private String key;
    private String rule;
    private String severity;
    private String component;
    private String project;
    private int line;
    private String hash;
    private List<Flow> flows;
    private TextRange textRange;
    private String status;
    private String message;
    private String effort;
    private String debt;
    private String author;
    private List<String> tags;
    private String creationDate;
    private String updateDate;
    private String type;
    private String scope;
    private boolean quickFixAvailable;
    private List<String> messageFormattings;
    private List<String> codeVariants;
    private String cleanCodeAttribute;
    private String cleanCodeAttributeCategory;
    private List<Impact> impacts;
}
