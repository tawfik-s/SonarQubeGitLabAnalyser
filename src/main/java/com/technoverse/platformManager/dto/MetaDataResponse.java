package com.technoverse.platformManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataResponse {
    private String projectVersion;

    private String javaVersion;
}
