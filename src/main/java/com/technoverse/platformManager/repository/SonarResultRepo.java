package com.technoverse.platformManager.repository;

import com.technoverse.platformManager.entity.SonarResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SonarResultRepo extends JpaRepository<SonarResult,Long> {
    Optional<List<SonarResult>> findByGitLabProjectIdAndSonarQubeProjectIdAndGitBranchAndStatus(String gitLabProjectId, String sonarQubeProjectId, String gitBranch, String status);
}
