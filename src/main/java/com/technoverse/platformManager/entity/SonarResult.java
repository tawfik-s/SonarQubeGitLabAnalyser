package com.technoverse.platformManager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sonarstatus")
@Builder
public class SonarResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String gitLabProjectId;

    private String sonarQubeProjectId;

    private String gitBranch;

    private String status;

}
