package com.technoverse.platformManager.controller;

import com.technoverse.platformManager.dto.SonarFacetsDTO;
import com.technoverse.platformManager.entity.SonarResult;
import com.technoverse.platformManager.repository.SonarResultRepo;
import com.technoverse.platformManager.service.AnalyseGitlabRepoService;
import com.technoverse.platformManager.service.Status;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/sonar-analyser")
public class SonarQubeAnalyseController {

    @Autowired
    private  AnalyseGitlabRepoService analyseGitlabRepoService;
    @Autowired
    private SonarResultRepo sonarResultRepo;


    @GetMapping
    public SonarFacetsDTO analyse() throws Exception {
       return analyseGitlabRepoService.analyzeGitlabRepo("50749092","main"
               ,"scorecard","1.0"
               ,"sqa_a7202e593ad36d9346365d3e26e271cb8f4e6d51"
               ,"glpat-j-g78kgaNuMzE_Ssrapk");
    }

    @Scheduled(cron="${analyser.cron.schedule}")
    public SonarFacetsDTO analyseSchedule() throws Exception {
       return analyseGitlabRepoService.analyzeGitlabRepo("50749092","main"
               ,"scorecard","1.0"
               ,"sqa_a7202e593ad36d9346365d3e26e271cb8f4e6d51"
               ,"glpat-j-g78kgaNuMzE_Ssrapk");
    }

    @GetMapping("/res")
    public Map<String,Long> getServertyReport(){
        return analyseGitlabRepoService.getServertyReport("scorecard");
    }


    //need to be non blocking
    @GetMapping("/analyse")
    public ResponseEntity analyseWithStore() throws Exception {
        long count=sonarResultRepo.findByGitLabProjectIdAndSonarQubeProjectIdAndGitBranchAndStatus("50749092","scorecard","main", String.valueOf(Status.PENDING)).orElse(new ArrayList<>()).size();
        if(count>0){
            return ResponseEntity.noContent().build();
        }
        SonarResult sonarResult=
                sonarResultRepo.save(new SonarResult(null,"50749092","scorecard","main",String.valueOf(Status.PENDING)));
        analyseGitlabRepoService.analyzeGitlabRepoAsync("50749092","main"
                ,"scorecard","1.0"
                ,"sqa_a7202e593ad36d9346365d3e26e271cb8f4e6d51"
                ,"glpat-j-g78kgaNuMzE_Ssrapk",sonarResult);
        return ResponseEntity.accepted().body(sonarResult);
    }


    //todo another end point to test the status from the database
    @GetMapping("/status")
    public ResponseEntity getStatus(){
        String projectId="50749092";
        String sonarQubeProjectKey="scorecard";
        String branchName="main";
        long count=sonarResultRepo.findByGitLabProjectIdAndSonarQubeProjectIdAndGitBranchAndStatus(projectId,sonarQubeProjectKey,branchName, String.valueOf(Status.PENDING)).orElse(new ArrayList<>()).size();
        if(count>0){
            return ResponseEntity.accepted().body(String.valueOf(Status.PENDING));
        }
        SonarFacetsDTO sonarFacetsDTO= analyseGitlabRepoService.getIssuesReport(sonarQubeProjectKey);

        return ResponseEntity.accepted().body(sonarFacetsDTO);
    }
    @GetMapping("/analyse/{sonarResultId}")
    public ResponseEntity getStatus(@PathVariable Long sonarResultId){
        SonarResult sonarResult=sonarResultRepo.findById(sonarResultId).orElseThrow();
        return ResponseEntity.accepted().body(sonarResult);
    }

    // @GetMapping(value = "/res/report",produces = "application/json")
    @GetMapping( "/res/report")
    public SonarFacetsDTO getIssuesForServerity(){
        return analyseGitlabRepoService.getIssuesReport("scorecard");
    }

}
