package com.technoverse.platformManager.service;

import com.google.gson.Gson;
import com.technoverse.platformManager.dto.SonarFacetsDTO;
import com.technoverse.platformManager.dto.SonarIssueDto;
import com.technoverse.platformManager.dto.SonarIssuesResponseDto;
import com.technoverse.platformManager.entity.SonarResult;
import com.technoverse.platformManager.repository.SonarResultRepo;
import com.technoverse.platformManager.utils.GitLabDownloadUtil;
import com.technoverse.platformManager.utils.MavenBuildUtil;
import com.technoverse.platformManager.utils.SonarAnalyzerUtil;
import com.technoverse.platformManager.utils.UnCompressUtil;
import org.sonar.wsclient.SonarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.technoverse.platformManager.utils.FileDirectoryCleaningUtil.cleanDirectories;

@Service
public class AnalyseGitlabRepoService {


    //we may need to make request nonblocking
    //todo global configuration for working path  done
    //todo files should be cleaned after finish   done
    //todo unique id  scanid => unique for each job   done
    //todo return the report

    @Value("${sonar.work.directory}")
    private String sonarWorkDirectory;

    @Value("${sonar.login}")
    private String sonarLogin;

    @Value("${sonar.password}")
    private String sonarPassword;

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${maven.home}")
    private String mavenHome;
    private final SonarResultRepo sonarResultRepo;

    public AnalyseGitlabRepoService(SonarResultRepo sonarResultRepo) {
        this.sonarResultRepo = sonarResultRepo;
    }

    public SonarFacetsDTO analyzeGitlabRepo(String projectId, String branchName, String sonarQubeProjectKey, String version, String SonarQubeToken, String gitLabToken) throws Exception {
        String projectScanId = projectId + branchName + (new Random().nextInt(1000) + 1);
        String savingPath = sonarWorkDirectory + '/' + projectScanId + ".tar.gz";
        try {
            ensureWorkdirectoryExist();

            GitLabDownloadUtil.downloadFile(savingPath, projectId, "https://gitlab.com", gitLabToken, branchName); //remove sha

            UnCompressUtil.unTar(savingPath, sonarWorkDirectory + '/' + projectScanId);

            MavenBuildUtil.build(sonarWorkDirectory + '/' + projectScanId,mavenHome);

            SonarAnalyzerUtil.runSonarAnalyzer(sonarWorkDirectory + '/' + projectScanId,sonarUrl, sonarQubeProjectKey, version, SonarQubeToken);

        } catch (Exception exception) {
            cleanDirectories(savingPath, sonarWorkDirectory + '/' + projectScanId);
            throw exception;
        }
        cleanDirectories(savingPath, sonarWorkDirectory + '/' + projectScanId);
        return getIssuesReport(sonarQubeProjectKey);
    }


    @Async
    public CompletableFuture<Void> analyzeGitlabRepoAsync(String projectId, String branchName, String sonarQubeProjectKey, String version, String sonarQubeToken, String gitLabToken,SonarResult sonarResult) throws Exception {

        return CompletableFuture.runAsync(() -> {
            try {
                analyzeGitlabRepo(projectId,branchName
                        ,sonarQubeProjectKey,version
                        ,sonarQubeToken
                        ,gitLabToken);
                sonarResult.setStatus(String.valueOf(Status.DONE));
                sonarResultRepo.save(sonarResult);
            } catch (Exception e) {
                sonarResult.setStatus(String.valueOf(Status.ERROR));
                sonarResultRepo.save(sonarResult);
                throw new RuntimeException(e);
            }
        });
    }

    private void ensureWorkdirectoryExist() {
        File sonarWorkdir=new File(sonarWorkDirectory);
        if(!sonarWorkdir.exists()){
            sonarWorkdir.mkdirs();
        }
    }

    public Map<String, Long> getServertyReport(String sonarWorkDirectory) {
        HashMap<String, Long> res = new HashMap<>();
        long p = 1;
        while (true) {
            SonarIssuesResponseDto sonarIssuesResponseDto = getProjectReport(sonarWorkDirectory, 100, p++);
            if (sonarIssuesResponseDto.getIssues().isEmpty()) {
                break;
            }
            for (SonarIssueDto sonarIssueDto : sonarIssuesResponseDto.getIssues()) {
                res.put(sonarIssueDto.getSeverity(), res.getOrDefault(sonarIssueDto.getSeverity(), 0l) + 1);
            }
        }

        return res;
    }


    public SonarIssuesResponseDto getProjectReport(String sonarQubeProjectKey, long ps, long p) {
        SonarClient sonarClient = SonarClient.builder()
                .url(sonarUrl)
                .login(sonarLogin)
                .password(sonarPassword)
                .build();

        String sonarEndpoint = "/api/issues/search?componentKeys=" + sonarQubeProjectKey + "&ps=" + ps + "&p=" + p;

        String sonarResponse = sonarClient.get(sonarEndpoint);
        Gson gson = new Gson();
        SonarIssuesResponseDto sonarIssuesResponseDto = gson.fromJson(sonarResponse, SonarIssuesResponseDto.class);
        return sonarIssuesResponseDto;
    }


    public SonarFacetsDTO getIssuesReport(String sonarQubeProjectKey) {
        SonarClient sonarClient = SonarClient.builder()
                .url(sonarUrl)
                .login(sonarLogin)
                .password(sonarPassword)
                .build();

        String sonarEndpoint = "/api/issues/search?componentKeys=scorecard&facets=severities&ps=2";
        String sonarResponse = sonarClient.get(sonarEndpoint);
        Gson gson = new Gson();
        SonarIssuesResponseDto sonarIssuesResponseDto = gson.fromJson(sonarResponse, SonarIssuesResponseDto.class);
        return sonarIssuesResponseDto.getFacets().stream()
                .filter(fact -> fact.getProperty().equalsIgnoreCase("severities"))
                .findFirst().orElse(new SonarFacetsDTO("severities", new ArrayList<>()));
    }
}
