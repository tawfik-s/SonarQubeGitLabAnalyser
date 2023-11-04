package com.technoverse.platformManager.utils;

import org.gitlab4j.api.GitLabApi;

import java.io.*;

public class GitLabDownloadUtil {

    public static void downloadFile(String savingPath, String projectId, String repoUrl, String token,String commitSha) throws Exception {
        GitLabApi gitLabApi = new GitLabApi(repoUrl, token);

        InputStream file = gitLabApi.getRepositoryApi().getRepositoryArchive(projectId,commitSha);

        FileOutputStream outputStream = new FileOutputStream(savingPath);
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = file.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
    }

}